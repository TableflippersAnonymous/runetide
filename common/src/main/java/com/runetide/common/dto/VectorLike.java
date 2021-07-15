package com.runetide.common.dto;

import java.util.Iterator;

public interface VectorLike<T extends VectorLike<T, U>, U extends Vec<U>> {
    boolean isSameCoordinateSystem(final T other);

    T minCoordinates(T other);
    T maxCoordinates(T other);

    T add(final U other);
    U subtract(final T other);

    boolean isBetween(T start, T end);

    Iterator<T> iteratorTo(T end);
}
