package com.runetide.common;

import org.apache.curator.framework.CuratorFramework;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.ByteArrayCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/*
 * This class is like UniqueLoadingManager, but also periodically calls handleSave() for loaded keys.
 */
public abstract class SavingUniqueLoadingManager<K, V> extends UniqueLoadingManager<K, V> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final String objectName;
    private final LockManager lockManager;
    private final RList<byte[]> toDelete;

    protected SavingUniqueLoadingManager(final String myUrl, final String objectName,
                                         final long saveInterval, final TimeUnit saveTimeUnit,
                                         final LockManager lockManager,
                                         final ServiceRegistry serviceRegistry,
                                         final ScheduledExecutorService executorService,
                                         final RedissonClient redissonClient,
                                         final CuratorFramework curatorFramework) throws Exception {
        super(myUrl, objectName, lockManager, serviceRegistry, executorService, redissonClient, curatorFramework);
        this.objectName = objectName;
        this.lockManager = lockManager;
        this.toDelete = redissonClient.getList(Constants.QUEUE_DELETE_PREFIX + objectName, ByteArrayCodec.INSTANCE);
        executorService.scheduleAtFixedRate(this::save, saveInterval, saveInterval, saveTimeUnit);
        executorService.scheduleAtFixedRate(this::tryDelete, saveInterval, saveInterval, saveTimeUnit);
    }

    protected abstract void handleSave(final K key, final V value) throws Exception;
    protected abstract void handleDelete(final K key) throws Exception;

    protected void enqueueDelete(final K key) {
        if(!tryDeleteOne(key))
            toDelete.add(key.toString().getBytes());
    }

    private void save() {
        awaitLive();
        final Set<K> keys = loaded.keySet();
        for(final K key : keys) {
            awaitLive();
            final V value = loaded.get(key);
            if(value != null)
                try {
                    handleSave(key, value);
                } catch (final Exception e) {
                    LOG.error("Caught exception trying to periodically save key={}", key, e);
                }
        }
    }

    private void tryDelete() {
        awaitLive();
        try {
            if (!lockManager.tryAcquire(Constants.LOCK_DELETE_PREFIX + objectName))
                return;
            for(final byte[] bytes : toDelete) {
                awaitLive();
                final K key = keyFromString(new String(bytes));
                if(tryDeleteOne(key))
                    toDelete.remove(bytes);
            }
        } finally {
            lockManager.release(Constants.LOCK_DELETE_PREFIX + objectName);
        }
    }

    private synchronized boolean tryDeleteOne(final K key) {
        if(loaded.containsKey(key))
            return false;
        if(hasInterest(key))
            return false;
        if(anyLoaded(key))
            return false;
        try {
            if(!lockManager.tryAcquire(Constants.LOCK_SVC_PREFIX + objectName + ":" + key))
                return false;
            handleDelete(key);
            return true;
        } catch (final Exception e) {
            LOG.error("Caught exception trying to delete key={}", key, e);
        } finally {
            lockManager.release(Constants.LOCK_SVC_PREFIX + objectName + ":" + key);
        }
        return false;
    }
}
