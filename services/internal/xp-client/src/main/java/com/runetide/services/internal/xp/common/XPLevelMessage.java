package com.runetide.services.internal.xp.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.runetide.common.services.topics.TopicMessage;

public class XPLevelMessage extends TopicMessage {
    @JsonProperty("xp")
    private XPRef xp;
    @JsonProperty("old")
    private int oldLevel;
    @JsonProperty("new")
    private int newLevel;

    public XPLevelMessage() {
    }

    public XPLevelMessage(XPRef xp, int oldLevel, int newLevel) {
        this.xp = xp;
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
    }

    public XPRef getXp() {
        return xp;
    }

    public void setXp(XPRef xp) {
        this.xp = xp;
    }

    public int getOldLevel() {
        return oldLevel;
    }

    public void setOldLevel(int oldLevel) {
        this.oldLevel = oldLevel;
    }

    public int getNewLevel() {
        return newLevel;
    }

    public void setNewLevel(int newLevel) {
        this.newLevel = newLevel;
    }
}
