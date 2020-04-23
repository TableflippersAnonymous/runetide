package com.runetide.services.internal.xp.common;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.datastax.oss.driver.api.mapper.annotations.Transient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@CqlName("xp")
public class XP {
    @JsonProperty("$")
    @PartitionKey
    @CqlName("id")
    private XPRef id;
    @JsonProperty("p")
    @CqlName("parent")
    private XPRef parent;
    @JsonProperty("t")
    @CqlName("type")
    private XPType type;
    @JsonProperty("v")
    @CqlName("xp")
    private long xp;

    public XP() {
    }

    public XP(XPRef id, XPRef parent, XPType type, long xp) {
        this.id = id;
        this.parent = parent;
        this.type = type;
        this.xp = xp;
    }

    public XPRef getId() {
        return id;
    }

    public void setId(XPRef id) {
        this.id = id;
    }

    public XPRef getParent() {
        return parent;
    }

    public void setParent(XPRef parent) {
        this.parent = parent;
    }

    public XPType getType() {
        return type;
    }

    public void setType(XPType type) {
        this.type = type;
    }

    public long getXp() {
        return xp;
    }

    public void setXp(long xp) {
        this.xp = Math.max(0,
                Math.min(xp,
                        type.getXpByLevel(type.getLevelCap())));
    }

    @JsonIgnore
    @Transient
    public int getLevel() {
        return type.getLevelByXp(xp);
    }

    @JsonIgnore
    @Transient
    public long getResidualXp() {
        return xp - type.getXpByLevel(getLevel());
    }

    @JsonIgnore
    @Transient
    public long getXpToNextLevel() {
        return type.getXpByLevel(getLevel() + 1) - xp;
    }
}
