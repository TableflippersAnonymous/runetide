package com.runetide.common.domain.geometry.locus;

import com.runetide.common.domain.geometry.point.Point;
import com.runetide.common.domain.geometry.vector.Vector;

import java.util.*;

public abstract class BoundingBoxSingle<BBType extends BoundingBoxSingle<BBType, BBSet, PointType, VecType, NumberType>,
        BBSet extends BoundingBoxSet<BBSet, BBType, PointType, VecType, NumberType>,
        PointType extends Point<PointType, VecType, NumberType>,
        VecType extends Vector<VecType, NumberType>, NumberType extends Number>
        implements BoundingBox<BBType, PointType, VecType, NumberType> {
    @FunctionalInterface
    protected interface Constructor<BBType, PointType> {
        BBType construct(final PointType start, final PointType end);
    }

    private final Constructor<BBType, PointType> constructor;

    protected final PointType start;
    protected final PointType end;

    protected BoundingBoxSingle(final Constructor<BBType, PointType> constructor, final PointType start,
                                final PointType end) {
        if(!start.isSameCoordinateSystem(end))
            throw new IllegalArgumentException("start/end must exist in same coordinate system.");
        this.constructor = constructor;
        this.start = start.minCoordinates(end);
        this.end = end.maxCoordinates(start);
    }

    protected abstract BBSet toSet();

    public PointType getStart() {
        return start;
    }

    public PointType getEnd() {
        return end;
    }

    public boolean canMerge(final BBType other) {
        /* In order for two bounding boxes to be merged, they must share a face.  What this really means
         * is at most one dimension may differ between the two start values, that only that same dimension may differ
         * between the end values, and the difference between one box's end and the other's start in the selected
         * dimension is 1.
         */
        if(!start.isSameCoordinateSystem(other.start))
            return false;
        final VecType starts = start.subtract(other.start);
        final VecType ends = end.subtract(other.end);
        final OptionalInt direction = starts.getAlignedAxis();
        if(direction.isEmpty())
            return false;
        if(!direction.equals(ends.getAlignedAxis()))
            return false;
        final VecType endToStart = start.subtract(other.end);
        final VecType directionVector = endToStart.axisVectors().get(direction.getAsInt());
        return endToStart
                .scale(directionVector)
                .equals(directionVector);
    }

    public Optional<BBType> merge(final BBType other) {
        if(canMerge(other))
            return Optional.of(constructor.construct(other.start, end));
        if(other.canMerge(getSelf()))
            return Optional.of(constructor.construct(start, other.end));
        return Optional.empty();
    }

    public BBType outset(final VecType direction) {
        return constructor.construct(start.add(direction.negate()), end.add(direction));
    }

    public BBType inset(final VecType direction) {
        return outset(direction.negate());
    }

    public BBType expand(final VecType direction) {
        return constructor.construct(start.add(direction).minCoordinates(start), end.add(direction).maxCoordinates(end));
    }

    public BBType contract(final VecType direction) {
        return constructor.construct(start.add(direction.negate()).maxCoordinates(start),
                end.add(direction.negate()).minCoordinates(end));
    }

    public BBType adjacent(final VecType direction) {
        return move(direction.scale(getDimensions()));
    }

    public Optional<BBSet> getAdjacents() {
        return outset(getDimensions()).subtract(getSelf());
    }

    public Iterable<BBType> inChunksOf(final VecType maxSize) {
        /* For FixedBoundingBoxes, this will be 1; for FloatBoundingBoxes, this will be 0 */
        final VecType dimensionOffset = end.subtract(start).subtract(getDimensions());
        final List<VecType> axis = maxSize.axisVectors();
        final BBType self = getSelf();
        return () -> new Iterator<>() {
            private Optional<BBType> next = constructor.construct(start, start.add(maxSize).add(dimensionOffset))
                    .intersect(self);

            @Override
            public boolean hasNext() {
                return next.isPresent();
            }

            @Override
            public BBType next() {
                if(next.isEmpty())
                    throw new NoSuchElementException();
                final BBType ret = next.get();
                next = calculateNext(ret);
                return ret;
            }

            private Optional<BBType> calculateNext(final BBType current) {
                BBType ret = current;
                for(int coordinate = 0; coordinate < axis.size(); coordinate++) {
                    ret = ret.adjacent(axis.get(coordinate));
                    if(ret.intersectsWith(self))
                        return ret.intersect(self);
                    final PointType newStart = ret.start.withCoordinateFrom(self.start, coordinate);
                    ret = constructor.construct(newStart, newStart.add(maxSize).add(dimensionOffset));
                }
                return Optional.empty();
            }
        };
    }

    public Iterable<BBType> expandingOut(final Optional<VecType> chunkSize) {
        final VecType actualChunkSize = chunkSize.orElse(getDimensions());
        final BBType self = getSelf();
        return () -> new Iterator<>() {
            private BBType current = self;
            private Iterator<BBType> bbIter = current.toSet().getBoxes().iterator();
            private Iterator<BBType> chunkIter = bbIter.next().inChunksOf(actualChunkSize).iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public BBType next() {
                if(!chunkIter.hasNext()) {
                    if(!bbIter.hasNext()) {
                        final BBType prev = current;
                        current = current.outset(actualChunkSize);
                        bbIter = current.subtract(prev).orElseThrow().getBoxes().iterator();
                    }
                    chunkIter = bbIter.next().inChunksOf(actualChunkSize).iterator();
                }
                return chunkIter.next();
            }
        };
    }

    @Override
    public Optional<BBType> intersect(final BBType other) {
        if(!intersectsWith(other))
            return Optional.empty();
        return Optional.of(constructor.construct(start.maxCoordinates(other.start), end.minCoordinates(other.end)));
    }

    @Override
    public BBSet union(final BBType other) {
        return toSet().union(other);
    }

    @Override
    public abstract Optional<BBSet> subtract(final BBType other);

    @Override
    public BBType move(final VecType direction) {
        return constructor.construct(start.add(direction), end.add(direction));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final BoundingBoxSingle<?, ?, ?, ?, ?> that = (BoundingBoxSingle<?, ?, ?, ?, ?>) o;
        return start.equals(that.start) && end.equals(that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
