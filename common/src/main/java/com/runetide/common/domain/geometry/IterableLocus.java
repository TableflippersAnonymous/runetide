package com.runetide.common.domain.geometry;

import org.jetbrains.annotations.Contract;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface IterableLocus<LocusType extends IterableLocus<LocusType, PointType, VecType>,
        PointType extends Point<PointType, VecType>, VecType extends Vector<VecType>>
        extends Locus<LocusType, PointType>, Iterable<PointType> {

    @Contract(pure = true)
    default Stream<PointType> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
    @Contract(pure = true)
    boolean intersectsWith(final LocusType other);
    @Contract(pure = true)
    VecType getDimensions();
}
