package com.runetide.common.dto;

import org.jetbrains.annotations.Contract;

import java.util.Comparator;

public interface BaseXZCoordinates<Self extends BaseXZCoordinates<Self, VecType>, VecType extends Vec<VecType>>
        extends VectorLike<Self, VecType> {
    @Contract(pure = true)
    default Self withXFrom(final Self other) {
        return withCoordinateFrom(other, COORDINATE_X);
    }

    @Contract(pure = true)
    default Self withZFrom(final Self other) {
        return withCoordinateFrom(other, COORDINATE_Z);
    }

    @Contract(pure = true)
    default Comparator<Self> getXComparator() {
        return compareByCoordinate(COORDINATE_X);
    }

    @Contract(pure = true)
    default Comparator<Self> getZComparator() {
        return compareByCoordinate(COORDINATE_Z);
    }
}
