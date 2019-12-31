package com.runetide.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Singleton
public class LockManagerImpl implements LockManager {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final LoadingCache<String, InterProcessMutex> cache;
    private final Map<String, InterProcessMutex> active = new HashMap<>();

    @Inject
    public LockManagerImpl(final CuratorFramework curatorFramework) {
        this.cache = CacheBuilder.newBuilder()
                .expireAfterAccess(30, TimeUnit.MINUTES)
                .build(new CacheLoader<String, InterProcessMutex>() {
                    @Override
                    public InterProcessMutex load(final String key) {
                        return new InterProcessMutex(curatorFramework, Constants.ZK_LOCKS + key);
                    }
                });
    }

    @Override
    public boolean tryAcquire(final String name) {
        final InterProcessMutex mutex = cache.getUnchecked(name);
        boolean acquired = false;
        try {
            acquired = mutex.acquire(1, TimeUnit.SECONDS);
        } catch (final Exception e) {
            LOG.error("Caught exception acquiring lock={}", name, e);
        }
        if(acquired)
            active.put(name, mutex);
        return acquired;
    }

    @Override
    public void release(final String name) {
        final InterProcessMutex mutex = active.get(name);
        if(mutex == null)
            return;
        try {
            mutex.release();
        } catch (final Exception e) {
            LOG.error("Caught exception releasing lock={}", name, e);
        }
        active.remove(name);
    }
}
