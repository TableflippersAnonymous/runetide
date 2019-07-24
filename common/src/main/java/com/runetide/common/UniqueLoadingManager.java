package com.runetide.common;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

public abstract class UniqueLoadingManager<K, V> {
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
    protected final Map<K, V> loaded = new ConcurrentHashMap<>();

    protected UniqueLoadingManager(final String myUrl, final String objectName, final LockManager lockManager,
                                   final ServiceRegistry serviceRegistry, final ExecutorService executorService,
                                   final RedissonClient redissonClient) {
        this.myUrl = myUrl;
        this.objectName = objectName;
        this.lockManager = lockManager;
        this.serviceRegistry = serviceRegistry;
        this.executorService = executorService;
        this.serviceState = redissonClient.getMap("services:" + objectName + ":state", StringCodec.INSTANCE);
        this.serviceClaims = redissonClient.getMap("services:" + objectName + ":claims", StringCodec.INSTANCE);
    }

    protected abstract V handleLoad(final K key);
    protected abstract void handleUnload(final K key, final V value);

    public boolean requestLoad(final K key) {
        if(loaded.containsKey(key))
            return false;
        if(serviceRegistry.get(objectName + ":" + key) != null)
            return false;
        if(lockManager.tryAcquire(objectName + ":" + key) != null)
            return false;
        try {
            serviceClaims.put(key.toString(), myUrl);
            serviceState.put(key.toString(), CLAIMED);
            executorService.submit(() -> load(key));
            return true;
        } catch(final Exception e) {
            lockManager.tryRelease(objectName + ":" + key);
            throw e;
        }
    }

    private void load(final K key) {
        try {
            serviceState.put(key.toString(), LOADING);
            final V value = handleLoad(key);
            loaded.put(key, value);
            serviceRegistry.register(objectName + ":" + key);
            serviceState.put(key.toString(), LOADED);
        } catch(final Exception e) {
            try {
                serviceClaims.remove(key.toString(), myUrl);
            } catch(final Exception e2) {}
            try {
                serviceState.remove(key.toString());
            } catch(final Exception e2) {}
            try {
                serviceRegistry.unregister(objectName + ":" + key);
            } catch(final Exception e2) {}
            try {
                lockManager.tryRelease(objectName + ":" + key);
            } catch(final Exception e2) {}
            throw e;
        }
    }

    public boolean requestUnload(final K key) {
        if(!loaded.containsKey(key))
            return false;
        try {
            serviceState.put(key.toString(), UNLOAD_REQUESTED);
        } catch(final Exception e) {}
        executorService.submit(() -> unload(key));
        return true;
    }

    private void unload(final K key) {
        try {
            serviceState.put(key.toString(), UNLOADING);
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
        try {
            serviceState.remove(key.toString());
        } catch(final Exception e) {}
        try {
            lockManager.tryRelease(objectName + ":" + key);
        } catch(final Exception e) {}
    }
}
