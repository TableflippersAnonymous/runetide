package com.runetide.common.domain.geometry;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;

public class FloatBoundingBoxSingle<PointType extends Point<PointType, VecType, Double>,
        VecType extends Vector<VecType, Double>>
        extends BoundingBoxSingle<FloatBoundingBoxSingle<PointType, VecType>, FloatBoundingBoxSet<PointType, VecType>,
        PointType, VecType, Double> {

    public static <PointType extends Point<PointType, VecType, Double>, VecType extends Vector<VecType, Double>>
    FloatBoundingBoxSingle<PointType, VecType> of(final PointType start, final PointType end) {
        return new FloatBoundingBoxSingle<>(start, end);
    }

    private FloatBoundingBoxSingle(final PointType start, final PointType end) {
        super(FloatBoundingBoxSingle::of, start, end);
    }

    @Override
    public boolean intersectsWith(final FloatBoundingBoxSingle<PointType, VecType> other) {
        if(!other.start.isSameCoordinateSystem(start))
            return false;
        return !(start.anyCoordinateCompares(c -> c >= 0, other.end)
                || other.start.anyCoordinateCompares(c -> c >= 0, end));
    }

    @Override
    public VecType getDimensions() {
        return end.subtract(start);
    }

    @Override
    public FloatBoundingBoxSingle<PointType, VecType> move(final VecType direction) {
        return of(start.add(direction), end.add(direction));
    }

    @Override
    public FloatBoundingBoxSingle<PointType, VecType> getSelf() {
        return this;
    }

    @Override
    protected FloatBoundingBoxSet<PointType, VecType> toSet() {
        return FloatBoundingBoxSet.of(this);
    }

    @Override
    public Optional<FloatBoundingBoxSet<PointType, VecType>> subtract(final FloatBoundingBoxSingle<PointType, VecType> other) {
        /* This is very similar to FixedBoundingBoxSingle.subtract, but with float bounding boxes, we don't consider
         * end to be in the set, so certain changes to how we calculate offsets are needed.
         */
        if(!intersectsWith(other))
            return Optional.of(FloatBoundingBoxSet.of(this));
        Optional<FloatBoundingBoxSet<PointType, VecType>> ret = Optional.empty();
        for(int coordinate = 0; coordinate < start.coordinateSize(); coordinate++) {
            final Comparator<PointType> coordinateComparator = start.compareByCoordinate(coordinate);
            if(coordinateComparator.compare(start, other.start) < 0) {
                final FloatBoundingBoxSingle<PointType, VecType> bb = of(start,
                        end.withCoordinateFrom(other.start, coordinate));
                ret = ret.map(bbs -> bbs.union(bb)).or(() -> Optional.of(FloatBoundingBoxSet.of(bb)));
            }
            if(coordinateComparator.compare(end, other.end) > 0) {
                final FloatBoundingBoxSingle<PointType, VecType> bb = of(
                        start.withCoordinateFrom(other.end, coordinate), end);
                ret = ret.map(bbs -> bbs.union(bb)).or(() -> Optional.of(FloatBoundingBoxSet.of(bb)));
            }
        }
        return ret;
    }

    @Override
    public boolean contains(final PointType element) {
        if(!element.isSameCoordinateSystem(start))
            return false;
        return element.isBetweenEndExclusive(start, end);
    }
}
