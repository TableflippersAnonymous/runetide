package com.runetide.services.internal.time.common;

import com.runetide.common.loading.SavingUniqueLoadingManager;
import com.runetide.common.services.locking.LockManager;
import com.runetide.common.services.servicediscovery.ServiceRegistry;
import com.runetide.common.services.topics.TopicListener;
import com.runetide.common.services.topics.TopicManager;
import com.runetide.services.internal.time.client.TimeClient;
import org.apache.curator.framework.CuratorFramework;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.lang.invoke.MethodHandles;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Like SavingUniqueLoadingManager, but also listens to the global tick rate.
 */
public abstract class TickingSavingUniqueLoadingManager<K, V>
        extends SavingUniqueLoadingManager<K, V>
        implements TopicListener<TimeTickMessage> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ExecutorService executorService;

    protected TickingSavingUniqueLoadingManager(String myUrl, String objectName,
                                                long saveInterval, TimeUnit saveTimeUnit,
                                                LockManager lockManager,
                                                ServiceRegistry serviceRegistry,
                                                ScheduledExecutorService executorService,
                                                RedissonClient redissonClient,
                                                CuratorFramework curatorFramework,
                                                TimeClient timeClient,
                                                TopicManager topicManager) throws Exception {
        super(myUrl, objectName, saveInterval, saveTimeUnit, lockManager, serviceRegistry, executorService,
                redissonClient, curatorFramework, topicManager);
        this.executorService = executorService;
        timeClient.listen(this);
    }

    @Override
    public void onMessage(TimeTickMessage message) {
        awaitLive();
        final Set<K> keys = loaded.keySet();
        for(final K key : keys)
            executorService.submit(() -> tick(key, message.getCurrentTick()));
    }

    /**
     * Called once per tick per loaded object.
     * @param key Key being ticked.
     * @param value Object being ticked.
     * @param tick Tick number.
     * @throws Exception If this method throws an Exception, it will be logged and then ignored.
     */
    protected abstract void handleTick(final K key, final V value, final long tick) throws Exception;

    private void tick(final K key, final long tick) {
        awaitLive();
        final V value = loaded.get(key);
        if(value != null)
            try {
                handleTick(key, value, tick);
            } catch (final Exception e) {
                LOG.error("Caught exception trying to handle tick key={} tick={}", key, tick, e);
            }
    }

    @Override
    public void onClose() {
        /* Ignore */
    }
}
