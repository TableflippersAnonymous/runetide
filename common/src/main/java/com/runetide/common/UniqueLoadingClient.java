package com.runetide.common;

import javax.ws.rs.client.WebTarget;

public abstract class UniqueLoadingClient<K> extends StatelessClient {
    private final String objectName;
    private final String basePath;

    protected UniqueLoadingClient(final ServiceRegistry serviceRegistry, final TopicManager topicManager,
                                  final String objectName, final String basePath) {
        super(serviceRegistry, topicManager, objectName, basePath);
        this.objectName = objectName;
        this.basePath = basePath;
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
}
