package com.runetide.common.domain;

import com.google.common.collect.ImmutableList;
import com.runetide.common.dto.Vec;
import com.runetide.common.dto.XYZCoordinates;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Vec3D implements Vec<Vec3D>, XYZCoordinates<Vec3D> {
    public static final Vec3D IDENTITY = new Vec3D(0, 0, 0);
    public static final Vec3D UNIT_X = new Vec3D(1, 0, 0);
    public static final Vec3D UNIT_Y = new Vec3D(0, 1, 0);
    public static final Vec3D UNIT_Z = new Vec3D(0, 0, 1);
    public static final Vec3D UNIT_NEG_X = UNIT_X.negate();
    public static final Vec3D UNIT_NEG_Y = UNIT_Y.negate();
    public static final Vec3D UNIT_NEG_Z = UNIT_Z.negate();
    public static final List<Vec3D> AXIS = ImmutableList.of(UNIT_X, UNIT_Z, UNIT_Y);
    public static final List<Comparator<Vec3D>> COMPARATORS = ImmutableList.of(Comparator.comparing(Vec3D::getX),
            Comparator.comparing(Vec3D::getZ), Comparator.comparing(Vec3D::getY));

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
    public List<Vec3D> axisVectors() {
        return AXIS;
    }

    @Override
    public Vec3D scale(final Vec3D vec) {
        return new Vec3D(x * vec.x, y * vec.y, z * vec.z);
    }

    @Override
    public Vec3D cross(final Vec3D vec) {
        return new Vec3D(y * vec.z - z * vec.y, z * vec.x - x * vec.z, x * vec.y - y * vec.x);
    }

    @Override
    public long dot(final Vec3D vec) {
        return x * vec.x + y * vec.y + z * vec.z;
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
    public Comparator<Vec3D> compareByCoordinate(final int coordinate) {
        return COMPARATORS.get(coordinate);
    }

    @Override
    public Vec3D withCoordinateFrom(final Vec3D other, final int coordinate) {
        return new Vec3D(coordinate == COORDINATE_X ? other.x : x, coordinate == COORDINATE_Y ? other.y : y,
                coordinate == COORDINATE_Z ? other.z : z);
    }

    public Vec2D toVec2D() {
        return new Vec2D(x, z);
    }

    @Override
    public Vec3D getSelf() {
        return this;
    }
}
