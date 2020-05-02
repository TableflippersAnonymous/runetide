package com.runetide.services.internal.xp.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.runetide.common.services.topics.TopicMessage;

public class XPTransactMessage extends TopicMessage {
    @JsonProperty("xp")
    private XPRef xp;
    @JsonProperty("old")
    private long oldValue;
    @JsonProperty("new")
    private long newValue;

    public XPTransactMessage() {
    }

    public XPTransactMessage(XPRef xp, long oldValue, long newValue) {
        this.xp = xp;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public XPRef getXp() {
        return xp;
    }

    public void setXp(XPRef xp) {
        this.xp = xp;
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
