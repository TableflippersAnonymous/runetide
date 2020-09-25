package com.runetide.services.internal.world.common;

import com.fasterxml.jackson.annotation.JsonValue;
import com.runetide.common.domain.IndexedEnum;

public enum WorldType implements IndexedEnum {
    VOID(0),
    PRIME(1),
    UNDERWORLD(2),
    HELL(),
    ASTRAL(),

    ;

    private final int id;

    WorldType(final int id) {
        this.id = id;
    }

    @Override
    @JsonValue
    public int toValue() {
        return id;
    }
}
