package com.runetide.common.domain.geometry;

import com.google.common.collect.Iterators;

import java.util.Iterator;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FixedBoundingBoxSet<PointType extends FixedPoint<PointType, VecType>, VecType extends FixedVector<VecType>>
        extends BoundingBoxSet<FixedBoundingBoxSet<PointType, VecType>, FixedBoundingBoxSingle<PointType, VecType>,
        PointType, VecType, Long>
        implements FixedBoundingBox<FixedBoundingBoxSet<PointType, VecType>, PointType, VecType> {

    public static <PointType extends FixedPoint<PointType, VecType>, VecType extends FixedVector<VecType>>
    FixedBoundingBoxSet<PointType, VecType> of(final FixedBoundingBoxSingle<PointType, VecType> box) {
        return of(Set.of(box));
    }

    public static <PointType extends FixedPoint<PointType, VecType>, VecType extends FixedVector<VecType>>
    FixedBoundingBoxSet<PointType, VecType> of(final Set<FixedBoundingBoxSingle<PointType, VecType>> boxes) {
        return new FixedBoundingBoxSet<>(boxes);
    }

    private FixedBoundingBoxSet(final Set<FixedBoundingBoxSingle<PointType, VecType>> boxes) {
        super(FixedBoundingBoxSet::of, boxes);
    }

    @Override
    public Iterator<PointType> iterator() {
        return Iterators.concat(Iterators.transform(boxes.iterator(), FixedBoundingBoxSingle::iterator));
    }

    public FixedBoundingBoxSingle<PointType, VecType> toBoundingBox() {
        final PointType start = boxes.stream().map(BoundingBoxSingle::getStart).reduce(PointType::minCoordinates)
                .orElseThrow(IllegalStateException::new);
        final PointType end = boxes.stream().map(BoundingBoxSingle::getEnd).reduce(PointType::maxCoordinates)
                .orElseThrow(IllegalStateException::new);
        return FixedBoundingBoxSingle.of(start, end);
    }

    @Override
    public FixedBoundingBoxSet<PointType, VecType> getSelf() {
        return this;
    }

    @Override
    public <NewPointType extends FixedPoint<NewPointType, NewVecType>, NewVecType extends FixedVector<NewVecType>>
    FixedBoundingBoxSet<NewPointType, NewVecType> map(final Function<PointType, NewPointType> startMapper,
                                                      final Function<PointType, NewPointType> endMapper) {
        return of(boxes.stream().map(box -> box.map(startMapper, endMapper))
                .collect(Collectors.toUnmodifiableSet()));
    }
}
