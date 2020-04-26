package com.runetide.common.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EquipmentType implements IndexedEnum {
    HEAD(0), CHEST(1), LEGS(2), FEET(3), HANDS(4), FINGER(5), AMULET(6), TRINKET(7), NECK(8),
    WRIST(9), WAIST(10), SHOULDERS(11), BACKPACK(12), MAIN_HAND(13);

    private final int id;

    EquipmentType(int id) {
        this.id = id;
    }

    @Override
    @JsonValue
    public int toValue() {
        return id;
    }
}
