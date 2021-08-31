package com.runetide.common.domain.geometry.point;

import com.runetide.common.domain.geometry.vector.Vector;
import com.runetide.common.domain.geometry.vector.Vector3L;
import org.jetbrains.annotations.Contract;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;

public interface XYZCoordinates<Self extends XYZCoordinates<Self>> extends BaseXZCoordinates<Self, Vector3L> {
    @Override
    default Self add(final long val) {
        return add(Vector.of(val, val, val));
    }

    @Contract(pure = true)
    default Self withYFrom(final Self other) {
        return withCoordinateFrom(other, COORDINATE_Y);
    }

    @Contract(pure = true)
    default Comparator<Self> getYComparator() {
        return compareByCoordinate(COORDINATE_Y);
    }

    @Override
    default int coordinateSize() {
        return 3;
    }

    @Override
    default Iterator<Self> iteratorTo(final Self end) {
        final Comparator<Self> compareByX = getXComparator();
        final Comparator<Self> compareByZ = getZComparator();
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
                    current = current.add(Vector3L.UNIT_X);
                else if(compareByZ.compare(current, end) < 0)
                    current = current.withXFrom(start).add(Vector3L.UNIT_Z);
                else
                    current = start.withYFrom(current).add(Vector3L.UNIT_Y);
                return current;
            }
        };
    }

    /* Perf: Faster concrete methods */
    @Override
    default boolean anyCoordinateCompares(final Predicate<Integer> predicate, final Self other) {
        final Self self = getSelf();
        return predicate.test(getXComparator().compare(self, other))
                || predicate.test(getYComparator().compare(self, other))
                || predicate.test(getZComparator().compare(self, other));
    }

    @Override
    default boolean allCoordinatesCompare(final Predicate<Integer> predicate, final Self other) {
        final Self self = getSelf();
        return predicate.test(getXComparator().compare(self, other))
                && predicate.test(getYComparator().compare(self, other))
                && predicate.test(getZComparator().compare(self, other));
    }

    @Override
    default boolean isBetween(final Self start, final Self end) {
        final Comparator<Self> xComparator = getXComparator();
        final Comparator<Self> yComparator = getYComparator();
        final Comparator<Self> zComparator = getZComparator();
        final Self self = getSelf();
        return xComparator.compare(start, self) <= 0
                && xComparator.compare(self, end) <= 0
                && yComparator.compare(start, self) <= 0
                && yComparator.compare(self, end) <= 0
                && zComparator.compare(start, self) <= 0
                && zComparator.compare(self, end) <= 0;
    }
}
