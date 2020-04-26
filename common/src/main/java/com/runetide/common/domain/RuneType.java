package com.runetide.common.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RuneType implements IndexedEnum {
    CAST(0), DEATH(1), PERSIST(2), PROXIMITY(3), DAMAGE(4),

    COMBINING(5),

    SELF(6), TOUCH(7), RANGE(8), BOUND(9),

    DISTANCE(10),

    LINE(11), CONE(12), AREA(13),

    SPHERE(14), COLUMN(15), CUBE(16),

    CLOSE(17), FAR(18), CENTER(19),

    EFFECT(20), HEALING(21), DIG(22), TELEPORT(23), BIND(24),

    DURATION(25),

    EYES(26), LANGUAGE(27), JUMPING(28), SPEED(29), SLOW(30), CHARM(31), LIGHT(32),

    FIRE(33), COLD(34), NECROTIC(35), RADIANT(36), LIGHTNING(37), SONIC(38), NATURE(39),
    PSYCHIC(40), POISON(41), ACID(42), ARCANE(43), DARK(44),

    POWER(45);

    private final int id;

    RuneType(int id) {
        this.id = id;
    }

    @Override
    @JsonValue
    public int toValue() {
        return id;
    }
}
