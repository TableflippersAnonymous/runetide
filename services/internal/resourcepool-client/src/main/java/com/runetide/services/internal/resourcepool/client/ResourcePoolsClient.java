package com.runetide.services.internal.resourcepool.client;

import com.datastax.oss.driver.api.core.type.codec.TypeCodec;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.runetide.common.*;
import com.runetide.common.services.cql.EnumOrdinalCodec;
import com.runetide.common.services.cql.UUIDRefCodec;
import com.runetide.services.internal.resourcepool.common.ResourcePoolRef;
import com.runetide.services.internal.resourcepool.common.*;
import org.apache.curator.framework.CuratorFramework;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.*;

@Singleton
public class ResourcePoolsClient extends UniqueLoadingClient<ResourcePoolRef> {
    @Inject
    public ResourcePoolsClient(ServiceRegistry serviceRegistry, TopicManager topicManager,
                               CuratorFramework curatorFramework) {
        super(serviceRegistry, topicManager, Constants.RESOURCE_POOL_LOADING_NAMESPACE, "resource-pools",
                curatorFramework);
    }

    public static List<TypeCodec<?>> getCqlTypeCodecs() {
        return Arrays.asList(
                new EnumOrdinalCodec<>(ResourceType.class),
                new UUIDRefCodec<>(ResourcePoolRef.class, ResourcePoolRef::new)
        );
    }

    public ResourcePool create(ResourceType resourceType, long value,
                               long normalLimitLower, long normalLimitUpper,
                               long finalLimitLower, long finalLimitUpper,
                               Map<String, ResourcePoolEffect> effects) {
        return getTarget()
                .request(ACCEPT)
                .post(Entity.entity(new ResourcePool(null, resourceType, value, normalLimitUpper, normalLimitLower,
                        finalLimitUpper, finalLimitLower, effects), MediaType.APPLICATION_JSON), ResourcePool.class);
    }

    public ResourcePool get(ResourcePoolRef resourcePoolRef) {
        return getTarget(resourcePoolRef)
                .request(ACCEPT)
                .get(ResourcePool.class);
    }

    public ResourcePoolTransactResponse transact(ResourcePoolRef resourcePoolRef, long delta,
                                                 Optional<Long> overrideMin, Optional<Long> overrideMax,
                                                 boolean ignoreNormalLimits, boolean takePartial) {
        return getTarget(resourcePoolRef)
                .request(ACCEPT)
                .post(Entity.entity(new ResourcePoolTransactRequest(delta,
                        overrideMin.orElse(null), overrideMax.orElse(null),
                        ignoreNormalLimits, takePartial), MediaType.APPLICATION_JSON), ResourcePoolTransactResponse.class);
    }

    public ResourcePool update(ResourcePoolRef resourcePoolRef, long value,
                               long normalLimitLower, long normalLimitUpper,
                               long finalLimitLower, long finalLimitUpper,
                               Optional<Map<String, ResourcePoolEffect>> effects) {
        return getTarget(resourcePoolRef)
                .request(ACCEPT)
                .put(Entity.entity(new ResourcePool(null, null, value, normalLimitUpper, normalLimitLower,
                        finalLimitUpper, finalLimitLower, effects.orElse(null)), MediaType.APPLICATION_JSON),
                        ResourcePool.class);
    }

    public void delete(ResourcePoolRef resourcePoolRef) {
        getTarget(resourcePoolRef)
                .request(ACCEPT)
                .delete();
    }

    public ResourcePool addEffect(ResourcePoolRef resourcePoolRef, String effectName, ResourcePoolEffect effect) {
        return getTarget(resourcePoolRef)
                .path(effectName)
                .request(ACCEPT)
                .put(Entity.entity(effect, MediaType.APPLICATION_JSON), ResourcePool.class);
    }

    public ResourcePoolEffect getEffect(ResourcePoolRef resourcePoolRef, String effectName) {
        return getTarget(resourcePoolRef)
                .path(effectName)
                .request(ACCEPT)
                .get(ResourcePoolEffect.class);
    }

    public void deleteEffect(ResourcePoolRef resourcePoolRef, String effectName) {
        getTarget(resourcePoolRef)
                .path(effectName)
                .request(ACCEPT)
                .delete();
    }

    public TopicListenerHandle<ResourcePoolLoadMessage> listenLoad(ResourcePoolRef resourcePoolRef,
                                                                   TopicListener<ResourcePoolLoadMessage> listener) {
        return topicManager.addListener(Constants.RESOURCE_POOL_TOPIC_PREFIX + resourcePoolRef + ":load",
                listener, ResourcePoolLoadMessage.class);
    }

    public TopicListenerHandle<ResourcePoolTransactMessage> listenTransact(ResourcePoolRef resourcePoolRef,
                                                                           TopicListener<ResourcePoolTransactMessage> listener) {
        return topicManager.addListener(Constants.RESOURCE_POOL_TOPIC_PREFIX + resourcePoolRef + ":transact",
                listener, ResourcePoolTransactMessage.class);
    }

    public TopicListenerHandle<ResourcePoolUpdateMessage> listenUpdate(ResourcePoolRef resourcePoolRef,
                                                                       TopicListener<ResourcePoolUpdateMessage> listener) {
        return topicManager.addListener(Constants.RESOURCE_POOL_TOPIC_PREFIX + resourcePoolRef + ":update",
                listener, ResourcePoolUpdateMessage.class);
    }

    @Override
    protected WebTarget getTarget(ResourcePoolRef key) {
        return super.getTarget(key)
                .path(key.toString());
    }
}
