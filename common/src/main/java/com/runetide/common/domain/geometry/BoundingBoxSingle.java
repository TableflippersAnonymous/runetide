package com.runetide.common.domain.geometry;

import java.util.Optional;
import java.util.OptionalInt;

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
        if(!canMerge(other))
            return Optional.empty();
        return Optional.of(constructor.construct(other.start, end));
    }

    public BBType resize(final VecType direction) {
        return constructor.construct(start.add(direction.negate()), end.add(direction));
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
}
