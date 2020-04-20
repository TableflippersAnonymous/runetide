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
    @Transient // See getCqlId/setCqlId below
    private XPRef id;
    @JsonProperty("p")
    @Transient // See getCqlParent/setCqlParent below
    private XPRef parent;
    @JsonProperty("t")
    @Transient // See getCqlType/setCqlType below
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

    @PartitionKey
    @CqlName("id")
    @JsonIgnore
    public UUID getCqlId() {
        return id.getId();
    }

    @JsonIgnore
    public void setCqlId(UUID id) {
        this.id = new XPRef(id);
    }

    public XPRef getParent() {
        return parent;
    }

    public void setParent(XPRef parent) {
        this.parent = parent;
    }

    @CqlName("parent")
    @JsonIgnore
    public UUID getCqlParent() {
        return parent.getId();
    }

    @JsonIgnore
    public void setCqlParent(UUID parent) {
        this.parent = Optional.ofNullable(parent).map(XPRef::new).orElse(null);
    }

    public XPType getType() {
        return type;
    }

    public void setType(XPType type) {
        this.type = type;
    }

    @CqlName("type")
    @JsonIgnore
    public int getCqlType() {
        return type.ordinal();
    }

    @JsonIgnore
    public void setCqlType(int type) {
        this.type = XPType.values()[type];
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
