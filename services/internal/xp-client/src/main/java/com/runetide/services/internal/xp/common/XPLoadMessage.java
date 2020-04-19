package com.runetide.services.internal.xp.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.runetide.common.TopicMessage;

public class XPLoadMessage extends TopicMessage {
    @JsonProperty
    private XPRef xp;

    public XPLoadMessage() {
    }

    public XPLoadMessage(XPRef xp) {
        this.xp = xp;
    }

    public XPRef getXp() {
        return xp;
    }

    public void setXp(XPRef xp) {
        this.xp = xp;
    }
}
