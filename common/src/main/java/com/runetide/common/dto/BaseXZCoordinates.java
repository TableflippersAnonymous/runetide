package com.runetide.common.dto;

import java.util.Comparator;

public interface BaseXZCoordinates<T extends BaseXZCoordinates<T, U>, U extends Vec<U>> extends VectorLike<T, U> {
    T withXFrom(final T other);

    T withZFrom(final T other);

    Comparator<T> getXComparator();

    Comparator<T> getZComparator();

    T getSelf();

    default T withMinXFrom(final T other) {
        if (getXComparator().compare(getSelf(), other) <= 0)
            return getSelf();
        return withXFrom(other);
    }

    default T withMinZFrom(final T other) {
        if (getZComparator().compare(getSelf(), other) <= 0)
            return getSelf();
        return withZFrom(other);
    }

    default T withMaxXFrom(final T other) {
        if (getXComparator().compare(getSelf(), other) >= 0)
            return getSelf();
        return withXFrom(other);
    }

    default T withMaxZFrom(final T other) {
        if (getZComparator().compare(getSelf(), other) >= 0)
            return getSelf();
        return withZFrom(other);
    }

}
