package com.runetide.services.internal.resourcepool.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.runetide.common.TopicMessage;
import com.runetide.common.dto.ResourcePoolRef;

public class ResourcePoolLoadMessage extends TopicMessage {
    @JsonProperty
    private ResourcePoolRef resourcePool;

    public ResourcePoolLoadMessage() {
    }

    public ResourcePoolLoadMessage(ResourcePoolRef resourcePool) {
        this.resourcePool = resourcePool;
    }

    public ResourcePoolRef getResourcePool() {
        return resourcePool;
    }

    public void setResourcePool(ResourcePoolRef resourcePool) {
        this.resourcePool = resourcePool;
    }
}
