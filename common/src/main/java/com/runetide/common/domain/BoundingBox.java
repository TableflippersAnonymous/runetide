package com.runetide.common.domain;

import com.runetide.common.dto.Vec;
import com.runetide.common.dto.VectorLike;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class BoundingBox<T extends VectorLike<T, U>, U extends Vec<U>> implements BoundingBoxLike<T, U> {
    private final T start;
    private final T end;

    public BoundingBox(final T start, final T end) {
        if(!start.isSameCoordinateSystem(end))
            throw new IllegalArgumentException("start/end must exist in same coordinate system.");
        this.start = start.minCoordinates(end);
        this.end = end.maxCoordinates(start);
    }

    public BoundingBox(final T start, final U vec) {
        this(start, start.add(vec));
    }

    public T getStart() {
        return start;
    }

    public T getEnd() {
        return end;
    }

    @Override
    public U getDimensions() {
        return end.subtract(start);
    }

    public BoundingBox<T, U> move(final U direction) {
        return new BoundingBox<>(start.add(direction), end.add(direction));
    }

    public BoundingBox<T, U> resize(final U direction) {
        return new BoundingBox<>(start.add(direction.negate()), end.add(direction));
    }

    @Override
    public boolean contains(final T element) {
        if(!element.isSameCoordinateSystem(start))
            return false;
        return element.isBetween(start, end);
    }

    @Override
    public boolean intersectsWith(final BoundingBox<T, U> other) {
        if(!other.start.isSameCoordinateSystem(start))
            return false;
        return !(start.anyCoordinateCompares(c -> c > 0, other.end)
                || other.start.anyCoordinateCompares(c -> c > 0, end));
    }

    @Override
    public Optional<BoundingBox<T, U>> intersect(final BoundingBox<T, U> other) {
        if(!intersectsWith(other))
            return Optional.empty();
        return Optional.of(new BoundingBox<>(start.maxCoordinates(other.start), end.minCoordinates(other.end)));
    }

    @Override
    public BoundingBoxSet<T, U> union(final BoundingBox<T, U> other) {
        //FIXME
        return new BoundingBoxSet<>();
    }

    @Override
    public Optional<BoundingBoxSet<T, U>> subtract(final BoundingBox<T, U> other) {
        //FIXME
        return Optional.empty();
    }

    @Override
    public Iterator<T> iterator() {
        return start.iteratorTo(end);
    }

    public <W extends VectorLike<W, X>, X extends Vec<X>> BoundingBox<W, X> map(final Function<T, W> mapper) {
        return map(mapper, mapper);
    }

    public <W extends VectorLike<W, X>, X extends Vec<X>> BoundingBox<W, X> map(final Function<T, W> startMapper,
                                                                                final Function<T, W> endMapper) {
        return new BoundingBox<>(startMapper.apply(start), endMapper.apply(end));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final BoundingBox<?, ?> that = (BoundingBox<?, ?>) o;
        return Objects.equals(start, that.start) && Objects.equals(end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
