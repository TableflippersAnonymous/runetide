package com.runetide.common.domain.geometry;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public class FixedBoundingBoxSingle<PointType extends FixedPoint<PointType, VecType>, VecType extends FixedVector<VecType>>
        extends BoundingBoxSingle<FixedBoundingBoxSingle<PointType, VecType>, FixedBoundingBoxSet<PointType, VecType>,
        PointType, VecType, Long>
        implements FixedBoundingBox<FixedBoundingBoxSingle<PointType, VecType>, PointType, VecType>,
        IterableLocus<FixedBoundingBoxSingle<PointType, VecType>, PointType, VecType> {

    public FixedBoundingBoxSingle(final PointType start, final PointType end) {
        super(FixedBoundingBoxSingle::new, start, end);
    }

    @Override
    public boolean contains(final PointType element) {
        if(!element.isSameCoordinateSystem(start))
            return false;
        return element.isBetween(start, end);
    }

    @Override
    public boolean intersectsWith(final FixedBoundingBoxSingle<PointType, VecType> other) {
        if(!other.start.isSameCoordinateSystem(start))
            return false;
        return !(start.anyCoordinateCompares(c -> c > 0, other.end)
                || other.start.anyCoordinateCompares(c -> c > 0, end));
    }

    @Override
    public VecType getDimensions() {
        return end.subtract(start).add(1);
    }

    @Override
    public FixedBoundingBoxSingle<PointType, VecType> getSelf() {
        return this;
    }

    @Override
    public Iterator<PointType> iterator() {
        return start.iteratorTo(end);
    }

    @Override
    protected FixedBoundingBoxSet<PointType, VecType> toSet() {
        return new FixedBoundingBoxSet<>(Set.of(this));
    }

    @Override
    public Optional<FixedBoundingBoxSet<PointType, VecType>> subtract(final FixedBoundingBoxSingle<PointType, VecType> other) {
        /* When subtracting one bounding box from another, we have a few different options:
         * 1) There is no intersection between the two bounding boxes, in which case, we can just return the original
         * 2) The subtracted box completely covers the original, and we need to return Optional.empty
         * 3) There is an intersection, and if this is the case, we need to split the original bounding box into smaller
         * bounding boxes so that we can have one bounding box that matches the subtracted one exactly, and then remove
         * just that one from the set.
         *
         * The split parts should be:
         * - {[orig.start, orig.end.withCoordinateFrom(sub.start - 1, c)] for c : coordinates}
         * - {[orig.start.withCoordinateFrom(sub.end + 1, c)] for c : coordinates}
         *
         * For 2 dimensions, this looks like:
         * +--------------+ <- orig.end
         * |              |
         * |  +------+    | <- sub.end
         * |  |      |    |
         * |  |      |    |
         * |  +------+    |
         * |              |
         * +--------------+
         * ^  ^- sub.start
         * `- orig.start
         *
         * We want to convert it into:
         * +--+------+----+
         * |     2        |
         * +  +------+    +
         * |  |      |    |
         * |1 |      | 3  |
         * +  +------+    +
         * |    4         |
         * +--|------+----+
         *
         * 1) [orig.start, orig.end.withCoordinateFrom(sub.start - 1, 0)]
         * 4) [orig.start, orig.end.withCoordinateFrom(sub.start - 1, 1)]
         * 3) [orig.start.withCoordinateFrom(sub.end + 1, 0), orig.end]
         * 2) [orig.start.withCoordinateFrom(sub.end + 1, 1), orig.end]
         * Note: These intersect, and will need the de-intersecting logic from union().
         */
        if(!intersectsWith(other))
            return Optional.of(new FixedBoundingBoxSet<>(Set.of(this)));
        Optional<FixedBoundingBoxSet<PointType, VecType>> ret = Optional.empty();
        for(int coordinate = 0; coordinate < start.coordinateSize(); coordinate++) {
            final Comparator<PointType> coordinateComparator = start.compareByCoordinate(coordinate);
            if(coordinateComparator.compare(start, other.start) < 0) {
                final FixedBoundingBoxSingle<PointType, VecType> bb = new FixedBoundingBoxSingle<>(start,
                        end.withCoordinateFrom(other.start.add(-1), coordinate));
                ret = ret.map(bbs -> bbs.union(bb)).or(() -> Optional.of(new FixedBoundingBoxSet<>(Set.of(bb))));
            }
            if(coordinateComparator.compare(end, other.end) > 0) {
                final FixedBoundingBoxSingle<PointType, VecType> bb = new FixedBoundingBoxSingle<>(
                        start.withCoordinateFrom(other.end.add(1), coordinate), end);
                ret = ret.map(bbs -> bbs.union(bb)).or(() -> Optional.of(new FixedBoundingBoxSet<>(Set.of(bb))));
            }
        }
        return ret;
    }

    @Override
    public <NewPointType extends FixedPoint<NewPointType, NewVecType>, NewVecType extends FixedVector<NewVecType>>
    FixedBoundingBoxSingle<NewPointType, NewVecType> map(final Function<PointType, NewPointType> startMapper,
                                                         final Function<PointType, NewPointType> endMapper) {
        return new FixedBoundingBoxSingle<>(startMapper.apply(start), endMapper.apply(end));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final FixedBoundingBoxSingle<?, ?> that = (FixedBoundingBoxSingle<?, ?>) o;
        return Objects.equals(start, that.start) && Objects.equals(end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public String toString() {
        return "<FixedBB:" + start + "-" + end + ">";
    }
}
