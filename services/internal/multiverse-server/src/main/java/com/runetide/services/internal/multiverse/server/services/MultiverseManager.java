package com.runetide.services.internal.multiverse.server.services;

import com.runetide.common.Constants;
import com.runetide.common.loading.UniqueLoadingManager;
import com.runetide.common.services.locking.LockManager;
import com.runetide.common.services.servicediscovery.ServiceRegistry;
import com.runetide.common.services.topics.TopicManager;
import com.runetide.services.internal.multiverse.common.MultiverseRef;
import com.runetide.services.internal.multiverse.server.domain.LoadedMultiverse;
import org.apache.curator.framework.CuratorFramework;
import org.redisson.api.RedissonClient;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.concurrent.ExecutorService;

@Singleton
public class MultiverseManager extends UniqueLoadingManager<MultiverseRef, LoadedMultiverse> {
    @Inject
    public MultiverseManager(@Named("myUrl") String myUrl, LockManager lockManager, ServiceRegistry serviceRegistry,
                             ExecutorService executorService, RedissonClient redissonClient,
                             CuratorFramework curatorFramework, TopicManager topicManager) throws Exception {
        super(myUrl, Constants.MULTIVERSE_LOADING_NAMESPACE, lockManager, serviceRegistry, executorService,
                redissonClient, curatorFramework, topicManager);
    }

    @Override
    protected LoadedMultiverse handleLoad(MultiverseRef key) throws Exception {
        return null;
    }

    @Override
    protected void postLoad(MultiverseRef key, LoadedMultiverse value) {

    }

    @Override
    protected void handleUnload(MultiverseRef key, LoadedMultiverse value) throws Exception {

    }

    @Override
    protected void postUnload(MultiverseRef key) {

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
    protected MultiverseRef keyFromString(String key) {
        return null;
    }
}
