package com.runetide.common.domain.geometry;

import com.google.common.collect.ImmutableList;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Vector3D implements Vector<Vector3D>, XYZCoordinates<Vector3D> {
    public static final Vector3D IDENTITY = new Vector3D(0, 0, 0);
    public static final Vector3D UNIT_X = new Vector3D(1, 0, 0);
    public static final Vector3D UNIT_Y = new Vector3D(0, 1, 0);
    public static final Vector3D UNIT_Z = new Vector3D(0, 0, 1);
    public static final Vector3D UNIT_NEG_X = UNIT_X.negate();
    public static final Vector3D UNIT_NEG_Y = UNIT_Y.negate();
    public static final Vector3D UNIT_NEG_Z = UNIT_Z.negate();
    public static final List<Vector3D> AXIS = ImmutableList.of(UNIT_X, UNIT_Z, UNIT_Y);
    public static final List<Comparator<Vector3D>> COMPARATORS = ImmutableList.of(Comparator.comparing(Vector3D::getX),
            Comparator.comparing(Vector3D::getZ), Comparator.comparing(Vector3D::getY));

    private final long x;
    private final long y;
    private final long z;

    public Vector3D(final long x, final long y, final long z) {
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
        final Vector3D vec3D = (Vector3D) o;
        return x == vec3D.x && y == vec3D.y && z == vec3D.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public Vector3D divide(final Vector3D vec) {
        return new Vector3D(x / vec.x, y / vec.y, z / vec.z);
    }

    @Override
    public Vector3D modulo(final Vector3D vec) {
        return new Vector3D(x % vec.x, y % vec.y, z % vec.z);
    }

    @Override
    public List<Vector3D> axisVectors() {
        return AXIS;
    }

    @Override
    public long sum() {
        return x + y + z;
    }

    @Override
    public long product() {
        return x * y * z;
    }

    @Override
    public Vector3D scale(final Vector3D vec) {
        return new Vector3D(x * vec.x, y * vec.y, z * vec.z);
    }

    @Override
    public Vector3D cross(final Vector3D vec) {
        return new Vector3D(y * vec.z - z * vec.y, z * vec.x - x * vec.z, x * vec.y - y * vec.x);
    }

    @Override
    public long dot(final Vector3D vec) {
        return x * vec.x + y * vec.y + z * vec.z;
    }

    @Override
    public boolean isSameCoordinateSystem(final Vector3D other) {
        return true;
    }

    @Override
    public Vector3D add(final Vector3D vec) {
        return new Vector3D(x + vec.x, y + vec.y, z + vec.z);
    }

    @Override
    public Vector3D negate() {
        return new Vector3D(-x, -y, -z);
    }

    @Override
    public Vector3D subtract(final Vector3D other) {
        return add(other.negate());
    }

    @Override
    public Comparator<Vector3D> compareByCoordinate(final int coordinate) {
        return COMPARATORS.get(coordinate);
    }

    @Override
    public Vector3D withCoordinateFrom(final Vector3D other, final int coordinate) {
        return new Vector3D(coordinate == COORDINATE_X ? other.x : x, coordinate == COORDINATE_Y ? other.y : y,
                coordinate == COORDINATE_Z ? other.z : z);
    }

    public Vector2D toVec2D() {
        return new Vector2D(x, z);
    }

    @Override
    public Vector3D getSelf() {
        return this;
    }

    @Override
    public String toString() {
        return "<Vec3D:" + x + "," + y + "," + z + ">";
    }
}
