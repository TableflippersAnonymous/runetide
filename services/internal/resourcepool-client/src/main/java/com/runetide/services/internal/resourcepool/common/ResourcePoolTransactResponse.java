package com.runetide.services.internal.resourcepool.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResourcePoolTransactResponse {
    @JsonProperty("s")
    private boolean success;
    @JsonProperty("p")
    private ResourcePool resourcePool;

    public ResourcePoolTransactResponse() {
    }

    public ResourcePoolTransactResponse(boolean success, ResourcePool resourcePool) {
        this.success = success;
        this.resourcePool = resourcePool;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ResourcePool getResourcePool() {
        return resourcePool;
    }

    public void setResourcePool(ResourcePool resourcePool) {
        this.resourcePool = resourcePool;
    }
}
