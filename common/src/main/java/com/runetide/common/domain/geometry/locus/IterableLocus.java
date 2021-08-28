package com.runetide.common.domain.geometry.locus;

import com.runetide.common.domain.geometry.point.Point;
import com.runetide.common.domain.geometry.vector.Vector;
import org.jetbrains.annotations.Contract;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface IterableLocus<LocusType extends IterableLocus<LocusType, PointType, VecType>,
        PointType extends Point<PointType, VecType, Long>, VecType extends Vector<VecType, Long>>
        extends Locus<LocusType, PointType>, Iterable<PointType> {
    @Contract(pure = true)
    default Stream<PointType> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
}
