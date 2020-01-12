package com.runetide.common;

import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public abstract class StatelessClient {
    protected static final String ACCEPT = MediaType.APPLICATION_JSON;

    protected final ServiceRegistry serviceRegistry;
    protected final TopicManager topicManager;
    protected final Client client;
    private final String objectName;
    private final String basePath;

    protected StatelessClient(final ServiceRegistry serviceRegistry, final TopicManager topicManager,
                              final String objectName, final String basePath) {
        this.serviceRegistry = serviceRegistry;
        this.topicManager = topicManager;
        this.objectName = objectName;
        this.basePath = basePath;
        client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);
    }

    protected WebTarget getTarget() {
        return client.target(serviceRegistry.getRandomUri(objectName)).path(basePath);
    }
}
