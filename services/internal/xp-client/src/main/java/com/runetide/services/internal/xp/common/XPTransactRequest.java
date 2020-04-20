package com.runetide.services.internal.xp.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class XPTransactRequest {
    @JsonProperty("d")
    private long delta;

    public XPTransactRequest() {
    }

    public XPTransactRequest(long delta) {
        this.delta = delta;
    }

    public long getDelta() {
        return delta;
    }

    public void setDelta(long delta) {
        this.delta = delta;
    }
}
