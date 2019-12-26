package com.runetide.common;

import org.apache.curator.framework.CuratorFramework;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.*;

/*
 * This provides a manager that only allows one instance to load a key at any given time.
 *
 * This is done using LockManager to acquire locks on the resources and ServiceRegistry to make
 * the owner known to other services.
 *
 * Loading is done in a phased approach:
 * - A global lock on the key is acquired
 * - The local instance is registered as the future owner of the key
 * - The state of the key transitions from <NONE> to CLAIMED
 * - The load of the key is queued in a task list
 * - The load task begins execution
 * - The state of the key transitions from CLAIMED to LOADING
 * - Abstract method handleLoad is called
 * - The service is published to the ServiceRegistry
 * - The state of the key transitions from LOADING to LOADED
 *
 * Unloading is likewise done in a phased approach:
 * - The state of the key transitions from LOADED to UNLOAD_REQUESTED
 * - The unload of the key is queued in a task list
 * - The unload task begins execution
 * - The state of the key transitions from UNLOAD_REQUESTED to UNLOADING
 * - The service is unpublished from the ServiceRegistry
 * - Abstract method handleUnload is called
 * - The local instance is unregistered as the owner of the key
 * - The state of the key transitions from UNLOADING to <NONE>
 * - The global lock on the key is released
 *
 * Failures during loading result in abandoning the load request and attempting clean-up.
 * Failures during unloading result are ignored.
 *
 * Loss of lock results in handleReset being called, followed by attempted clean-up.
 * handleUnload is not called.
 *
 *
 */
