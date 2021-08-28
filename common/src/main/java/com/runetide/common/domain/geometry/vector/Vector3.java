package com.runetide.common.domain.geometry.vector;

import org.jetbrains.annotations.Contract;

public interface Vector3<Self extends Vector3<Self, NumberType>, NumberType extends Number>
        extends Vector<Self, NumberType> {
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
