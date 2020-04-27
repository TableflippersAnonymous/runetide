package com.runetide.services.internal.resourcepool.server.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.runetide.common.Constants;
import com.runetide.common.LockManager;
import com.runetide.common.ServiceRegistry;
import com.runetide.common.TopicManager;
import com.runetide.services.internal.resourcepool.common.ResourcePoolRef;
import com.runetide.services.internal.resourcepool.common.ResourcePool;
import com.runetide.services.internal.resourcepool.common.ResourcePoolLoadMessage;
import com.runetide.services.internal.resourcepool.server.dao.ResourcePoolDao;
import com.runetide.services.internal.resourcepool.server.domain.LoadedResourcePool;
import com.runetide.services.internal.time.client.TimeClient;
import com.runetide.services.internal.time.common.TickingSavingUniqueLoadingManager;
import org.apache.curator.framework.CuratorFramework;
import org.redisson.api.RedissonClient;

import javax.inject.Named;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Singleton
public class ResourcePoolManager extends TickingSavingUniqueLoadingManager<ResourcePoolRef, LoadedResourcePool> {
    private final ResourcePoolDao resourcePoolDao;
    private final TopicManager topicManager;

    @Inject
    protected ResourcePoolManager(@Named("myUrl") String myUrl, LockManager lockManager,
                                  ServiceRegistry serviceRegistry, ScheduledExecutorService executorService,
                                  RedissonClient redissonClient, CuratorFramework curatorFramework,
                                  TimeClient timeClient, ResourcePoolDao resourcePoolDao,
                                  TopicManager topicManager) throws Exception {
        super(myUrl, Constants.RESOURCE_POOL_LOADING_NAMESPACE, Constants.SAVE_RATE_MS, TimeUnit.MILLISECONDS,
                lockManager, serviceRegistry, executorService, redissonClient, curatorFramework, timeClient,
                topicManager);
        this.resourcePoolDao = resourcePoolDao;
        this.topicManager = topicManager;
    }

    public Collection<LoadedResourcePool> getLoadedResourcePools() {
        return loaded.values();
    }

    public LoadedResourcePool getLoadedResourcePool(final ResourcePoolRef resourcePoolRef) {
        return loaded.get(resourcePoolRef);
    }

    public void createResourcePool(ResourcePool resourcePool) {
        resourcePool.setId(new ResourcePoolRef(UUID.randomUUID()));
        resourcePoolDao.save(resourcePool);
    }

    public void deleteResourcePool(ResourcePoolRef resourcePoolRef) {
        enqueueDelete(resourcePoolRef);
    }

    @Override
    protected void handleTick(ResourcePoolRef key, LoadedResourcePool value, long tick) {
        value.tick(tick);
    }

    @Override
    protected void handleSave(ResourcePoolRef key, LoadedResourcePool value) {
        value.save();
    }

    @Override
    protected void handleDelete(ResourcePoolRef key) {
        resourcePoolDao.delete(resourcePoolDao.get(key));
    }

    @Override
    protected LoadedResourcePool handleLoad(ResourcePoolRef key) {
        return new LoadedResourcePool(resourcePoolDao.get(key), resourcePoolDao, topicManager);
    }

    @Override
    protected void postLoad(ResourcePoolRef key, LoadedResourcePool value) {

    }

    @Override
    protected void handleUnload(ResourcePoolRef key, LoadedResourcePool value) {
        value.save();
    }

    @Override
    protected void postUnload(ResourcePoolRef key) {

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
    protected ResourcePoolRef keyFromString(String key) {
        return ResourcePoolRef.valueOf(key);
    }
}
