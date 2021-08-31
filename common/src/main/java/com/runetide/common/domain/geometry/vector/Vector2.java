package com.runetide.common.domain.geometry.vector;

import org.jetbrains.annotations.Contract;

public interface Vector2<Self extends Vector2<Self, NumberType>, NumberType extends Number>
        extends Vector<Self, NumberType> {
    static Vector2L of(final int x, final int z) {
        return Vector2L.of(x, z);
    }

    static Vector2L of(final long x, final long z) {
        return Vector2L.of(x, z);
    }

    static Vector2F of(final float x, final float z) {
        return Vector2F.of(x, z);
    }

    static Vector2F of(final double x, final double z) {
        return Vector2F.of(x, z);
    }

    @Contract(pure = true)
    NumberType getX();
    @Contract(pure = true)
    NumberType getZ();
    @Override
    Vector2F toFloat();
    @Override
    Vector2L toFixed();

    @Override
    default Vector2F normalize() {
        final Vector2F fVec = toFloat();
        return fVec.divide(fVec.length());
    }
}