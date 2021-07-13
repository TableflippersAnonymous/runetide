package com.runetide.services.internal.character.server.services;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BatchStatement;
import com.datastax.oss.driver.api.core.cql.BatchType;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.runetide.common.*;
import com.runetide.common.loading.SavingUniqueLoadingManager;
import com.runetide.common.services.locking.LockManager;
import com.runetide.common.services.servicediscovery.ServiceRegistry;
import com.runetide.common.services.topics.TopicManager;
import com.runetide.services.internal.character.common.CharacterRef;
import com.runetide.services.internal.character.server.dao.CharacterDao;
import com.runetide.services.internal.character.server.domain.LoadedCharacter;
import com.runetide.services.internal.entity.client.EntitiesClient;
import com.runetide.services.internal.resourcepool.client.ResourcePoolsClient;
import com.runetide.services.internal.xp.client.XPClient;
import org.apache.curator.framework.CuratorFramework;
import org.redisson.api.RedissonClient;

import javax.inject.Named;
import java.util.Collection;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Singleton
public class CharacterManager extends SavingUniqueLoadingManager<CharacterRef, LoadedCharacter> {
    private final CharacterDao dao;
    private final CqlSession cqlSession;
    private final TopicManager topicManager;
    private final EntitiesClient entitiesClient;
    private final ResourcePoolsClient resourcePoolsClient;
    private final XPClient xpClient;

    @Inject
    public CharacterManager(@Named("myUrl") String myUrl, LockManager lockManager, ServiceRegistry serviceRegistry,
                            ScheduledExecutorService executorService, RedissonClient redissonClient,
                            CuratorFramework curatorFramework, CharacterDao dao, TopicManager topicManager,
                            CqlSession cqlSession, EntitiesClient entitiesClient,
                            ResourcePoolsClient resourcePoolsClient, XPClient xpClient) throws Exception {
        super(myUrl, Constants.CHARACTER_LOADING_NAMESPACE, Constants.SAVE_RATE_MS, TimeUnit.MILLISECONDS, lockManager,
                serviceRegistry, executorService, redissonClient, curatorFramework, topicManager);
        this.dao = dao;
        this.cqlSession = cqlSession;
        this.topicManager = topicManager;
        this.entitiesClient = entitiesClient;
        this.resourcePoolsClient = resourcePoolsClient;
        this.xpClient = xpClient;
    }

    public Collection<LoadedCharacter> getLoaded() {
        return loaded.values();
    }

    public LoadedCharacter getLoaded(CharacterRef key) {
        return loaded.get(key);
    }

    @Override
    protected void handleSave(CharacterRef key, LoadedCharacter value) {
        value.save();
    }

    @Override
    protected void handleDelete(CharacterRef key) {
        final BatchStatement batch = BatchStatement.builder(BatchType.LOGGED)
                .addStatement(dao.prepareDelete(key))
                .addStatement(dao.prepareDeleteAssignments(key))
                .build();
        cqlSession.execute(batch);
        //TODO Delete Entity, XP, ResourcePools
    }

    @Override
    protected LoadedCharacter handleLoad(CharacterRef key) {
        return new LoadedCharacter(dao.getCharacter(key), dao, topicManager, entitiesClient, resourcePoolsClient,
                xpClient).awaitDependencies();
    }

    @Override
    protected void postLoad(CharacterRef key, LoadedCharacter value) {

    }

    @Override
    protected void handleUnload(CharacterRef key, LoadedCharacter value) {
        value.unload();
    }

    @Override
    protected void handleReset() {
        loaded.values().forEach(LoadedCharacter::reset);
    }

    @Override
    protected void handleSuspend() {

    }

    @Override
    protected void handleResume() {

    }

    @Override
    protected CharacterRef keyFromString(String key) {
        return CharacterRef.valueOf(key);
    }
}
