package com.runetide.common.domain.geometry;

import com.google.common.collect.ImmutableList;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Vector2L implements FixedVector<Vector2L>, Vector2<Vector2L, Long>, XZCoordinates<Vector2L> {
    public static final Vector2L IDENTITY = new Vector2L(0, 0);
    public static final Vector2L UNIT_X = new Vector2L(1, 0);
    public static final Vector2L UNIT_Z = new Vector2L(0, 1);
    public static final Vector2L UNIT_NEG_X = UNIT_X.negate();
    public static final Vector2L UNIT_NEG_Z = UNIT_Z.negate();
    public static final List<Vector2L> AXIS = ImmutableList.of(UNIT_X, UNIT_Z);
    public static final List<Comparator<Vector2L>> COMPARATORS = ImmutableList.of(Comparator.comparing(Vector2L::getX),
            Comparator.comparing(Vector2L::getZ));

    protected final long x;
    protected final long z;

    public Vector2L(final long x, final long z) {
        this.x = x;
        this.z = z;
    }

    public Long getX() {
        return x;
    }

    public Long getZ() {
        return z;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Vector2L vec2D = (Vector2L) o;
        return x == vec2D.x && z == vec2D.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z);
    }

    @Override
    public Vector2L divide(final Vector2L vec) {
        return new Vector2L(x / vec.x, z / vec.z);
    }

    public Vector2L divide(final Vector3L vec) {
        return new Vector2L(x / vec.getX(), z / vec.getZ());
    }

    @Override
    public Vector2L modulo(final Vector2L vec) {
        return new Vector2L(x % vec.x, z % vec.z);
    }

    @Override
    public List<Vector2L> axisVectors() {
        return AXIS;
    }

    @Override
    public Long sum() {
        return x + z;
    }

    @Override
    public Long product() {
        return x * z;
    }

    public Vector2L modulo(final Vector3L vec) {
        return new Vector2L(x % vec.getX(), z % vec.getZ());
    }

    @Override
    public Vector2L scale(final Vector2L vec) {
        return new Vector2L(x * vec.x, z * vec.z);
    }

    @Override
    public Long crossSquareLength(final Vector2L vec) {
        return crossLongLength(vec) * crossLongLength(vec);
    }

    public long crossLongLength(final Vector2L vec) {
        return z * vec.x - x * vec.z;
    }

    public Vector2L scale(final Vector3L vec) {
        return new Vector2L(x * vec.getX(), z * vec.getZ());
    }

    @Override
    public Vector2L add(final Vector2L vec) {
        return new Vector2L(x + vec.x, z + vec.z);
    }

    @Override
    public Vector2L subtract(final Vector2L other) {
        return new Vector2L(x - other.x, z - other.z);
    }

    @Override
    public Comparator<Vector2L> compareByCoordinate(final int coordinate) {
        return COMPARATORS.get(coordinate);
    }

    @Override
    public Vector2L withCoordinateFrom(final Vector2L other, final int coordinate) {
        return new Vector2L(coordinate == COORDINATE_X ? other.x : x, coordinate == COORDINATE_Z ? other.z : z);
    }

    public Vector3L add(final Vector3L vec) {
        return new Vector3L(x + vec.getX(), vec.getY(), z + vec.getZ());
    }

    public Vector3L toVec3D(final long y) {
        return new Vector3L(x, y, z);
    }

    @Override
    public Vector2L negate() {
        return new Vector2L(-x, -z);
    }

    @Override
    public Vector2L getSelf() {
        return this;
    }

    @Override
    public String toString() {
        return "<Vec2L:" + x + "," + z + ">";
    }
}
