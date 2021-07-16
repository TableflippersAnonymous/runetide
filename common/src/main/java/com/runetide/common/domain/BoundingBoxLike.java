package com.runetide.common.domain;

import com.runetide.common.dto.Vec;
import com.runetide.common.dto.VectorLike;
import org.jetbrains.annotations.Contract;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface BoundingBoxLike<T extends VectorLike<T, U>, U extends Vec<U>> extends Iterable<T> {
    @Contract(pure = true)
    boolean contains(final T element);
    @Contract(pure = true)
    boolean intersectsWith(final BoundingBox<T, U> other);
    @Contract(pure = true)
    Optional<? extends BoundingBoxLike<T, U>> intersect(final BoundingBox<T, U> other);
    @Contract(pure = true)
    BoundingBoxSet<T, U> union(final BoundingBox<T, U> other);
    @Contract(pure = true)
    Optional<BoundingBoxSet<T, U>> subtract(final BoundingBox<T, U> other);

    @Contract(pure = true)
    U getDimensions();

    @Contract(pure = true)
    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
}
