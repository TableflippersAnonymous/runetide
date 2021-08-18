package com.runetide.common.domain.geometry;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;

public class FloatBoundingBoxSingle<PointType extends Point<PointType, VecType, Double>,
        VecType extends Vector<VecType, Double>>
        extends BoundingBoxSingle<FloatBoundingBoxSingle<PointType, VecType>, FloatBoundingBoxSet<PointType, VecType>,
        PointType, VecType, Double> {

    public FloatBoundingBoxSingle(final PointType start, final PointType end) {
        super(FloatBoundingBoxSingle::new, start, end);
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
        return new FloatBoundingBoxSingle<>(start.add(direction), end.add(direction));
    }

    @Override
    public FloatBoundingBoxSingle<PointType, VecType> getSelf() {
        return this;
    }

    @Override
    protected FloatBoundingBoxSet<PointType, VecType> toSet() {
        return new FloatBoundingBoxSet<>(Set.of(this));
    }

    @Override
    public Optional<FloatBoundingBoxSet<PointType, VecType>> subtract(final FloatBoundingBoxSingle<PointType, VecType> other) {
        /* This is very similar to FixedBoundingBoxSingle.subtract, but with float bounding boxes, we don't consider
         * end to be in the set, so certain changes to how we calculate offsets are needed.
         */
        if(!intersectsWith(other))
            return Optional.of(new FloatBoundingBoxSet<>(Set.of(this)));
        Optional<FloatBoundingBoxSet<PointType, VecType>> ret = Optional.empty();
        for(int coordinate = 0; coordinate < start.coordinateSize(); coordinate++) {
            final Comparator<PointType> coordinateComparator = start.compareByCoordinate(coordinate);
            if(coordinateComparator.compare(start, other.start) < 0) {
                final FloatBoundingBoxSingle<PointType, VecType> bb = new FloatBoundingBoxSingle<>(start,
                        end.withCoordinateFrom(other.start, coordinate));
                ret = ret.map(bbs -> bbs.union(bb)).or(() -> Optional.of(new FloatBoundingBoxSet<>(Set.of(bb))));
            }
            if(coordinateComparator.compare(end, other.end) > 0) {
                final FloatBoundingBoxSingle<PointType, VecType> bb = new FloatBoundingBoxSingle<>(
                        start.withCoordinateFrom(other.end, coordinate), end);
                ret = ret.map(bbs -> bbs.union(bb)).or(() -> Optional.of(new FloatBoundingBoxSet<>(Set.of(bb))));
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
