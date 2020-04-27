package com.runetide.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StateTransitionMessage extends TopicMessage {
    @JsonProperty("_")
    private String objectName;
    @JsonProperty("$")
    private String key;
    @JsonProperty("o")
    private ServiceState oldState;
    @JsonProperty("n")
    private ServiceState newState;

    public StateTransitionMessage() {
    }

    public StateTransitionMessage(String objectName, String key, ServiceState oldState, ServiceState state) {
        this.objectName = objectName;
        this.key = key;
        this.oldState = oldState;
        this.newState = state;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ServiceState getOldState() {
        return oldState;
    }

    public void setOldState(ServiceState oldState) {
        this.oldState = oldState;
    }

    public ServiceState getNewState() {
        return newState;
    }

    public void setNewState(ServiceState newState) {
        this.newState = newState;
    }
}
