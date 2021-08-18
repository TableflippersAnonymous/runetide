package com.runetide.common.domain.geometry;

import java.util.Set;

public class FloatBoundingBoxSet<PointType extends Point<PointType, VecType, Double>,
        VecType extends Vector<VecType, Double>>
        extends BoundingBoxSet<FloatBoundingBoxSet<PointType, VecType>, FloatBoundingBoxSingle<PointType, VecType>,
        PointType, VecType, Double> {

    public FloatBoundingBoxSet(final Set<FloatBoundingBoxSingle<PointType, VecType>> boxes) {
        super(FloatBoundingBoxSet::new, boxes);
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
        return new FloatBoundingBoxSingle<>(start, end);
    }
}
