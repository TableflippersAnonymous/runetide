package com.runetide.services.internal.character.server.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.runetide.common.Constants;
import com.runetide.common.LockManager;
import com.runetide.common.SavingUniqueLoadingManager;
import com.runetide.common.ServiceRegistry;
import com.runetide.services.internal.character.common.CharacterRef;
import com.runetide.services.internal.character.server.dao.CharacterDao;
import com.runetide.services.internal.character.server.domain.LoadedCharacter;
import org.apache.curator.framework.CuratorFramework;
import org.redisson.api.RedissonClient;

import javax.inject.Named;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Singleton
public class CharacterManager extends SavingUniqueLoadingManager<CharacterRef, LoadedCharacter> {
    private final CharacterDao dao;

    @Inject
    public CharacterManager(@Named("myUrl") String myUrl, LockManager lockManager, ServiceRegistry serviceRegistry,
                            ScheduledExecutorService executorService, RedissonClient redissonClient,
                            CuratorFramework curatorFramework, CharacterDao dao) throws Exception {
        super(myUrl, Constants.CHARACTER_LOADING_NAMESPACE, Constants.SAVE_RATE_MS, TimeUnit.MILLISECONDS, lockManager,
                serviceRegistry, executorService, redissonClient, curatorFramework);
        this.dao = dao;
    }

    @Override
    protected void handleSave(CharacterRef key, LoadedCharacter value) throws Exception {

    }

    @Override
    protected void handleDelete(CharacterRef key) throws Exception {

    }

    @Override
    protected LoadedCharacter handleLoad(CharacterRef key) throws Exception {
        return null;
    }

    @Override
    protected void postLoad(CharacterRef key, LoadedCharacter value) {

    }

    @Override
    protected void handleUnload(CharacterRef key, LoadedCharacter value) throws Exception {

    }

    @Override
    protected void postUnload(CharacterRef key) {

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
    protected CharacterRef keyFromString(String key) {
        return null;
    }
}
