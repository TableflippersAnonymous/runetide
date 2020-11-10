package com.runetide.services.internal.world.common;

import com.fasterxml.jackson.annotation.JsonValue;
import com.runetide.common.domain.IndexedEnum;

public enum WorldType implements IndexedEnum {
    PRIME(0),

    UNDERWORLD(1),
    THE_DEEP(2),
    DUNGEON(3),
    THE_MAZE(4),

    ELEMENTAL_WATER(5),
    ELEMENTAL_FIRE(6),
    ELEMENTAL_EARTH(7),
    ELEMENTAL_AIR(8),

    ETERNAL_CITY_OXONOTH(9),
    RADIANT_ISLANDS_RAIX(10),
    MOUNTAINS_RUNOS(11),
    TWISTED_CAVES_PISOS(12),
    TWIN_EMPIRES_XEX(13),
    SHADOW_REALMS_TURIZ(14),
    BEACHES_MEZAL(15),
    MECHANICAL_CITY_BRONZE(16),
    MAGICAL_FOREST_FYAVAANYL(17),

    HELL_ACHERON(18),
    HELL_HADES(19),
    HELL_GEHENNA(20),
    HELL_TARTARUS(21),
    HELL_AVERNUS(22),

    ABYSS_VOID(23),
    ABYSS_EREBUS(24),
    ABYSS_MOORSHEIM(25),
    ABYSS_SHEOL(26),
    ABYSS_ABADDON(27)
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
