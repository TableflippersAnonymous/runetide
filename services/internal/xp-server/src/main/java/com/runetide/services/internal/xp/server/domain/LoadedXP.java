package com.runetide.services.internal.xp.server.domain;

import com.runetide.common.TopicManager;
import com.runetide.services.internal.xp.common.XP;
import com.runetide.services.internal.xp.server.dao.XPDao;

public class LoadedXP {
    private final XP xp;
    private final XPDao dao;
    private final TopicManager topicManager;
    private final XPClient xpClient;

    public LoadedXP(XP xp, XPDao dao, TopicManager topicManager, XPClient xpClient) {
        this.xp = xp;
        this.dao = dao;
        this.topicManager = topicManager;
        this.xpClient = xpClient;
    }

    public void save() {
        dao.save(xp);
    }

    public synchronized boolean transact(long delta) {
        
    }
}
