package com.runetide.services.internal.region.common;

import com.fasterxml.jackson.annotation.JsonValue;

public enum BlockType {
    AIR;

    @JsonValue
    public int toValue() {
        return ordinal();
    }
}
