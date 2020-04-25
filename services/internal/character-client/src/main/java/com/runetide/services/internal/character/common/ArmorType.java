package com.runetide.services.internal.character.common;

import com.fasterxml.jackson.annotation.JsonValue;
import com.runetide.common.domain.IndexedEnum;

public enum ArmorType implements IndexedEnum {
    NONE(0), LIGHT(1), MEDIUM(2), HEAVY(3);

    private final int id;

    ArmorType(int id) {
        this.id = id;
    }

    @Override
    @JsonValue
    public int toValue() {
        return id;
    }
}
