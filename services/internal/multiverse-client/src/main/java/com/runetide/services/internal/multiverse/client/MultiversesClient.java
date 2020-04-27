package com.runetide.services.internal.multiverse.client;

import com.datastax.oss.driver.api.core.type.codec.TypeCodec;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.runetide.common.Constants;
import com.runetide.common.ServiceRegistry;
import com.runetide.common.TopicManager;
import com.runetide.common.UniqueLoadingClient;
import com.runetide.common.services.cql.UUIDRefCodec;
import com.runetide.services.internal.multiverse.common.MultiverseRef;
import org.apache.curator.framework.CuratorFramework;
import org.redisson.api.RedissonClient;

import java.util.Arrays;
import java.util.List;

@Singleton
public class MultiversesClient extends UniqueLoadingClient<MultiverseRef> {
    @Inject
    public MultiversesClient(ServiceRegistry serviceRegistry, TopicManager topicManager,
                             CuratorFramework curatorFramework, RedissonClient redissonClient) {
        super(serviceRegistry, topicManager, Constants.MULTIVERSE_LOADING_NAMESPACE, "multiverses",
                curatorFramework, redissonClient);
    }

    public static List<TypeCodec<?>> getCqlTypeCodecs() {
        return Arrays.asList(
                new UUIDRefCodec<>(MultiverseRef.class, MultiverseRef::new)
        );
    }
}
