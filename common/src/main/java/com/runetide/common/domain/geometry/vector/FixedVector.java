package com.runetide.common.domain.geometry.vector;

import com.runetide.common.domain.geometry.point.FixedPoint;

import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public interface FixedVector<Self extends FixedVector<Self>> extends Vector<Self, Long>, FixedPoint<Self, Self> {
    static Vector2L of(final int x, final int z) {
        return Vector2L.of(x, z);
    }

    static Vector2L of(final long x, final long z) {
        return Vector2L.of(x, z);
    }

    static Vector3L of(final int x, final int y, final int z) {
        return Vector3L.of(x, y, z);
    }

    static Vector3L of(final long x, final long y, final long z) {
        return Vector3L.of(x, y, z);
    }

    @Override
    default OptionalInt getAlignedAxis() {
        final List<Self> axisVectors = axisVectors();
        return IntStream.range(0, axisVectors.size())
                .filter(i -> crossSquareLength(axisVectors.get(i)) == 0)
                .findFirst();
    }
}
