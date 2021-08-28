package com.runetide.common.domain.geometry.vector;

import org.jetbrains.annotations.Contract;

import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public interface FloatVector<Self extends FloatVector<Self>> extends Vector<Self, Double> {
    @Override
    default OptionalInt getAlignedAxis() {
        final List<Self> axisVectors = axisVectors();
        return IntStream.range(0, axisVectors.size())
                .filter(i -> crossSquareLength(axisVectors.get(i)) == 0)
                .findFirst();
    }

    @Contract(pure = true)
    default double length() {
        return Math.sqrt(squareLength());
    }

    @Contract(pure = true)
    default double crossLength(final Self other) {
        return Math.sqrt(crossSquareLength(other));
    }
}
