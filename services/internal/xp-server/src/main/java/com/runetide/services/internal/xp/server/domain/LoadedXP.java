package com.runetide.services.internal.xp.server.domain;

import com.runetide.common.Constants;
import com.runetide.common.LoadingToken;
import com.runetide.common.TopicManager;
import com.runetide.services.internal.xp.client.XPClient;
import com.runetide.services.internal.xp.common.XP;
import com.runetide.services.internal.xp.common.XPLevelMessage;
import com.runetide.services.internal.xp.common.XPRef;
import com.runetide.services.internal.xp.common.XPTransactMessage;
import com.runetide.services.internal.xp.server.dao.XPDao;

import java.util.Optional;

public class LoadedXP {
    private final XP xp;
    private final XPDao dao;
    private final TopicManager topicManager;
    private final XPClient xpClient;
    private final Optional<LoadingToken<XPRef>> loadingToken;

    public LoadedXP(XP xp, XPDao dao, TopicManager topicManager, XPClient xpClient) {
        this.xp = xp;
        this.dao = dao;
        this.topicManager = topicManager;
        this.xpClient = xpClient;
        this.loadingToken = Optional.ofNullable(xp.getParent())
                .map(xpClient::requestLoad);
    }

    public void save() {
        dao.save(xp);
    }

    public synchronized boolean transact(long delta) {
        final int oldLevel = xp.getLevel();
        final long oldXp = xp.getXp();
        loadingToken.ifPresent(token -> xpClient.transact(token, delta));
        xp.setXp(oldXp + delta);
        if(xp.getLevel() < oldLevel) {
            xp.setXp(oldXp);
            return false;
        }
        save();
        topicManager.publish(Constants.XP_TOPIC_PREFIX + xp.getId() + ":transact",
                new XPTransactMessage(xp.getId(), oldXp, xp.getXp()));
        if(xp.getLevel() > oldLevel) {
            topicManager.publish(Constants.XP_TOPIC_PREFIX + xp.getId() + ":level",
                    new XPLevelMessage(xp.getId(), oldLevel, xp.getLevel()));
        }
        return true;
    }

    public void unload() {
        save();
        reset();
    }

    public void reset() {
        loadingToken.ifPresent(LoadingToken::close);
    }

    public XP getXp() {
        return xp;
    }
}
