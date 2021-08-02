package com.runetide.services.internal.xp.server.dto;

import com.datastax.oss.driver.api.mapper.annotations.*;
import com.runetide.services.internal.xp.common.XPRef;
import com.runetide.services.internal.xp.common.XPType;

@Entity
@CqlName("xp_by_parent")
public class XPByParent {
    @PartitionKey
    @CqlName("parent")
    private XPRef parent;
    @ClusteringColumn
    @CqlName("id")
    private XPRef id;
    @CqlName("type")
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

    public XPRef getId() {
        return id;
    }

    public void setId(XPRef id) {
        this.id = id;
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
        this.xp = xp;
    }
}
