package com.runetide.services.internal.entity.server.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.runetide.common.*;
import com.runetide.common.services.locking.LockManager;
import com.runetide.common.services.servicediscovery.ServiceRegistry;
import com.runetide.common.services.topics.TopicManager;
import com.runetide.services.internal.entity.common.dto.EntityRef;
import com.runetide.services.internal.entity.server.domain.LoadedEntity;
import com.runetide.services.internal.time.client.TimeClient;
import com.runetide.services.internal.time.common.TickingSavingUniqueLoadingManager;
import org.apache.curator.framework.CuratorFramework;
import org.redisson.api.RedissonClient;

import javax.inject.Named;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Singleton
public class EntityManager extends TickingSavingUniqueLoadingManager<EntityRef, LoadedEntity> {
    @Inject
    public EntityManager(@Named("myUrl") final String myUrl, final LockManager lockManager,
                         final ServiceRegistry serviceRegistry, final ScheduledExecutorService executorService,
                         final RedissonClient redissonClient, final CuratorFramework curatorFramework,
                         final TopicManager topicManager, final TimeClient timeClient) throws Exception {
        super(myUrl, Constants.ENTITY_LOADING_NAMESPACE, Constants.SAVE_RATE_MS, TimeUnit.MILLISECONDS, lockManager,
                serviceRegistry, executorService, redissonClient, curatorFramework, timeClient, topicManager);
    }

    @Override
    protected void handleTick(EntityRef key, LoadedEntity value, long tick) throws Exception {
        value.tick();
    }

    @Override
    protected void handleSave(EntityRef key, LoadedEntity value) throws Exception {

    }

    @Override
    protected void handleDelete(EntityRef key) throws Exception {

    }

    @Override
    protected LoadedEntity handleLoad(EntityRef key) throws Exception {
        return null;
    }

    @Override
    protected void postLoad(EntityRef key, LoadedEntity value) {

    }

    @Override
    protected void handleUnload(EntityRef key, LoadedEntity value) throws Exception {

    }

    @Override
    protected void postUnload(EntityRef key) {

    }

    @Override
    protected void handleReset() {

    }

    @Override
    protected void handleSuspend() {

    }

    @Override
    protected void handleResume() {

    }

    @Override
    protected EntityRef keyFromString(String key) {
        return EntityRef.valueOf(key);
    }
}
