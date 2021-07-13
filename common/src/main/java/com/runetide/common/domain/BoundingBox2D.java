package com.runetide.common.domain;

import com.runetide.common.dto.ColumnRef;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class BoundingBox2D implements Iterable<ColumnRef> {
    private final ColumnRef start;
    private final ColumnRef end;

    public BoundingBox2D(final ColumnRef start, final ColumnRef end) {
        if(!start.getWorldRef().equals(end.getWorldRef()))
            throw new IllegalArgumentException("start/end must exist in same world.");
        this.start = start.minCoordinates(end);
        this.end = end.maxCoordinates(start);
    }

    public BoundingBox2D(final ColumnRef start, final Vec2D vec) {
        this(start, start.add(vec));
    }

    public ColumnRef getStart() {
        return start;
    }

    public ColumnRef getEnd() {
        return end;
    }

    public Vec2D getDimensions() {
        return end.subtract(start);
    }

    public BoundingBox2D move(final Vec2D direction) {
        return new BoundingBox2D(start.add(direction), end.add(direction));
    }

    public BoundingBox2D resize(final Vec2D direction) {
        return new BoundingBox2D(start.add(direction.negate()), end.add(direction));
    }

    public boolean contains(final ColumnRef column) {
        return ColumnRef.COMPARE_BY_X.compare(start, column) <= 0
                && ColumnRef.COMPARE_BY_Z.compare(start, column) <= 0
                && ColumnRef.COMPARE_BY_X.compare(column, end) <= 0
                && ColumnRef.COMPARE_BY_Z.compare(column, end) <= 0;
    }

    @Override
    public Iterator<ColumnRef> iterator() {
        return new Iterator<>() {
            private ColumnRef current = null;

            @Override
            public boolean hasNext() {
                return !Objects.equals(current, end);
            }

            @Override
            public ColumnRef next() {
                if(!hasNext())
                    throw new NoSuchElementException();
                if(current == null)
                    current = start;
                else if(ColumnRef.COMPARE_BY_X.compare(current, end) < 0)
                    current = current.add(Vec2D.UNIT_X);
                else
                    current = current.withXFrom(start).add(Vec2D.UNIT_Z);
                return current;
            }
        };
    }
}
