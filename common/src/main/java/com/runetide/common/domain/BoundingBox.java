package com.runetide.common.domain;

import com.runetide.common.dto.Vec;
import com.runetide.common.dto.VectorLike;

import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class BoundingBox<T extends VectorLike<T, U>, U extends Vec<U>> implements Iterable<T> {
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

    public U getDimensions() {
        return end.subtract(start);
    }

    public BoundingBox<T, U> move(final U direction) {
        return new BoundingBox<>(start.add(direction), end.add(direction));
    }

    public BoundingBox<T, U> resize(final U direction) {
        return new BoundingBox<>(start.add(direction.negate()), end.add(direction));
    }

    public boolean contains(final T column) {
        return column.isBetween(start, end);
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

    public Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
}
