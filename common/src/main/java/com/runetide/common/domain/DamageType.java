package com.runetide.common.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DamageType implements IndexedEnum {
    NORMAL(0), PIERCING(1), SLASHING(2), BLUDGEONING(3), FIRE(4), COLD(5), NECROTIC(6),
    RADIANT(7), LIGHTNING(8), SONIC(9), NATURE(10), PSYCHIC(11), POISON(12), ACID(13),
    ARCANE(14), DARK(15);

    private final int id;

    DamageType(int id) {
        this.id = id;
    }

    @Override
    @JsonValue
    public int toValue() {
        return id;
    }
}
