package com.runetide.common.dto;

import com.runetide.common.domain.Vec2D;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public interface XZCoordinates<T extends XZCoordinates<T>> extends BaseXZCoordinates<T, Vec2D> {

    @Override
    default T minCoordinates(final T other) {
        return withMinXFrom(other).withMinZFrom(other);
    }

    @Override
    default T maxCoordinates(final T other) {
        return withMaxXFrom(other).withMaxZFrom(other);
    }

    @Override
    default boolean isBetween(final T start, final T end) {
        final Comparator<T> compareByX = getXComparator();
        final Comparator<T> compareByZ = getZComparator();
        return compareByX.compare(start, getSelf()) <= 0
                && compareByX.compare(getSelf(), end) <= 0
                && compareByZ.compare(start, getSelf()) <= 0
                && compareByZ.compare(getSelf(), end) <= 0;
    }

    @Override
    default Iterator<T> iteratorTo(final T end) {
        final Comparator<T> compareByX = getXComparator();
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
                    current = current.add(Vec2D.UNIT_X);
                else
                    current = current.withXFrom(start).add(Vec2D.UNIT_Z);
                return current;
            }
        };
    }
}
