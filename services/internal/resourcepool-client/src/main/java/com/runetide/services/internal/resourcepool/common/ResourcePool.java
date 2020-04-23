package com.runetide.services.internal.resourcepool.common;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.datastax.oss.driver.api.mapper.annotations.Transient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.UUID;

/**
 * A ResourcePool holds the value of an RPG Resource Pool.
 *
 * A ResourcePool has a Normal Limit which is the maximum that
 * any normal effect may have on the resource pool.
 *
 * A ResourcePool also has a Final Limit where changes outside
 * of this value will be declined and clamped to the final limit.
 *
 * A ResourcePool also has a list of active effects.
 *
 * Consider Health, for example, for a character.  The value would
 * be the current health of the character.  The lower normal and final
 * limit would both be 0 (a character cannot have less than 0 HP).  The
 * upper normal limit would be the character's max HP.  This is because
 * healing, natural regeneration, magical regeneration, and such should
 * never regenerate over the character's max HP.  However, the final
 * upper limit would be undefined as things that grant temporary HP
 * could bump the health over the max temporarily, but once lost, those
 * temporary HP should not regenerate.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@CqlName("resource_pool")
public class ResourcePool {
    @JsonProperty("$")
    @PartitionKey
    @CqlName("id")
    private ResourcePoolRef id;
    @JsonProperty("t")
    @CqlName("type")
    private ResourceType type;
    @JsonProperty("v")
    private long value;
    @JsonProperty("N")
    @CqlName("normal_limit_upper")
    private long normalLimitUpper;
    @JsonProperty("n")
    @CqlName("normal_limit_lower")
    private long normalLimitLower;
    @JsonProperty("F")
    @CqlName("final_limit_upper")
    private long finalLimitUpper;
    @JsonProperty("f")
    @CqlName("final_limit_lower")
    private long finalLimitLower;
    @JsonProperty("e")
    private Map<String, ResourcePoolEffect> effects;

    public ResourcePool() {
    }

    public ResourcePool(ResourcePoolRef id, ResourceType type, long value, long normalLimitUpper, long normalLimitLower,
                        long finalLimitUpper, long finalLimitLower, Map<String, ResourcePoolEffect> effects) {
        this.id = id;
        this.type = type;
        this.value = value;
        this.normalLimitUpper = normalLimitUpper;
        this.normalLimitLower = normalLimitLower;
        this.finalLimitUpper = finalLimitUpper;
        this.finalLimitLower = finalLimitLower;
        this.effects = effects;
    }

    public ResourcePoolRef getId() {
        return id;
    }

    public void setId(ResourcePoolRef id) {
        this.id = id;
    }

    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public long getNormalLimitUpper() {
        return normalLimitUpper;
    }

    public void setNormalLimitUpper(long normalLimitUpper) {
        this.normalLimitUpper = normalLimitUpper;
    }

    public long getNormalLimitLower() {
        return normalLimitLower;
    }

    public void setNormalLimitLower(long normalLimitLower) {
        this.normalLimitLower = normalLimitLower;
    }

    public long getFinalLimitUpper() {
        return finalLimitUpper;
    }

    public void setFinalLimitUpper(long finalLimitUpper) {
        this.finalLimitUpper = finalLimitUpper;
    }

    public long getFinalLimitLower() {
        return finalLimitLower;
    }

    public void setFinalLimitLower(long finalLimitLower) {
        this.finalLimitLower = finalLimitLower;
    }

    public Map<String, ResourcePoolEffect> getEffects() {
        return effects;
    }

    public void setEffects(Map<String, ResourcePoolEffect> effects) {
        this.effects = effects;
    }
}
