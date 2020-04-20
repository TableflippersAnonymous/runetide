package com.runetide.services.internal.xp.client;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.runetide.common.*;
import com.runetide.services.internal.xp.common.*;
import org.apache.curator.framework.CuratorFramework;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

@Singleton
public class XPClient extends UniqueLoadingClient<XPRef> {
    @Inject
    public XPClient(ServiceRegistry serviceRegistry, TopicManager topicManager, String objectName,
                    String basePath, CuratorFramework curatorFramework) {
        super(serviceRegistry, topicManager, objectName, basePath, curatorFramework);
    }

    public XPRef create(final XPType type, final Optional<XPRef> parent) {
        return getTarget()
                .request(ACCEPT)
                .post(Entity.entity(new XP(null, parent.orElse(null), type, 0),
                        MediaType.APPLICATION_JSON), XPRef.class);
    }

    public XP get(final XPRef xpRef) {
        return getTarget(xpRef)
                .request(ACCEPT)
                .get(XP.class);
    }

    public XPTransactResponse transact(final XPRef xpRef, long delta) {
        return getTarget(xpRef)
                .request(ACCEPT)
                .post(Entity.entity(new XPTransactRequest(delta), MediaType.APPLICATION_JSON),
                        XPTransactResponse.class);
    }

    public void delete(final XPRef xpRef) {
        getTarget()
                .path(xpRef.toString())
                .request(ACCEPT)
                .delete();
    }

    public TopicListenerHandle<XPLoadMessage> listenLoad(final XPRef xpRef,
                                                         TopicListener<XPLoadMessage> listener) {
        return topicManager.addListener(Constants.XP_TOPIC_PREFIX + xpRef + ":load", listener,
                XPLoadMessage.class);
    }

    public TopicListenerHandle<XPTransactMessage> listenTransact(final XPRef xpRef,
                                                                 TopicListener<XPTransactMessage> listener) {
        return topicManager.addListener(Constants.XP_TOPIC_PREFIX + xpRef + ":transact", listener,
                XPTransactMessage.class);
    }

    public TopicListenerHandle<XPLevelMessage> listenLevel(final XPRef xpRef,
                                                           TopicListener<XPLevelMessage> listener) {
        return topicManager.addListener(Constants.XP_TOPIC_PREFIX + xpRef + ":level", listener,
                XPLevelMessage.class);
    }

    @Override
    protected WebTarget getTarget(XPRef key) {
        return super.getTarget(key)
                .path(key.toString());
    }
}
