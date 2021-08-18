package com.runetide.common.domain.geometry;

import org.jetbrains.annotations.Contract;

public interface Vector2<Self extends Vector2<Self, NumberType>, NumberType extends Number>
        extends Vector<Self, NumberType> {
    @Contract(pure = true)
    NumberType getX();
    @Contract(pure = true)
    NumberType getZ();
}