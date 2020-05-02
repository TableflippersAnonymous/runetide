package com.runetide.services.internal.resourcepool.client;

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
import com.runetide.services.internal.resourcepool.common.ResourcePoolRef;
import com.runetide.services.internal.resourcepool.common.*;
import org.apache.curator.framework.CuratorFramework;
import org.redisson.api.RedissonClient;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.*;

@Singleton
public class ResourcePoolsClient extends UniqueLoadingClient<ResourcePoolRef> {
    @Inject
    public ResourcePoolsClient(ServiceRegistry serviceRegistry, TopicManager topicManager,
                               CuratorFramework curatorFramework, RedissonClient redissonClient) {
        super(serviceRegistry, topicManager, Constants.RESOURCE_POOL_LOADING_NAMESPACE, "resource-pools",
                curatorFramework, redissonClient);
    }

    public static List<TypeCodec<?>> getCqlTypeCodecs() {
        return Arrays.asList(
                new EnumIndexedCodec<>(ResourceType.class),
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

    public ResourcePool get(LoadingToken<ResourcePoolRef> resourcePoolRef) {
        return getTarget(resourcePoolRef)
                .request(ACCEPT)
                .get(ResourcePool.class);
    }

    public ResourcePoolTransactResponse transact(LoadingToken<ResourcePoolRef> resourcePoolRef, long delta,
                                                 Optional<Long> overrideMin, Optional<Long> overrideMax,
                                                 boolean ignoreNormalLimits, boolean takePartial) {
        return getTarget(resourcePoolRef)
                .request(ACCEPT)
                .post(Entity.entity(new ResourcePoolTransactRequest(delta,
                        overrideMin.orElse(null), overrideMax.orElse(null),
                        ignoreNormalLimits, takePartial), MediaType.APPLICATION_JSON), ResourcePoolTransactResponse.class);
    }

    public ResourcePool update(LoadingToken<ResourcePoolRef> resourcePoolRef, long value,
                               long normalLimitLower, long normalLimitUpper,
                               long finalLimitLower, long finalLimitUpper,
                               Optional<Map<String, ResourcePoolEffect>> effects) {
        return getTarget(resourcePoolRef)
                .request(ACCEPT)
                .put(Entity.entity(new ResourcePool(null, null, value, normalLimitUpper, normalLimitLower,
                        finalLimitUpper, finalLimitLower, effects.orElse(null)), MediaType.APPLICATION_JSON),
                        ResourcePool.class);
    }

    public void delete(LoadingToken<ResourcePoolRef> resourcePoolRef) {
        getTarget(resourcePoolRef)
                .request(ACCEPT)
                .delete();
    }

    public ResourcePool addEffect(LoadingToken<ResourcePoolRef> resourcePoolRef, String effectName,
                                  ResourcePoolEffect effect) {
        return getTarget(resourcePoolRef)
                .path(effectName)
                .request(ACCEPT)
                .put(Entity.entity(effect, MediaType.APPLICATION_JSON), ResourcePool.class);
    }

    public ResourcePoolEffect getEffect(LoadingToken<ResourcePoolRef> resourcePoolRef, String effectName) {
        return getTarget(resourcePoolRef)
                .path(effectName)
                .request(ACCEPT)
                .get(ResourcePoolEffect.class);
    }

    public void deleteEffect(LoadingToken<ResourcePoolRef> resourcePoolRef, String effectName) {
        getTarget(resourcePoolRef)
                .path(effectName)
                .request(ACCEPT)
                .delete();
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
    protected WebTarget getTarget(LoadingToken<ResourcePoolRef> key) {
        return super.getTarget(key)
                .path(key.toString());
    }
}
