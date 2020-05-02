package com.runetide.common.clients;

import com.runetide.common.*;
import com.runetide.common.loading.ServiceState;
import com.runetide.common.loading.StateTransitionMessage;
import com.runetide.common.services.servicediscovery.ServiceRegistry;
import com.runetide.common.services.topics.TopicListener;
import com.runetide.common.services.topics.TopicListenerHandle;
import com.runetide.common.services.topics.TopicManager;
import org.apache.curator.framework.CuratorFramework;
import org.redisson.api.RedissonClient;

import javax.ws.rs.client.WebTarget;

public abstract class UniqueLoadingClient<K> extends StatelessClient {
    private final String objectName;
    private final String basePath;
    private final CuratorFramework curatorFramework;
    private final RedissonClient redissonClient;

    protected UniqueLoadingClient(final ServiceRegistry serviceRegistry, final TopicManager topicManager,
                                  final String objectName, final String basePath,
                                  final CuratorFramework curatorFramework, final RedissonClient redissonClient) {
        super(serviceRegistry, topicManager, objectName, basePath);
        this.objectName = objectName;
        this.basePath = basePath;
        this.curatorFramework = curatorFramework;
        this.redissonClient = redissonClient;
    }

    protected WebTarget getTarget(final LoadingToken<K> key) {
        key.awaitServiceState(ServiceState.LOADED);
        return client.target(serviceRegistry.getRandomUri(objectName + ":" + key.getKey())).path(basePath);
    }

    public boolean isLoaded(final K key) {
        try {
            return serviceRegistry.getFirst(objectName + ":" + key) != null;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public LoadingToken<K> requestLoad(final K key) {
        final LoadingToken<K> loadingToken = new LoadingToken<>(curatorFramework, objectName, key,
                this, redissonClient);
        loadingToken.start();
        return loadingToken;
    }

    public TopicListenerHandle<StateTransitionMessage> listenStateTransition(final K key,
                                                                             final TopicListener<StateTransitionMessage> topicListener) {
        return topicManager.addListener(Constants.LOADING_TOPIC_PREFIX + objectName + ":" + key,
                topicListener, StateTransitionMessage.class);
    }
}
