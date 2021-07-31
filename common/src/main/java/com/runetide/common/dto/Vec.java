package com.runetide.common.dto;

import com.runetide.common.domain.Vec3D;
import org.jetbrains.annotations.Contract;

import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public interface Vec<Self extends Vec<Self>> extends VectorLike<Self, Self> {
    @Contract(pure = true)
    Self negate();
    @Contract(pure = true)
    Self scale(final Self other);
    @Contract(pure = true)
    Vec3D cross(final Self other);
    @Contract(pure = true)
    long dot(final Self other);
    @Contract(pure = true)
    Self divide(final Self other);
    @Contract(pure = true)
    Self modulo(final Self other);
    @Contract(pure = true)
    List<Self> axisVectors();

    @Contract(pure = true)
    default long crossSquareLength(final Self other) {
        return cross(other).squareLength();
    }

    @Contract(pure = true)
    default double crossLength(final Self other) {
        return Math.sqrt(crossSquareLength(other));
    }

    @Contract(pure = true)
    default long squareLength() {
        return dot(getSelf());
    }

    @Contract(pure = true)
    default double length() {
        return Math.sqrt(squareLength());
    }

    @Contract(pure = true)
    default boolean isAxisAligned() {
        return getAlignedAxis().isPresent();
    }

    @Contract(pure = true)
    default OptionalInt getAlignedAxis() {
        final List<Self> axisVectors = axisVectors();
        return IntStream.range(0, axisVectors.size())
                .filter(i -> crossSquareLength(axisVectors.get(i)) == 0)
                .findFirst();
    }
}
