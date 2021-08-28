package com.runetide.common.domain.geometry.vector;

import com.runetide.common.domain.geometry.point.FixedPoint;

import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public interface FixedVector<Self extends FixedVector<Self>> extends Vector<Self, Long>, FixedPoint<Self, Self> {
    @Override
    default OptionalInt getAlignedAxis() {
        final List<Self> axisVectors = axisVectors();
        return IntStream.range(0, axisVectors.size())
                .filter(i -> crossSquareLength(axisVectors.get(i)) == 0)
                .findFirst();
    }
}
