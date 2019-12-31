package com.runetide.services.internal.time.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.runetide.common.TopicMessage;

public class TimeTickMessage extends TopicMessage {
    @JsonProperty
    private long currentTick;

    public TimeTickMessage() {
    }

    public TimeTickMessage(long currentTick) {
        this.currentTick = currentTick;
    }

    public long getCurrentTick() {
        return currentTick;
    }

    public void setCurrentTick(long currentTick) {
        this.currentTick = currentTick;
    }
}
