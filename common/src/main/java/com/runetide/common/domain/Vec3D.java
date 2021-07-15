package com.runetide.common.domain;

import com.runetide.common.dto.Vec;
import com.runetide.common.dto.XYZCoordinates;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class Vec3D implements Vec<Vec3D>, XYZCoordinates<Vec3D> {
    public static final Vec3D IDENTITY = new Vec3D(0, 0, 0);
    public static final Vec3D UNIT_X = new Vec3D(1, 0, 0);
    public static final Vec3D UNIT_Y = new Vec3D(0, 1, 0);
    public static final Vec3D UNIT_Z = new Vec3D(0, 0, 1);
    public static final Vec3D UNIT_NEG_X = UNIT_X.negate();
    public static final Vec3D UNIT_NEG_Y = UNIT_Y.negate();
    public static final Vec3D UNIT_NEG_Z = UNIT_Z.negate();

    private final long x;
    private final long y;
    private final long z;

    public Vec3D(final long x, final long y, final long z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public long getX() {
        return x;
    }

    public long getY() {
        return y;
    }

    public long getZ() {
        return z;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        final Vec3D vec3D = (Vec3D) o;
        return y == vec3D.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), y);
    }

    @Override
    public Vec3D divide(final Vec3D vec) {
        return new Vec3D(x / vec.x, y / vec.y, z / vec.z);
    }

    @Override
    public Vec3D modulo(final Vec3D vec) {
        return new Vec3D(x % vec.x, y % vec.y, z % vec.z);
    }

    @Override
    public Vec3D scale(final Vec3D vec) {
        return new Vec3D(x * vec.x, y * vec.y, z * vec.z);
    }

    @Override
    public boolean isSameCoordinateSystem(final Vec3D other) {
        return true;
    }

    @Override
    public Vec3D add(final Vec3D vec) {
        return new Vec3D(x + vec.x, y + vec.y, z + vec.z);
    }

    @Override
    public Vec3D negate() {
        return new Vec3D(-x, -y, -z);
    }

    @Override
    public Vec3D subtract(final Vec3D other) {
        return add(other.negate());
    }

    @Override
    public Iterator<Vec3D> iteratorTo(final Vec3D end) {
        return new Iterator<>() {
            private Vec3D current = null;

            @Override
            public boolean hasNext() {
                return !Objects.equals(current, end);
            }

            @Override
            public Vec3D next() {
                if(!hasNext())
                    throw new NoSuchElementException();
                if(current == null)
                    current = Vec3D.this;
                else if(current.x < end.x)
                    current = current.add(Vec3D.UNIT_X);
                else if(current.z < end.z)
                    current = new Vec3D(x, current.y, current.z + 1);
                else
                    current = new Vec3D(x, current.y + 1, z);
                return current;
            }
        };
    }

    public Vec2D toVec2D() {
        return new Vec2D(x, z);
    }

    @Override
    public Vec3D withXFrom(final Vec3D other) {
        return new Vec3D(other.x, y, z);
    }

    @Override
    public Vec3D withYFrom(final Vec3D other) {
        return new Vec3D(x, other.y, z);
    }

    @Override
    public Vec3D withZFrom(final Vec3D other) {
        return new Vec3D(x, y, other.z);
    }

    @Override
    public Comparator<Vec3D> getXComparator() {
        return Comparator.comparing(Vec3D::getX);
    }

    @Override
    public Comparator<Vec3D> getYComparator() {
        return Comparator.comparing(Vec3D::getY);
    }

    @Override
    public Comparator<Vec3D> getZComparator() {
        return Comparator.comparing(Vec3D::getZ);
    }

    @Override
    public Vec3D getSelf() {
        return this;
    }
}
