package com.runetide.common.domain;

import com.google.common.collect.ImmutableList;
import com.runetide.common.dto.Vec;
import com.runetide.common.dto.XZCoordinates;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Vec2D implements Vec<Vec2D>, XZCoordinates<Vec2D> {
    public static final Vec2D IDENTITY = new Vec2D(0, 0);
    public static final Vec2D UNIT_X = new Vec2D(1, 0);
    public static final Vec2D UNIT_Z = new Vec2D(0, 1);
    public static final Vec2D UNIT_NEG_X = UNIT_X.negate();
    public static final Vec2D UNIT_NEG_Z = UNIT_Z.negate();
    public static final List<Vec2D> AXIS = ImmutableList.of(UNIT_X, UNIT_Z);
    public static final List<Comparator<Vec2D>> COMPARATORS = ImmutableList.of(Comparator.comparing(Vec2D::getX),
            Comparator.comparing(Vec2D::getZ));

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

    @Override
    public List<Vec2D> axisVectors() {
        return AXIS;
    }

    public Vec2D modulo(final Vec3D vec) {
        return new Vec2D(x % vec.getX(), z % vec.getZ());
    }

    @Override
    public Vec2D scale(final Vec2D vec) {
        return new Vec2D(x * vec.x, z * vec.z);
    }

    @Override
    public Vec3D cross(final Vec2D vec) {
        return new Vec3D(0, crossLongLength(vec), 0);
    }

    @Override
    public long dot(final Vec2D vec) {
        return x * vec.x + z * vec.z;
    }

    @Override
    public long crossSquareLength(final Vec2D vec) {
        return crossLongLength(vec) * crossLongLength(vec);
    }

    @Override
    public double crossLength(final Vec2D vec) {
        return crossLongLength(vec);
    }

    public long crossLongLength(final Vec2D vec) {
        return z * vec.x - x * vec.z;
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
    public Comparator<Vec2D> compareByCoordinate(final int coordinate) {
        return COMPARATORS.get(coordinate);
    }

    @Override
    public Vec2D withCoordinateFrom(final Vec2D other, final int coordinate) {
        return new Vec2D(coordinate == COORDINATE_X ? other.x : x, coordinate == COORDINATE_Z ? other.z : z);
    }

    public Vec3D add(final Vec3D vec) {
        return new Vec3D(x + vec.getX(), vec.getY(), z + vec.getZ());
    }

    @Override
    public Vec2D negate() {
        return new Vec2D(-x, -z);
    }

    @Override
    public Vec2D getSelf() {
        return this;
    }

    @Override
    public String toString() {
        return "<Vec2D:" + x + "," + z + ">";
    }
}
