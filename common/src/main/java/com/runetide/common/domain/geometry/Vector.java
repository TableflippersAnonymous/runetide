package com.runetide.common.domain.geometry;

import org.jetbrains.annotations.Contract;

import java.util.List;
import java.util.OptionalInt;

public interface Vector<Self extends Vector<Self, NumberType>, NumberType extends Number>
        extends Point<Self, Self, NumberType> {
    @Contract(pure = true)
    Self negate();
    @Contract(pure = true)
    Self scale(final Self other);
    @Contract(pure = true)
    Self scale(final NumberType scalar);
    @Contract(pure = true)
    Self divide(final Self other);
    @Contract(pure = true)
    Self divide(final NumberType scalar);
    @Contract(pure = true)
    Self modulo(final Self other);
    @Contract(pure = true)
    List<Self> axisVectors();
    @Contract(pure = true)
    NumberType sum();
    @Contract(pure = true)
    NumberType product();
    @Contract(pure = true)
    OptionalInt getAlignedAxis();
    @Contract(pure = true)
    NumberType crossSquareLength(final Self other);
    @Contract(pure = true)
    FloatVector<?> toFloat();
    @Contract(pure = true)
    FixedVector<?> toFixed();

    @Contract(pure = true)
    default NumberType dot(final Self other) {
        return scale(other).sum();
    }

    @Contract(pure = true)
    default NumberType squareLength() {
        return dot(getSelf());
    }

    @Contract(pure = true)
    default boolean isAxisAligned() {
        return getAlignedAxis().isPresent();
    }

    @Contract(pure = true)
    default Self transform(final SquareMatrix<?, Self, NumberType> transform) {
        return transform.multiply(getSelf());
    }

    @Contract(pure = true)
    default FloatVector<?> normalize() {
        final FloatVector<?> fVec = toFloat();
        return fVec.divide(fVec.length());
    }

    @Override
    default boolean isSameCoordinateSystem(final Self other) {
        return true;
    }
}
