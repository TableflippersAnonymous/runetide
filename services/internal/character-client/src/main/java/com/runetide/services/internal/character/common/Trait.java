package com.runetide.services.internal.character.common;

import com.fasterxml.jackson.annotation.JsonValue;
import com.runetide.common.domain.IndexedEnum;

public enum Trait implements IndexedEnum {
    MAGIC_SIGHT(0), NATURAL_DIPLOMAT(1), NATURAL_MINER(2), COLD_RESIST(3), LUCKY(4),
    NATURAL_CARTOGRAPHER(5), EASILY_SATED(6), BREATH_BOOST(7), NIGHT_VISION(8), LIGHT_SENSITIVITY(9),
    MONSTROUS_HIDING(10), LOOTING(11), HEAT_RESIST(12), GREATER_OMNIVORE(13), UNBREATHING(14),
    UNDRINKING(15), UNEATING(16), WATER_VULNERABILITY(17), FEATHER_FALLING(18), NATURAL_REGEN(19);

    private final int id;

    Trait(int id) {
        this.id = id;
    }

    @Override
    @JsonValue
    public int toValue() {
        return id;
    }
}
