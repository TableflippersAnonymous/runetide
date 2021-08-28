package com.runetide.common.domain.geometry.locus;

import com.runetide.common.domain.geometry.point.Point;
import org.jetbrains.annotations.Contract;

import java.util.Optional;

public interface Locus<LocusType extends Locus<LocusType, PointType>, PointType extends Point<PointType, ?, ?>> {
    @Contract(pure = true)
    boolean contains(final PointType element);

    @Contract(pure = true)
    default Optional<? extends Locus<?, PointType>> intersect(final LocusType other) {
        return Optional.of(new PredicateLocus<>(point -> contains(point) && other.contains(point)));
    }

    @Contract(pure = true)
    default Locus<?, PointType> union(final LocusType other) {
        return new PredicateLocus<>(point -> contains(point) || other.contains(point));
    }

    @Contract(pure = true)
    default Optional<? extends Locus<?, PointType>> subtract(final LocusType other) {
        return Optional.of(new PredicateLocus<>(point -> contains(point) && !other.contains(point)));
    }
}
