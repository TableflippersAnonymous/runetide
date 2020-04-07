package com.runetide.services.internal.resourcepool.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResourcePoolTransactRequest {
    @JsonProperty("d")
    private long delta;
    @JsonProperty("o")
    private Long overrideMin;
    @JsonProperty("O")
    private Long overrideMax;
    @JsonProperty("f")
    private boolean ignoreNormalLimits;
    @JsonProperty("P")
    private boolean takePartial;

    public ResourcePoolTransactRequest() {
    }

    public ResourcePoolTransactRequest(long delta, Long overrideMin, Long overrideMax, boolean ignoreNormalLimits,
                                       boolean takePartial) {
        this.delta = delta;
        this.overrideMin = overrideMin;
        this.overrideMax = overrideMax;
        this.ignoreNormalLimits = ignoreNormalLimits;
        this.takePartial = takePartial;
    }

    public long getDelta() {
        return delta;
    }

    public void setDelta(long delta) {
        this.delta = delta;
    }

    public Long getOverrideMin() {
        return overrideMin;
    }

    public void setOverrideMin(Long overrideMin) {
        this.overrideMin = overrideMin;
    }

    public Long getOverrideMax() {
        return overrideMax;
    }

    public void setOverrideMax(Long overrideMax) {
        this.overrideMax = overrideMax;
    }

    public boolean isIgnoreNormalLimits() {
        return ignoreNormalLimits;
    }

    public void setIgnoreNormalLimits(boolean ignoreNormalLimits) {
        this.ignoreNormalLimits = ignoreNormalLimits;
    }

    public boolean isTakePartial() {
        return takePartial;
    }

    public void setTakePartial(boolean takePartial) {
        this.takePartial = takePartial;
    }
}
