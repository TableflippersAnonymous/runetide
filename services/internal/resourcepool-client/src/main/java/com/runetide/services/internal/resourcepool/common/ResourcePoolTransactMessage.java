package com.runetide.services.internal.resourcepool.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.runetide.common.services.topics.TopicMessage;

public class ResourcePoolTransactMessage extends TopicMessage {
    @JsonProperty
    private ResourcePoolRef resourcePool;
    @JsonProperty
    private long oldValue;
    @JsonProperty
    private long newValue;

    public ResourcePoolTransactMessage() {
    }

    public ResourcePoolTransactMessage(ResourcePoolRef resourcePool, long oldValue, long newValue) {
        this.resourcePool = resourcePool;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public ResourcePoolRef getResourcePool() {
        return resourcePool;
    }

    public void setResourcePool(ResourcePoolRef resourcePool) {
        this.resourcePool = resourcePool;
    }

    public long getOldValue() {
        return oldValue;
    }

    public void setOldValue(long oldValue) {
        this.oldValue = oldValue;
    }

    public long getNewValue() {
        return newValue;
    }

    public void setNewValue(long newValue) {
        this.newValue = newValue;
    }
}
