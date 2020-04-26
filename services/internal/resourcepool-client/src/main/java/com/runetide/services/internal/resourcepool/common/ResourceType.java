package com.runetide.services.internal.resourcepool.common;

import com.fasterxml.jackson.annotation.JsonValue;
import com.runetide.common.domain.IndexedEnum;

public enum ResourceType implements IndexedEnum {
    HEALTH(0), MANA(1), STAMINA(2), HUNGER(3), THIRST(4), BREATH(5), PAIN(6),
    EXHAUSTION(7), XP(8);

    private final int id;

    ResourceType(int id) {
        this.id = id;
    }

    @Override
    @JsonValue
    public int toValue() {
        return id;
    }
}
