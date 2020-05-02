package com.runetide.services.internal.xp.client;

import com.datastax.oss.driver.api.core.type.codec.TypeCodec;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.runetide.common.*;
import com.runetide.common.clients.LoadingToken;
import com.runetide.common.clients.UniqueLoadingClient;
import com.runetide.common.services.cql.EnumIndexedCodec;
import com.runetide.common.services.cql.UUIDRefCodec;
import com.runetide.common.services.servicediscovery.ServiceRegistry;
import com.runetide.common.services.topics.TopicListener;
import com.runetide.common.services.topics.TopicListenerHandle;
import com.runetide.common.services.topics.TopicManager;
import com.runetide.services.internal.xp.common.*;
import org.apache.curator.framework.CuratorFramework;
import org.redisson.api.RedissonClient;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Singleton
public class XPClient extends UniqueLoadingClient<XPRef> {
    @Inject
    public XPClient(ServiceRegistry serviceRegistry, TopicManager topicManager, String objectName,
                    String basePath, CuratorFramework curatorFramework, RedissonClient redissonClient) {
        super(serviceRegistry, topicManager, objectName, basePath, curatorFramework, redissonClient);
    }

    public static List<TypeCodec<?>> getCqlTypeCodecs() {
        return Arrays.asList(
                new EnumIndexedCodec<>(XPType.class),
                new UUIDRefCodec<>(XPRef.class, XPRef::new)
        );
    }

    public XPRef create(final XPType type, final Optional<XPRef> parent) {
        return getTarget()
                .request(ACCEPT)
                .post(Entity.entity(new XP(null, parent.orElse(null), type, 0),
                        MediaType.APPLICATION_JSON), XPRef.class);
    }

    public XP get(final LoadingToken<XPRef> xpRef) {
        return getTarget(xpRef)
                .request(ACCEPT)
                .get(XP.class);
    }

    public XPTransactResponse transact(final LoadingToken<XPRef> xpRef, long delta) {
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
    protected WebTarget getTarget(LoadingToken<XPRef> key) {
        return super.getTarget(key)
                .path(key.toString());
    }
}
