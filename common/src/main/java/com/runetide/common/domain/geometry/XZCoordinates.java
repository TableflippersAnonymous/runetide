package com.runetide.common.domain.geometry;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public interface XZCoordinates<Self extends XZCoordinates<Self>> extends BaseXZCoordinates<Self, Vec2D> {
    @Override
    default Self add(final long val) {
        return add(new Vec2D(val, val));
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
                    current = current.add(Vec2D.UNIT_X);
                else
                    current = current.withXFrom(start).add(Vec2D.UNIT_Z);
                return current;
            }
        };
    }
}
