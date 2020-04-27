package com.runetide.common;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.utils.ZKPaths;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.*;

import static com.runetide.common.ServiceState.*;

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
 */
public abstract class UniqueLoadingManager<K, V> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final String myUrl;
    private final String objectName;
    private final LockManager lockManager;
    private final ServiceRegistry serviceRegistry;
    private final ExecutorService executorService;
    private final RMap<String, String> serviceState;
    private final RMap<String, String> serviceClaims;
    private final Lock suspendLock = new ReentrantLock();
    private final Condition suspendCondition = suspendLock.newCondition();
    private final Map<K, LeaderSelector> leaderSelectors = new HashMap<>();
    private final PathChildrenCache pathChildrenCache;
    private final CuratorFramework curatorFramework;
    private final TopicManager topicManager;
    protected final Map<K, V> loaded = new ConcurrentHashMap<>();
    protected volatile boolean isLive;
    protected volatile boolean isLost;

    protected UniqueLoadingManager(final String myUrl, final String objectName, final LockManager lockManager,
                                   final ServiceRegistry serviceRegistry, final ExecutorService executorService,
                                   final RedissonClient redissonClient, final CuratorFramework curatorFramework,
                                   final TopicManager topicManager) throws Exception {
        this.myUrl = myUrl;
        this.objectName = objectName;
        this.lockManager = lockManager;
        this.serviceRegistry = serviceRegistry;
        this.executorService = executorService;
        this.curatorFramework = curatorFramework;
        this.topicManager = topicManager;
        this.pathChildrenCache = new PathChildrenCache(curatorFramework, Constants.ZK_SVC_INTEREST + objectName, false);
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
        startAutoLoad();
    }

    public boolean requestLoad(final K key) {
        if(loaded.containsKey(key))
            return false;
        try {
            if(serviceRegistry.getFirst(objectName + ":" + key) != null)
                return false;
            if(!lockManager.tryAcquire(Constants.LOCK_SVC_PREFIX + objectName + ":" + key))
                return false;
            beginLoad(key);
            return true;
        } catch(final Exception e) {
            LOG.error("[{}] Exception caught requesting load key={}", objectName, key, e);
            lockManager.release(Constants.LOCK_SVC_PREFIX + objectName + ":" + key);
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
    protected abstract K keyFromString(final String key);

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

    protected boolean hasInterest(final K key) {
        return pathChildrenCache.getCurrentData(Constants.ZK_SVC_INTEREST + objectName + "/" + key) != null;
    }

    protected boolean anyLoaded(final K key) {
        try {
            return serviceRegistry.getFirst(objectName + ":" + key) != null;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void startAutoLoad() throws Exception {
        pathChildrenCache.getListenable().addListener((client, event) -> {
            switch(event.getType()) {
                case CHILD_ADDED:
                    shouldAutoLoad(keyFromString(ZKPaths.getNodeFromPath(event.getData().getPath())));
                    break;
                case CHILD_REMOVED:
                    noLongerAutoLoad(keyFromString(ZKPaths.getNodeFromPath(event.getData().getPath())));
                    break;
            }
        });
        pathChildrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
    }

    private synchronized void shouldAutoLoad(final K key) {
        if(leaderSelectors.containsKey(key))
            return;
        final LeaderSelector leaderSelector = new LeaderSelector(curatorFramework,
                Constants.ZK_SVC_LEADERS + objectName + "/" + key, new LeaderSelectorListener() {
            @Override
            public void takeLeadership(CuratorFramework client) throws Exception {
                try {
                    // Need to wait for the older node to be evicted from the lock.
                    if(lockManager.acquire(Constants.LOCK_SVC_PREFIX + objectName + ":" + key) && hasInterest(key))
                        beginLoad(key);
                    else
                        lockManager.release(Constants.LOCK_SVC_PREFIX + objectName + ":" + key);
                } catch(final Exception e) {
                    LOG.error("[{}] Exception caught requesting load key={}", objectName, key, e);
                    lockManager.release(Constants.LOCK_SVC_PREFIX + objectName + ":" + key);
                    throw e;
                }
            }

            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                /* Ignore.  We already handle this in the master listener in the constructor. */
            }
        });
        leaderSelector.start();
        leaderSelectors.put(key, leaderSelector);
    }

    private synchronized void noLongerAutoLoad(final K key) {
        final LeaderSelector leaderSelector = leaderSelectors.remove(key);
        if(leaderSelector == null)
            return;
        requestUnload(key);
        leaderSelector.close();
    }

    private void beginLoad(K key) {
        serviceClaims.put(key.toString(), myUrl);
        setState(key, CLAIMED);
        executorService.submit(() -> load(key));
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
                lockManager.release(Constants.LOCK_SVC_PREFIX + objectName + ":" + key);
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
            lockManager.release(Constants.LOCK_SVC_PREFIX + objectName + ":" + key);
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
                lockManager.release(Constants.LOCK_SVC_PREFIX + objectName + ":" + key);
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

    private void setState(final K key, final ServiceState state) {
        try {
            final ServiceState oldState = Optional.ofNullable(serviceState.put(key.toString(), state.name()))
                    .map(ServiceState::valueOf).orElse(null);
            LOG.info("[{}] State transition for key={} from {} to {}", objectName, key,
                    oldState == null ? "<NONE>" : oldState, state);
            topicManager.publish(Constants.LOADING_TOPIC_PREFIX + objectName + ":" + key,
                    new StateTransitionMessage(objectName, key.toString(), oldState, state));
        } catch (final Exception e) {
            LOG.error("[{}] State transition failed for key={} to {}", objectName, key, state, e);
            throw e;
        }
    }

    private void clearState(final K key, final ServiceState oldState) {
        try {
            if(serviceState.remove(key.toString(), oldState.name())) {
                LOG.info("[{}] State transition for key={} from {} to <NONE>", objectName, key, oldState);
                topicManager.publish(Constants.LOADING_TOPIC_PREFIX + objectName + ":" + key,
                        new StateTransitionMessage(objectName, key.toString(), oldState, null));
            }
        } catch (final Exception e) {
            LOG.error("[{}] State transition failed for key={} to <NONE>", objectName, key, e);
        }
    }
}
