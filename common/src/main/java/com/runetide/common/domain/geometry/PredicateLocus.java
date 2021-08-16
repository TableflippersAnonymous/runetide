package com.runetide.common.domain.geometry;

import java.util.function.Predicate;

public class PredicateLocus<PointType extends Point<PointType, ?>>
        implements Locus<PredicateLocus<PointType>, PointType> {
    final private Predicate<PointType> predicate;

    public PredicateLocus(final Predicate<PointType> predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean contains(final PointType element) {
        return predicate.test(element);
    }
}
