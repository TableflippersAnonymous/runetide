package com.runetide.common;

import org.apache.curator.framework.CuratorFramework;

import javax.ws.rs.client.WebTarget;

public abstract class UniqueLoadingClient<K> extends StatelessClient {
    private final String objectName;
    private final String basePath;
    private final CuratorFramework curatorFramework;

    protected UniqueLoadingClient(final ServiceRegistry serviceRegistry, final TopicManager topicManager,
                                  final String objectName, final String basePath,
                                  final CuratorFramework curatorFramework) {
        super(serviceRegistry, topicManager, objectName, basePath);
        this.objectName = objectName;
        this.basePath = basePath;
        this.curatorFramework = curatorFramework;
    }

    protected WebTarget getTarget(final K key) {
        return client.target(serviceRegistry.getRandomUri(objectName + ":" + key)).path(basePath);
    }

    public boolean isLoaded(final K key) {
        try {
            return serviceRegistry.getFirst(objectName + ":" + key) != null;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public LoadingToken requestLoad(final K key) {
        final LoadingToken loadingToken = new LoadingToken(curatorFramework, objectName, key.toString());
        loadingToken.start();
        return loadingToken;
    }
}
