package com.runetide.services.internal.xp.server.dto;

import com.datastax.oss.driver.api.mapper.annotations.*;
import com.runetide.services.internal.xp.common.XPRef;
import com.runetide.services.internal.xp.common.XPType;

import java.util.Optional;
import java.util.UUID;

@Entity
@CqlName("xp_by_parent")
public class XPByParent {
    @Transient // See getCqlParent/setCqlParent below
    private XPRef parent;
    @Transient // See getCqlId/setCqlId below
    private XPRef id;
    @Transient // See getCqlType/setCqlType below
    private XPType type;
    @CqlName("xp")
    private long xp;

    public XPByParent() {
    }

    public XPByParent(XPRef parent, XPRef id, XPType type, long xp) {
        this.parent = parent;
        this.id = id;
        this.type = type;
        this.xp = xp;
    }

    public XPRef getParent() {
        return parent;
    }

    public void setParent(XPRef parent) {
        this.parent = parent;
    }

    @PartitionKey
    @CqlName("parent")
    public UUID getCqlParent() {
        return parent.getId();
    }

    public void setCqlParent(UUID parent) {
        this.parent = Optional.ofNullable(parent).map(XPRef::new).orElse(null);
    }

    public XPRef getId() {
        return id;
    }

    public void setId(XPRef id) {
        this.id = id;
    }

    @ClusteringColumn
    @CqlName("id")
    public UUID getCqlId() {
        return id.getId();
    }

    public void setCqlId(UUID id) {
        this.id = new XPRef(id);
    }

    public XPType getType() {
        return type;
    }

    public void setType(XPType type) {
        this.type = type;
    }

    @CqlName("type")
    public int getCqlType() {
        return type.ordinal();
    }

    public void setCqlType(int type) {
        this.type = XPType.values()[type];
    }

    public long getXp() {
        return xp;
    }

    public void setXp(long xp) {
        this.xp = xp;
    }
}
