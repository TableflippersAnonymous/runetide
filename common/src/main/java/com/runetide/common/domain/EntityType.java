package com.runetide.common.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EntityType implements IndexedEnum {
    ITEM(0), CHARACTER(1), CREATURE(2), FALLING_BLOCK(3), PROJECTILE(4), EFFECT(5), WEATHER(6),
    VEHICLE(7);

    private final int id;

    EntityType(int id) {
        this.id = id;
    }

    @Override
    @JsonValue
    public int toValue() {
        return id;
    }
}
