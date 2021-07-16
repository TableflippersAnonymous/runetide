package com.runetide.common.domain;

import com.google.common.collect.Iterators;
import com.runetide.common.dto.Vec;
import com.runetide.common.dto.VectorLike;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class BoundingBoxSet<T extends VectorLike<T, U>, U extends Vec<U>> implements BoundingBoxLike<T, U> {
    private final Set<BoundingBox<T, U>> boxes;

    public BoundingBoxSet(final Set<BoundingBox<T, U>> boxes) {
        if(boxes.size() == 0)
            throw new IllegalArgumentException("boxes must have a positive cardinality.");
        this.boxes = boxes;
    }

    @Override
    public Iterator<T> iterator() {
        return Iterators.concat(Iterators.transform(boxes.iterator(), BoundingBox::iterator));
    }

    @Override
    public boolean contains(final T element) {
        return boxes.stream().anyMatch(bb -> bb.contains(element));
    }

    @Override
    public boolean intersectsWith(final BoundingBox<T, U> other) {
        return boxes.stream().anyMatch(bb -> bb.intersectsWith(other));
    }

    @Override
    public U getDimensions() {
        return toBoundingBox().getDimensions();
    }

    public BoundingBox<T, U> toBoundingBox() {
        final T start = boxes.stream().map(BoundingBox::getStart).reduce(VectorLike::minCoordinates)
                .orElseThrow(IllegalStateException::new);
        final T end = boxes.stream().map(BoundingBox::getEnd).reduce(VectorLike::maxCoordinates)
                .orElseThrow(IllegalStateException::new);
        return new BoundingBox<>(start, end);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final BoundingBoxSet<?, ?> that = (BoundingBoxSet<?, ?>) o;
        return Objects.equals(boxes, that.boxes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boxes);
    }
}
