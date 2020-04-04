package com.runetide.services.internal.resourcepool.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResourcePoolEffect {
    @JsonProperty("i")
    private long interval;
    @JsonProperty("r")
    private long repetitions;
    @JsonProperty("d")
    private long delta;
    @JsonProperty("o")
    private Long overrideMin;
    @JsonProperty("O")
    private Long overrideMax;
    @JsonProperty("f")
    private boolean ignoreNormalLimits;
    @JsonProperty("p")
    private boolean persistent;

    public ResourcePoolEffect() {
    }

    public ResourcePoolEffect(long interval, long repetitions, long delta, Long overrideMin, Long overrideMax,
                              boolean ignoreNormalLimits, boolean persistent) {
        this.interval = interval;
        this.repetitions = repetitions;
        this.delta = delta;
        this.overrideMin = overrideMin;
        this.overrideMax = overrideMax;
        this.ignoreNormalLimits = ignoreNormalLimits;
        this.persistent = persistent;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public long getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(long repetitions) {
        this.repetitions = repetitions;
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

    public boolean isPersistent() {
        return persistent;
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }
}
