package com.runetide.common.domain;

import com.runetide.common.dto.Vec;
import com.runetide.common.dto.XZCoordinates;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class Vec2D implements Vec<Vec2D>, XZCoordinates<Vec2D> {
    public static final Vec2D IDENTITY = new Vec2D(0, 0);
    public static final Vec2D UNIT_X = new Vec2D(1, 0);
    public static final Vec2D UNIT_Z = new Vec2D(0, 1);
    public static final Vec2D UNIT_NEG_X = UNIT_X.negate();
    public static final Vec2D UNIT_NEG_Z = UNIT_Z.negate();

    protected final long x;
    protected final long z;

    public Vec2D(final long x, final long z) {
        this.x = x;
        this.z = z;
    }

    public long getX() {
        return x;
    }

    public long getZ() {
        return z;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Vec2D vec2D = (Vec2D) o;
        return x == vec2D.x && z == vec2D.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z);
    }

    @Override
    public Vec2D divide(final Vec2D vec) {
        return new Vec2D(x / vec.x, z / vec.z);
    }

    public Vec2D divide(final Vec3D vec) {
        return new Vec2D(x / vec.getX(), z / vec.getZ());
    }

    @Override
    public Vec2D modulo(final Vec2D vec) {
        return new Vec2D(x % vec.x, z % vec.z);
    }

    public Vec2D modulo(final Vec3D vec) {
        return new Vec2D(x % vec.getX(), z % vec.getZ());
    }

    @Override
    public Vec2D scale(final Vec2D vec) {
        return new Vec2D(x * vec.x, z * vec.z);
    }

    public Vec2D scale(final Vec3D vec) {
        return new Vec2D(x * vec.getX(), z * vec.getZ());
    }

    @Override
    public boolean isSameCoordinateSystem(final Vec2D other) {
        return true;
    }

    @Override
    public Vec2D add(final Vec2D vec) {
        return new Vec2D(x + vec.x, z + vec.z);
    }

    @Override
    public Vec2D subtract(final Vec2D other) {
        return add(other.negate());
    }

    @Override
    public Iterator<Vec2D> iteratorTo(final Vec2D end) {
        return new Iterator<>() {
            private Vec2D current = null;

            @Override
            public boolean hasNext() {
                return !Objects.equals(current, end);
            }

            @Override
            public Vec2D next() {
                if(!hasNext())
                    throw new NoSuchElementException();
                if(current == null)
                    current = Vec2D.this;
                else if(current.x < end.x)
                    current = current.add(Vec2D.UNIT_X);
                else
                    current = new Vec2D(x, current.z + 1);
                return current;
            }
        };
    }

    public Vec3D add(final Vec3D vec) {
        return new Vec3D(x + vec.getX(), vec.getY(), z + vec.getZ());
    }

    @Override
    public Vec2D negate() {
        return new Vec2D(-x, -z);
    }

    @Override
    public Vec2D withXFrom(final Vec2D other) {
        return new Vec2D(other.x, z);
    }

    @Override
    public Vec2D withZFrom(final Vec2D other) {
        return new Vec2D(x, other.z);
    }

    @Override
    public Comparator<Vec2D> getXComparator() {
        return Comparator.comparing(Vec2D::getX);
    }

    @Override
    public Comparator<Vec2D> getZComparator() {
        return Comparator.comparing(Vec2D::getZ);
    }

    @Override
    public Vec2D getSelf() {
        return this;
    }
}
