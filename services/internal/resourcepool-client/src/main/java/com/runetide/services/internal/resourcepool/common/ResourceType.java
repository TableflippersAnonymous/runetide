package com.runetide.services.internal.resourcepool.common;

import org.codehaus.jackson.annotate.JsonValue;

public enum ResourceType {
    HEALTH, MANA, STAMINA, HUNGER, THIRST, BREATH, PAIN, EXHAUSTION;

    @JsonValue
    public int toValue() {
        return ordinal();
    }
}
