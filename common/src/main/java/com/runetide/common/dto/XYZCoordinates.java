package com.runetide.common.dto;

import com.runetide.common.domain.Vec3D;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public interface XYZCoordinates<T extends XYZCoordinates<T>> extends BaseXZCoordinates<T, Vec3D> {
    T withYFrom(final T other);
    Comparator<T> getYComparator();

    default T withMinYFrom(final T other) {
        if(getYComparator().compare(getSelf(), other) <= 0)
            return getSelf();
        return withYFrom(other);
    }

    default T withMaxYFrom(final T other) {
        if(getYComparator().compare(getSelf(), other) >= 0)
            return getSelf();
        return withYFrom(other);
    }

    @Override
    default T minCoordinates(final T other) {
        return withMinXFrom(other).withMinYFrom(other).withMinZFrom(other);
    }

    @Override
    default T maxCoordinates(final T other) {
        return withMaxXFrom(other).withMaxYFrom(other).withMaxZFrom(other);
    }

    @Override
    default boolean isBetween(final T start, final T end) {
        final Comparator<T> compareByX = getXComparator();
        final Comparator<T> compareByY = getYComparator();
        final Comparator<T> compareByZ = getZComparator();
        return compareByX.compare(start, getSelf()) <= 0
                && compareByX.compare(getSelf(), end) <= 0
                && compareByY.compare(start, getSelf()) <= 0
                && compareByY.compare(getSelf(), end) <= 0
                && compareByZ.compare(start, getSelf()) <= 0
                && compareByZ.compare(getSelf(), end) <= 0;
    }

    @Override
    default Iterator<T> iteratorTo(final T end) {
        final Comparator<T> compareByX = getXComparator();
        final Comparator<T> compareByZ = getZComparator();
        final T start = getSelf();
        return new Iterator<>() {
            private T current = null;

            @Override
            public boolean hasNext() {
                return !Objects.equals(current, end);
            }

            @Override
            public T next() {
                if(!hasNext())
                    throw new NoSuchElementException();
                if(current == null)
                    current = start;
                else if(compareByX.compare(current, end) < 0)
                    current = current.add(Vec3D.UNIT_X);
                else if(compareByZ.compare(current, end) < 0)
                    current = current.withXFrom(start).add(Vec3D.UNIT_Z);
                else
                    current = current.withXFrom(start).withZFrom(start).add(Vec3D.UNIT_Y);
                return current;
            }
        };
    }
}
