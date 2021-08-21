package com.runetide.common.domain.geometry;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public interface XZCoordinates<Self extends XZCoordinates<Self>> extends BaseXZCoordinates<Self, Vector2L> {
    @Override
    default Self add(final long val) {
        return add(Vector2L.of(val, val));
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
}
