package com.runetide.common.dto;

public interface Vec<T extends Vec<T>> extends VectorLike<T, T> {
    T negate();
    T scale(final T other);
    T divide(final T other);
    T modulo(final T other);
}
