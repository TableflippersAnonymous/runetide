package com.runetide.services.internal.xp.server.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.runetide.common.*;
import com.runetide.services.internal.xp.client.XPClient;
import com.runetide.services.internal.xp.common.XP;
import com.runetide.services.internal.xp.common.XPLoadMessage;
import com.runetide.services.internal.xp.common.XPRef;
import com.runetide.services.internal.xp.server.dao.XPDao;
import com.runetide.services.internal.xp.server.domain.LoadedXP;
import com.runetide.services.internal.xp.server.dto.XPByParent;
import org.apache.curator.framework.CuratorFramework;
import org.redisson.api.RedissonClient;

import javax.inject.Named;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Singleton
public class XPManager extends SavingUniqueLoadingManager<XPRef, LoadedXP> {
    private final XPDao dao;
    private final TopicManager topicManager;
    private final XPClient xpClient;

    @Inject
    public XPManager(@Named("myUrl") String myUrl, LockManager lockManager, ServiceRegistry serviceRegistry,
                     ScheduledExecutorService executorService, RedissonClient redissonClient,
                     CuratorFramework curatorFramework, TopicManager topicManager, XPDao dao,
                     XPClient xpClient) throws Exception {
        super(myUrl, Constants.XP_LOADING_NAMESPACE, Constants.SAVE_RATE_MS, TimeUnit.MILLISECONDS, lockManager,
                serviceRegistry, executorService, redissonClient, curatorFramework, topicManager);
        this.topicManager = topicManager;
        this.dao = dao;
        this.xpClient = xpClient;
    }

    public Collection<LoadedXP> getLoadedXPs() {
        return loaded.values();
    }

    public LoadedXP getLoadedXP(XPRef xpRef) {
        return loaded.get(xpRef);
    }

    public boolean isValid(XPRef xpRef) {
        return dao.get(xpRef) != null;
    }

    public XPRef create(XP value) {
        value.setId(new XPRef(UUID.randomUUID()));
        dao.save(value);
        return value.getId();
    }

    public void delete(XPRef key) {
        enqueueDelete(key);
    }

    @Override
    protected void handleSave(XPRef key, LoadedXP value) {
        value.save();
    }

    @Override
    protected void handleDelete(XPRef key) {
        dao.delete(dao.get(key));
        for(XPByParent xpByParent : dao.getByParent(key))
            enqueueDelete(xpByParent.getId());
    }

    @Override
    protected LoadedXP handleLoad(XPRef key) {
        return new LoadedXP(dao.get(key), dao, topicManager, xpClient);
    }

    @Override
    protected void postLoad(XPRef key, LoadedXP value) {

    }

    @Override
    protected void handleUnload(XPRef key, LoadedXP value) {
        value.unload();
    }

    @Override
    protected void handleReset() {
        loaded.values().forEach(LoadedXP::reset);
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
