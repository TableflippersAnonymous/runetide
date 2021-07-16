package com.runetide.common.dto;

import org.jetbrains.annotations.Contract;

public interface Vec<T extends Vec<T>> extends VectorLike<T, T> {
    @Contract(pure = true)
    T negate();
    @Contract(pure = true)
    T scale(final T other);
    @Contract(pure = true)
    T divide(final T other);
    @Contract(pure = true)
    T modulo(final T other);
}
