package com.runetide.common.dto;

import org.jetbrains.annotations.Contract;

import java.util.Iterator;
import java.util.function.Predicate;

public interface VectorLike<T extends VectorLike<T, U>, U extends Vec<U>> {
    @Contract(pure = true)
    boolean isSameCoordinateSystem(final T other);

    @Contract(pure = true)
    T minCoordinates(final T other);
    @Contract(pure = true)
    T maxCoordinates(final T other);

    @Contract(pure = true)
    T add(final U other);
    @Contract(pure = true)
    U subtract(final T other);

    @Contract(pure = true)
    boolean anyCoordinateCompares(final Predicate<Integer> compare, final T other);
    @Contract(pure = true)
    boolean allCoordinatesCompare(final Predicate<Integer> compare, final T other);

    @Contract(pure = true)
    Iterator<T> iteratorTo(final T end);

    @Contract(pure = true)
    default boolean isBetween(final T start, final T end) {
        return allCoordinatesCompare(c -> c >= 0, start)
                && allCoordinatesCompare(c -> c <= 0, end);
    }
}
