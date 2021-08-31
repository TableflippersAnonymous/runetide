package com.runetide.common.domain.geometry.vector;

import org.jetbrains.annotations.Contract;

public interface Vector3<Self extends Vector3<Self, NumberType>, NumberType extends Number>
        extends Vector<Self, NumberType> {
    static Vector3L of(final int x, final int y, final int z) {
        return Vector3L.of(x, y, z);
    }

    static Vector3L of(final long x, final long y, final long z) {
        return Vector3L.of(x, y, z);
    }

    static Vector3F of(final float x, final float y, final float z) {
        return Vector3F.of(x, y, z);
    }

    static Vector3F of(final double x, final double y, final double z) {
        return Vector3F.of(x, y, z);
    }

    @Contract(pure = true)
    NumberType getX();
    @Contract(pure = true)
    NumberType getY();
    @Contract(pure = true)
    NumberType getZ();

    @Contract(pure = true)
    Self cross(final Self other);

    @Override
    Vector3F toFloat();
    @Override
    Vector3L toFixed();

    @Override
    default Vector3F normalize() {
        final Vector3F fVec = toFloat();
        return fVec.divide(fVec.length());
    }

    @Override
    default NumberType crossSquareLength(final Self other) {
        return cross(other).squareLength();
    }
}
