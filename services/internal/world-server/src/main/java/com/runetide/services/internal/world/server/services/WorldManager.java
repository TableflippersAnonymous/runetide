package com.runetide.services.internal.world.server.services;

import com.runetide.common.Constants;
import com.runetide.common.dto.WorldRef;
import com.runetide.common.loading.UniqueLoadingManager;
import com.runetide.common.services.locking.LockManager;
import com.runetide.common.services.servicediscovery.ServiceRegistry;
import com.runetide.common.services.topics.TopicManager;
import com.runetide.services.internal.multiverse.client.MultiversesClient;
import com.runetide.services.internal.world.common.World;
import com.runetide.services.internal.world.server.WorldMapper;
import com.runetide.services.internal.world.server.dao.WorldDao;
import com.runetide.services.internal.world.server.domain.LoadedWorld;
import org.apache.curator.framework.CuratorFramework;
import org.redisson.api.RedissonClient;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.concurrent.ExecutorService;

@Singleton
public class WorldManager extends UniqueLoadingManager<WorldRef, LoadedWorld> {
    private final WorldDao worldDao;
    private final MultiversesClient multiversesClient;

    @Inject
    public WorldManager(@Named("myUrl") final String myUrl, final LockManager lockManager,
                        final ServiceRegistry serviceRegistry, final ExecutorService executorService,
                        final RedissonClient redissonClient, final CuratorFramework curatorFramework,
                        final TopicManager topicManager, final WorldMapper mapper,
                        final MultiversesClient multiversesClient) throws Exception {
        super(myUrl, Constants.WORLD_LOADING_NAMESPACE, lockManager, serviceRegistry, executorService, redissonClient,
                curatorFramework, topicManager);
        this.worldDao = mapper.dao();
        this.multiversesClient = multiversesClient;
    }

    public World getWorld(WorldRef key) {
        return loaded.get(key).getWorld();
    }

    @Override
    protected LoadedWorld handleLoad(WorldRef key) throws Exception {
        return new LoadedWorld(multiversesClient, worldDao.getWorld(key));
    }

    @Override
    protected void postLoad(WorldRef key, LoadedWorld value) {

    }

    @Override
    protected void handleUnload(WorldRef key, LoadedWorld value) throws Exception {

    }

    @Override
    protected void postUnload(WorldRef key) {

    }

    @Override
    protected void handleReset() {
        loaded.values().forEach(LoadedWorld::close);
    }

    @Override
    protected void handleSuspend() {

    }

    @Override
    protected void handleResume() {

    }

    @Override
    protected WorldRef keyFromString(String key) {
        return WorldRef.valueOf(key);
    }
}
