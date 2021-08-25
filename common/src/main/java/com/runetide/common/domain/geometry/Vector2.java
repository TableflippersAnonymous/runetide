package com.runetide.common.domain.geometry;

import org.jetbrains.annotations.Contract;

public interface Vector2<Self extends Vector2<Self, NumberType>, NumberType extends Number>
        extends Vector<Self, NumberType> {
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