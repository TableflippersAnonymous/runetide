package com.runetide.common.domain;

import com.runetide.common.dto.BlockRef;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class BoundingBox3D implements Iterable<BlockRef> {
    private final BlockRef start;
    private final BlockRef end;

    public BoundingBox3D(final BlockRef start, final BlockRef end) {
        if(!start.getWorldRef().equals(end.getWorldRef()))
            throw new IllegalArgumentException("start/end must exist in same world.");
        this.start = start.minCoordinates(end);
        this.end = end.maxCoordinates(start);
    }

    public BoundingBox3D(final BlockRef start, final Vec3D vec) {
        this(start, start.add(vec));
    }

    public BlockRef getStart() {
        return start;
    }

    public BlockRef getEnd() {
        return end;
    }

    public Vec3D getDimensions() {
        return end.subtract(start);
    }

    public BoundingBox3D move(final Vec3D direction) {
        return new BoundingBox3D(start.add(direction), end.add(direction));
    }

    public BoundingBox3D resize(final Vec3D direction) {
        return new BoundingBox3D(start.add(direction.negate()), end.add(direction));
    }

    public boolean contains(final BlockRef block) {
        return BlockRef.COMPARE_BY_X.compare(start, block) <= 0
                && BlockRef.COMPARE_BY_Y.compare(start, block) <= 0
                && BlockRef.COMPARE_BY_Z.compare(start, block) <= 0
                && BlockRef.COMPARE_BY_X.compare(block, end) <= 0
                && BlockRef.COMPARE_BY_Y.compare(block, end) <= 0
                && BlockRef.COMPARE_BY_Z.compare(block, end) <= 0;
    }

    @Override
    public Iterator<BlockRef> iterator() {
        return new Iterator<>() {
            private BlockRef current = null;

            @Override
            public boolean hasNext() {
                return !Objects.equals(current, end);
            }

            @Override
            public BlockRef next() {
                if(!hasNext())
                    throw new NoSuchElementException();
                if(current == null)
                    current = start;
                else if(BlockRef.COMPARE_BY_X.compare(current, end) < 0)
                    current = current.add(Vec3D.UNIT_X);
                else if(BlockRef.COMPARE_BY_Z.compare(current, end) < 0)
                    current = current.withXFrom(start).add(Vec3D.UNIT_Z);
                else
                    current = current.withXFrom(start).withZFrom(start).add(Vec3D.UNIT_Y);
                return current;
            }
        };
    }

    public BoundingBox2D boundingBox2D() {
        return new BoundingBox2D(start.column(), end.column());
    }
}
