package com.runetide.services.internal.character.common;

import com.fasterxml.jackson.annotation.JsonValue;
import com.runetide.common.domain.IndexedEnum;

public enum AbilityType implements IndexedEnum {
    POTION_BREWING(0), WARCRAFTING(1), DIVINE_MAGIC(2), DIVINE_RITUALS(3), LOCK_PICKING(4),
    GOLEMANCY(5), NATURE_MAGIC(6), ANIMAL_SHAPE(7), ARCANE_MAGIC(8), SORCEROUS_MAGIC(9),
    CASTING_MANIPULATION(10), FAMILIAR(11), ELDRITCH_MAGIC(12), PATRON_FEATURES(13), WARLOCK_SWORD(14),
    NECROMANCY(15), WIZARD_MAGIC(16), RUNE_CRAFTING(17), MUSIC_CRAFTING(18), TRAPS(19);

    private final int id;

    AbilityType(int id) {
        this.id = id;
    }

    @Override
    @JsonValue
    public int toValue() {
        return id;
    }
}
