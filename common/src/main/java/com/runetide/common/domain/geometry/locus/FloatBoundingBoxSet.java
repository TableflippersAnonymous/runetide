package com.runetide.common.domain.geometry.locus;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.runetide.common.domain.geometry.point.Point;
import com.runetide.common.domain.geometry.vector.Vector;

import java.util.Set;

@SuppressWarnings("UnstableApiUsage")
public class FloatBoundingBoxSet<PointType extends Point<PointType, VecType, Double>,
        VecType extends Vector<VecType, Double>>
        extends BoundingBoxSet<FloatBoundingBoxSet<PointType, VecType>, FloatBoundingBoxSingle<PointType, VecType>,
        PointType, VecType, Double> {
    private static final Interner<FloatBoundingBoxSet<?, ?>> INTERNER = Interners.newWeakInterner();

    public static <PointType extends Point<PointType, VecType, Double>, VecType extends Vector<VecType, Double>>
    FloatBoundingBoxSet<PointType, VecType> of(final FloatBoundingBoxSingle<PointType, VecType> box) {
        return of(Set.of(box));
    }

    public static <PointType extends Point<PointType, VecType, Double>, VecType extends Vector<VecType, Double>>
    FloatBoundingBoxSet<PointType, VecType> of(final Set<FloatBoundingBoxSingle<PointType, VecType>> boxes) {
        //noinspection unchecked
        return (FloatBoundingBoxSet<PointType, VecType>) INTERNER.intern(new FloatBoundingBoxSet<>(boxes));
    }

    public FloatBoundingBoxSet(final Set<FloatBoundingBoxSingle<PointType, VecType>> boxes) {
        super(FloatBoundingBoxSet::of, boxes);
    }

    @Override
    public FloatBoundingBoxSet<PointType, VecType> getSelf() {
        return this;
    }

    @Override
    public FloatBoundingBoxSingle<PointType, VecType> toBoundingBox() {
        final PointType start = boxes.stream().map(BoundingBoxSingle::getStart).reduce(PointType::minCoordinates)
                .orElseThrow(IllegalStateException::new);
        final PointType end = boxes.stream().map(BoundingBoxSingle::getEnd).reduce(PointType::maxCoordinates)
                .orElseThrow(IllegalStateException::new);
        return FloatBoundingBoxSingle.of(start, end);
    }
}
