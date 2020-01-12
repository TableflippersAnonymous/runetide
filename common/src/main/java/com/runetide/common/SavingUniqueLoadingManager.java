package com.runetide.common;

import org.apache.curator.framework.CuratorFramework;
import org.redisson.api.RedissonClient;
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

    protected SavingUniqueLoadingManager(final String myUrl, final String objectName,
                                         final long saveInterval, final TimeUnit saveTimeUnit,
                                         final LockManager lockManager,
                                         final ServiceRegistry serviceRegistry,
                                         final ScheduledExecutorService executorService,
                                         final RedissonClient redissonClient,
                                         final CuratorFramework curatorFramework) throws InterruptedException {
        super(myUrl, objectName, lockManager, serviceRegistry, executorService, redissonClient, curatorFramework);
        executorService.scheduleAtFixedRate(this::save, saveInterval, saveInterval, saveTimeUnit);
    }

    protected abstract void handleSave(final K key, final V value) throws Exception;

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
}
