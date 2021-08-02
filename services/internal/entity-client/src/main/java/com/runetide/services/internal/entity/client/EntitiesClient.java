package com.runetide.services.internal.entity.client;

import com.datastax.oss.driver.api.core.type.codec.TypeCodec;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.runetide.common.Constants;
import com.runetide.common.services.servicediscovery.ServiceRegistry;
import com.runetide.common.services.topics.TopicManager;
import com.runetide.common.clients.UniqueLoadingClient;
import com.runetide.services.internal.entity.common.dto.EntityRef;
import org.apache.curator.framework.CuratorFramework;
import org.redisson.api.RedissonClient;

import java.util.Arrays;
import java.util.List;

@Singleton
public class EntitiesClient extends UniqueLoadingClient<EntityRef> {
    @Inject
    public EntitiesClient(ServiceRegistry serviceRegistry, TopicManager topicManager,
                          CuratorFramework curatorFramework, RedissonClient redissonClient) {
        super(serviceRegistry, topicManager, Constants.ENTITY_LOADING_NAMESPACE, "entities", curatorFramework,
                redissonClient);
    }

    public static List<TypeCodec<?>> getCqlTypeCodecs() {
        return Arrays.asList(EntityRef.CODEC);
    }
}
