package com.runetide.services.internal.xp.server.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.runetide.common.*;
import com.runetide.services.internal.xp.common.XPLoadMessage;
import com.runetide.services.internal.xp.common.XPRef;
import com.runetide.services.internal.xp.server.dao.XPDao;
import com.runetide.services.internal.xp.server.domain.LoadedXP;
import org.apache.curator.framework.CuratorFramework;
import org.redisson.api.RedissonClient;

import javax.inject.Named;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Singleton
public class XPManager extends SavingUniqueLoadingManager<XPRef, LoadedXP> {
    private final XPDao dao;
    private final TopicManager topicManager;

    @Inject
    public XPManager(@Named("myUrl") String myUrl, LockManager lockManager, ServiceRegistry serviceRegistry,
                     ScheduledExecutorService executorService, RedissonClient redissonClient,
                     CuratorFramework curatorFramework, TopicManager topicManager, XPDao dao) throws Exception {
        super(myUrl, Constants.XP_LOADING_NAMESPACE, Constants.SAVE_RATE_MS, TimeUnit.MILLISECONDS, lockManager,
                serviceRegistry, executorService, redissonClient, curatorFramework);
        this.topicManager = topicManager;
        this.dao = dao;
    }

    @Override
    protected void handleSave(XPRef key, LoadedXP value) {
        value.save();
    }

    @Override
    protected void handleDelete(XPRef key) {
        dao.delete(dao.get(key));
    }

    @Override
    protected LoadedXP handleLoad(XPRef key) {
        return new LoadedXP(dao.get(key), dao, topicManager);
    }

    @Override
    protected void postLoad(XPRef key, LoadedXP value) {
        topicManager.publish(Constants.XP_TOPIC_PREFIX + key + ":load", new XPLoadMessage(key));
    }

    @Override
    protected void handleUnload(XPRef key, LoadedXP value) {
        value.save();
    }

    @Override
    protected void postUnload(XPRef key) {

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
    protected XPRef keyFromString(String key) {
        return XPRef.valueOf(key);
    }
}
