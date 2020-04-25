package com.runetide.services.internal.character.common;

import com.fasterxml.jackson.annotation.JsonValue;
import com.runetide.common.domain.IndexedEnum;

public enum EquipmentSlot implements IndexedEnum {
    HEAD(0), CHEST(1), LEGS(2), FEET(3), HANDS(4), FINGER_0(5), FINGER_1(6), FINGER_2(7),
    FINGER_3(8), FINGER_4(9), FINGER_5(10), FINGER_6(11), FINGER_7(12), AMULET(13), TRINKET_0(14),
    TRINKET_1(15), NECK(16), WRIST(17), WAIST(18), SHOULDERS(19), BACKPACK(20), MAIN_HAND(21),
    OFF_HAND(22), HOTBAR_0(23), HOTBAR_1(24), HOTBAR_2(25), HOTBAR_3(26), HOTBAR_4(27),
    HOTBAR_5(28), HOTBAR_6(29), HOTBAR_7(30), HOTBAR_8(31), HOTBAR_9(32);

    private final int id;

    EquipmentSlot(int id) {
        this.id = id;
    }

    @Override
    @JsonValue
    public int toValue() {
        return id;
    }
}
