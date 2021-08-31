package com.runetide.common.domain.geometry.point;

import com.runetide.common.domain.geometry.vector.Vector;
import com.runetide.common.domain.geometry.vector.Vector2L;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;

public interface XZCoordinates<Self extends XZCoordinates<Self>> extends BaseXZCoordinates<Self, Vector2L> {
    @Override
    default Self add(final long val) {
        return add(Vector.of(val, val));
    }

    @Override
    default int coordinateSize() {
        return 2;
    }

    @Override
    default Iterator<Self> iteratorTo(final Self end) {
        final Comparator<Self> compareByX = getXComparator();
        final Self start = getSelf();
        return new Iterator<>() {
            private Self current = null;

            @Override
            public boolean hasNext() {
                return !Objects.equals(current, end);
            }

            @Override
            public Self next() {
                if(!hasNext())
                    throw new NoSuchElementException();
                if(current == null)
                    current = start;
                else if(compareByX.compare(current, end) < 0)
                    current = current.add(Vector2L.UNIT_X);
                else
                    current = current.withXFrom(start).add(Vector2L.UNIT_Z);
                return current;
            }
        };
    }

    /* Perf: Faster concrete methods */
    @Override
    default boolean anyCoordinateCompares(final Predicate<Integer> predicate, final Self other) {
        final Self self = getSelf();
        return predicate.test(getXComparator().compare(self, other))
                || predicate.test(getZComparator().compare(self, other));
    }

    @Override
    default boolean allCoordinatesCompare(final Predicate<Integer> predicate, final Self other) {
        final Self self = getSelf();
        return predicate.test(getXComparator().compare(self, other))
                && predicate.test(getZComparator().compare(self, other));
    }

    @Override
    default boolean isBetween(final Self start, final Self end) {
        final Comparator<Self> xComparator = getXComparator();
        final Comparator<Self> zComparator = getZComparator();
        final Self self = getSelf();
        return xComparator.compare(start, self) <= 0
                && xComparator.compare(self, end) <= 0
                && zComparator.compare(start, self) <= 0
                && zComparator.compare(self, end) <= 0;
    }
}
