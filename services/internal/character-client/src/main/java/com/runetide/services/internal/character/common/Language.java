package com.runetide.services.internal.character.common;

import com.fasterxml.jackson.annotation.JsonValue;
import com.runetide.common.domain.IndexedEnum;

public enum Language implements IndexedEnum {
    COMMON(0), ELVISH(1), DWARVISH(2), GNOMISH(3), DRACONIC(4),
    GOBLINOID(5), ORCISH(6), FELINE(7);

    private final int id;

    Language(int id) {
        this.id = id;
    }


    @Override
    @JsonValue
    public int toValue() {
        return id;
    }
}
