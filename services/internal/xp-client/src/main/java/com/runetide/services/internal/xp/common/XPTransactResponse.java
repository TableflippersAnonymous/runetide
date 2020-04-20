package com.runetide.services.internal.xp.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class XPTransactResponse {
    @JsonProperty("!")
    private boolean success;
    @JsonProperty("$")
    private XPRef xpRef;
    @JsonProperty("v")
    private long oldValue;
    @JsonProperty("V")
    private long newValue;
    @JsonProperty("l")
    private int oldLevel;
    @JsonProperty("L")
    private int newLevel;

    public XPTransactResponse() {
    }

    public XPTransactResponse(boolean success, XPRef xpRef, long oldValue, long newValue, int oldLevel, int newLevel) {
        this.success = success;
        this.xpRef = xpRef;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public XPRef getXpRef() {
        return xpRef;
    }

    public void setXpRef(XPRef xpRef) {
        this.xpRef = xpRef;
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
