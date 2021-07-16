package com.runetide.common.dto;

import org.jetbrains.annotations.Contract;

import java.util.Comparator;

public interface BaseXZCoordinates<T extends BaseXZCoordinates<T, U>, U extends Vec<U>> extends VectorLike<T, U> {
    @Contract(pure = true)
    T withXFrom(final T other);

    @Contract(pure = true)
    T withZFrom(final T other);

    @Contract(pure = true)
    Comparator<T> getXComparator();

    @Contract(pure = true)
    Comparator<T> getZComparator();

    @Contract(pure = true, value = "-> this")
    T getSelf();

    @Contract(pure = true)
    default T withMinXFrom(final T other) {
        if (getXComparator().compare(getSelf(), other) <= 0)
            return getSelf();
        return withXFrom(other);
    }

    @Contract(pure = true)
    default T withMinZFrom(final T other) {
        if (getZComparator().compare(getSelf(), other) <= 0)
            return getSelf();
        return withZFrom(other);
    }

    @Contract(pure = true)
    default T withMaxXFrom(final T other) {
        if (getXComparator().compare(getSelf(), other) >= 0)
            return getSelf();
        return withXFrom(other);
    }

    @Contract(pure = true)
    default T withMaxZFrom(final T other) {
        if (getZComparator().compare(getSelf(), other) >= 0)
            return getSelf();
        return withZFrom(other);
    }
}
