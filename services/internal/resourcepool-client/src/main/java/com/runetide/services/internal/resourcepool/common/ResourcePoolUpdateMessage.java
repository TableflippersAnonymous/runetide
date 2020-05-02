package com.runetide.services.internal.resourcepool.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.runetide.common.services.topics.TopicMessage;

public class ResourcePoolUpdateMessage extends TopicMessage {
    @JsonProperty
    private ResourcePool resourcePool;

    public ResourcePoolUpdateMessage() {
    }

    public ResourcePoolUpdateMessage(ResourcePool resourcePool) {
        this.resourcePool = resourcePool;
    }

    public ResourcePool getResourcePool() {
        return resourcePool;
    }

    public void setResourcePool(ResourcePool resourcePool) {
        this.resourcePool = resourcePool;
    }
}
