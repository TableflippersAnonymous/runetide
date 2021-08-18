package com.runetide.common.domain.geometry;

import com.google.common.collect.ImmutableList;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Vector3L implements FixedVector<Vector3L>, Vector3<Vector3L, Long>, XYZCoordinates<Vector3L> {
    public static final Vector3L IDENTITY = new Vector3L(0, 0, 0);
    public static final Vector3L UNIT_X = new Vector3L(1, 0, 0);
    public static final Vector3L UNIT_Y = new Vector3L(0, 1, 0);
    public static final Vector3L UNIT_Z = new Vector3L(0, 0, 1);
    public static final Vector3L UNIT_NEG_X = UNIT_X.negate();
    public static final Vector3L UNIT_NEG_Y = UNIT_Y.negate();
    public static final Vector3L UNIT_NEG_Z = UNIT_Z.negate();
    public static final List<Vector3L> AXIS = ImmutableList.of(UNIT_X, UNIT_Z, UNIT_Y);
    public static final List<Comparator<Vector3L>> COMPARATORS = ImmutableList.of(Comparator.comparing(Vector3L::getX),
            Comparator.comparing(Vector3L::getZ), Comparator.comparing(Vector3L::getY));

    private final long x;
    private final long y;
    private final long z;

    public Vector3L(final long x, final long y, final long z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Long getX() {
        return x;
    }

    public Long getY() {
        return y;
    }

    public Long getZ() {
        return z;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Vector3L vec3D = (Vector3L) o;
        return x == vec3D.x && y == vec3D.y && z == vec3D.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public Vector3L divide(final Vector3L vec) {
        return new Vector3L(x / vec.x, y / vec.y, z / vec.z);
    }

    @Override
    public Vector3L modulo(final Vector3L vec) {
        return new Vector3L(x % vec.x, y % vec.y, z % vec.z);
    }

    @Override
    public List<Vector3L> axisVectors() {
        return AXIS;
    }

    @Override
    public Long sum() {
        return x + y + z;
    }

    @Override
    public Long product() {
        return x * y * z;
    }

    @Override
    public Vector3L scale(final Vector3L vec) {
        return new Vector3L(x * vec.x, y * vec.y, z * vec.z);
    }

    @Override
    public Vector3L cross(final Vector3L vec) {
        return new Vector3L(y * vec.z - z * vec.y, z * vec.x - x * vec.z, x * vec.y - y * vec.x);
    }

    @Override
    public Vector3L add(final Vector3L vec) {
        return new Vector3L(x + vec.x, y + vec.y, z + vec.z);
    }

    @Override
    public Vector3L negate() {
        return new Vector3L(-x, -y, -z);
    }

    @Override
    public Vector3L subtract(final Vector3L other) {
        return add(other.negate());
    }

    @Override
    public Comparator<Vector3L> compareByCoordinate(final int coordinate) {
        return COMPARATORS.get(coordinate);
    }

    @Override
    public Vector3L withCoordinateFrom(final Vector3L other, final int coordinate) {
        return new Vector3L(coordinate == COORDINATE_X ? other.x : x, coordinate == COORDINATE_Y ? other.y : y,
                coordinate == COORDINATE_Z ? other.z : z);
    }

    public Vector2L toVec2D() {
        return new Vector2L(x, z);
    }

    @Override
    public Vector3L getSelf() {
        return this;
    }

    @Override
    public String toString() {
        return "<Vec3L:" + x + "," + y + "," + z + ">";
    }
}