public abstract class UniqueLoadingManager<K, V> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String CLAIMED = "CLAIMED";
    private static final String LOADING = "LOADING";
    private static final String LOADED = "LOADED";
    private static final String UNLOAD_REQUESTED = "UNLOAD_REQUESTED";
    private static final String UNLOADING = "UNLOADING";

    private final String myUrl;
    private final String objectName;
    private final LockManager lockManager;
    private final ServiceRegistry serviceRegistry;
    private final ExecutorService executorService;
    private final RMap<String, String> serviceState;
    private final RMap<String, String> serviceClaims;
    private final Lock suspendLock = new ReentrantLock();
    private final Condition suspendCondition = suspendLock.newCondition();
    protected final Map<K, V> loaded = new ConcurrentHashMap<>();
    protected volatile boolean isLive;
    protected volatile boolean isLost;

    protected UniqueLoadingManager(final String myUrl, final String objectName, final LockManager lockManager,
                                   final ServiceRegistry serviceRegistry, final ExecutorService executorService,
                                   final RedissonClient redissonClient, final CuratorFramework curatorFramework) throws InterruptedException {
        this.myUrl = myUrl;
        this.objectName = objectName;
        this.lockManager = lockManager;
        this.serviceRegistry = serviceRegistry;
        this.executorService = executorService;
        this.serviceState = redissonClient.getMap("services:" + objectName + ":state", StringCodec.INSTANCE);
        this.serviceClaims = redissonClient.getMap("services:" + objectName + ":claims", StringCodec.INSTANCE);
        curatorFramework.getConnectionStateListenable().addListener((curatorFramework1, connectionState) -> {
            switch(connectionState) {
                case CONNECTED:
                case RECONNECTED:
                    resume();
                    break;
                case SUSPENDED:
                    suspend();
                    break;
                case LOST:
                    suspend();
                    reset();
                    break;
            }
        });
        curatorFramework.blockUntilConnected();
        isLive = true;
    }

    public boolean requestLoad(final K key) {
        if(loaded.containsKey(key))
            return false;
        try {
            if(serviceRegistry.getFirst(objectName + ":" + key) != null)
                return false;
            if(!lockManager.tryAcquire(objectName + ":" + key))
                return false;
            serviceClaims.put(key.toString(), myUrl);
            setState(key, CLAIMED);
            executorService.submit(() -> load(key));
            return true;
        } catch(final Exception e) {
            LOG.error("[{}] Exception caught requesting load key={}", objectName, key, e);
            lockManager.tryRelease(objectName + ":" + key);
            throw new RuntimeException(e);
        }
    }

    public boolean requestUnload(final K key) {
        if(!loaded.containsKey(key))
            return false;
        try {
            setState(key, UNLOAD_REQUESTED);
            executorService.submit(() -> unload(key));
            return true;
        } catch(final Exception e) {
            LOG.error("[{}] Exception caught requesting unload key={}", objectName, key, e);
            throw e;
        }
    }

    public URI getUri(final K key) {
        return URI.create(serviceClaims.get(key.toString()));
    }

    protected abstract V handleLoad(final K key) throws Exception;
    protected abstract void postLoad(final K key, final V value);
    protected abstract void handleUnload(final K key, final V value) throws Exception;
    protected abstract void postUnload(final K key);
    protected abstract void handleReset();
    protected abstract void handleSuspend();
    protected abstract void handleResume();

    protected void awaitLive() {
        suspendLock.lock();
        try {
            while (!isLive && !isLost)
                suspendCondition.awaitUninterruptibly();
            if (isLost)
                throw new RuntimeException("Lock lost.");
        } finally {
            suspendLock.unlock();
        }
    }

    private void load(final K key) {
        try {
            awaitLive();
            setState(key, LOADING);
            final V value = handleLoad(key);
            loaded.put(key, value);
            serviceRegistry.register(objectName + ":" + key);
            setState(key, LOADED);
            postLoad(key, value);
        } catch(final Exception e) {
            LOG.error("[{}] Exception caught loading key={}", objectName, key, e);
            try {
                serviceClaims.remove(key.toString(), myUrl);
            } catch(final Exception e2) {}
            clearState(key, LOADING);
            try {
                serviceRegistry.unregister(objectName + ":" + key);
            } catch(final Exception e2) {}
            try {
                lockManager.tryRelease(objectName + ":" + key);
            } catch(final Exception e2) {}
            throw new RuntimeException(e);
        }
    }

    private void unload(final K key) {
        awaitLive();
        try {
            setState(key, UNLOADING);
        } catch(final Exception e) {}
        try {
            serviceRegistry.unregister(objectName + ":" + key);
        } catch(final Exception e) {}
        try {
            final V value = loaded.remove(key);
            if (value != null)
                handleUnload(key, value);
        } catch(final Exception e) {}
        try {
            serviceClaims.remove(key.toString(), myUrl);
        } catch(final Exception e) {}
        clearState(key, UNLOADING);
        try {
            lockManager.tryRelease(objectName + ":" + key);
        } catch(final Exception e) {}
        postUnload(key);
    }

    private void reset() {
        setLost(true);
        serviceRegistry.clear();
        handleReset();
        for(final K key : loaded.keySet()) {
            try {
                serviceClaims.remove(key.toString(), myUrl);
            } catch(final Exception e) {}
            try {
                lockManager.tryRelease(objectName + ":" + key);
            } catch(final Exception e) {}
        }
        loaded.clear();
    }

    private void suspend() {
        setLive(false);
        handleSuspend();
    }

    private void resume() {
        setLost(false);
        setLive(true);
        handleResume();
    }

    private void setLive(final boolean value) {
        suspendLock.lock();
        try {
            isLive = value;
            suspendCondition.signalAll();
        } finally {
            suspendLock.unlock();
        }
    }

    private void setLost(final boolean value) {
        suspendLock.lock();
        try {
            isLost = value;
            suspendCondition.signalAll();
        } finally {
            suspendLock.unlock();
        }
    }

    private void setState(final K key, final String state) {
        try {
            final String oldState = serviceState.put(key.toString(), state);
            LOG.info("[{}] State transition for key={} from {} to {}", objectName, key,
                    oldState == null ? "<NONE>" : oldState, state);
        } catch (final Exception e) {
            LOG.error("[{}] State transition failed for key={} to {}", objectName, key, state, e);
            throw e;
        }
    }

    private void clearState(final K key, final String oldState) {
        try {
            serviceState.remove(key.toString(), oldState);
            LOG.info("[{}] State transition for key={} from {} to <NONE>", objectName, key,
                    oldState == null ? "<NONE>" : oldState);
        } catch (final Exception e) {
            LOG.error("[{}] State transition failed for key={} to <NONE>", objectName, key, e);
        }
    }
}
