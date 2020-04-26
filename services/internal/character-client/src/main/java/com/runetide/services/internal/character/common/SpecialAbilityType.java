package com.runetide.services.internal.character.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.runetide.common.domain.IndexedEnum;

public enum SpecialAbilityType implements IndexedEnum {
    ALERT(0), ATHLETE(1), LUCKY(2), NATURAL_MINER(3), NIGHT_VISION(4), MAGIC_SIGHT(5);

    private final int id;

    SpecialAbilityType(int id) {
        this.id = id;
    }

    @Override
    @JsonProperty
    public int toValue() {
        return id;
    }
}
