package com.runetide.common.domain.geometry;

import com.google.common.collect.ImmutableList;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Vector2D implements Vector<Vector2D>, XZCoordinates<Vector2D> {
    public static final Vector2D IDENTITY = new Vector2D(0, 0);
    public static final Vector2D UNIT_X = new Vector2D(1, 0);
    public static final Vector2D UNIT_Z = new Vector2D(0, 1);
    public static final Vector2D UNIT_NEG_X = UNIT_X.negate();
    public static final Vector2D UNIT_NEG_Z = UNIT_Z.negate();
    public static final List<Vector2D> AXIS = ImmutableList.of(UNIT_X, UNIT_Z);
    public static final List<Comparator<Vector2D>> COMPARATORS = ImmutableList.of(Comparator.comparing(Vector2D::getX),
            Comparator.comparing(Vector2D::getZ));

    protected final long x;
    protected final long z;

    public Vector2D(final long x, final long z) {
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
        final Vector2D vec2D = (Vector2D) o;
        return x == vec2D.x && z == vec2D.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z);
    }

    @Override
    public Vector2D divide(final Vector2D vec) {
        return new Vector2D(x / vec.x, z / vec.z);
    }

    public Vector2D divide(final Vector3D vec) {
        return new Vector2D(x / vec.getX(), z / vec.getZ());
    }

    @Override
    public Vector2D modulo(final Vector2D vec) {
        return new Vector2D(x % vec.x, z % vec.z);
    }

    @Override
    public List<Vector2D> axisVectors() {
        return AXIS;
    }

    @Override
    public long sum() {
        return x + z;
    }

    @Override
    public long product() {
        return x * z;
    }

    public Vector2D modulo(final Vector3D vec) {
        return new Vector2D(x % vec.getX(), z % vec.getZ());
    }

    @Override
    public Vector2D scale(final Vector2D vec) {
        return new Vector2D(x * vec.x, z * vec.z);
    }

    @Override
    public Vector3D cross(final Vector2D vec) {
        return new Vector3D(0, crossLongLength(vec), 0);
    }

    @Override
    public long dot(final Vector2D vec) {
        return x * vec.x + z * vec.z;
    }

    @Override
    public long crossSquareLength(final Vector2D vec) {
        return crossLongLength(vec) * crossLongLength(vec);
    }

    @Override
    public double crossLength(final Vector2D vec) {
        return crossLongLength(vec);
    }

    public long crossLongLength(final Vector2D vec) {
        return z * vec.x - x * vec.z;
    }

    public Vector2D scale(final Vector3D vec) {
        return new Vector2D(x * vec.getX(), z * vec.getZ());
    }

    @Override
    public boolean isSameCoordinateSystem(final Vector2D other) {
        return true;
    }

    @Override
    public Vector2D add(final Vector2D vec) {
        return new Vector2D(x + vec.x, z + vec.z);
    }

    @Override
    public Vector2D subtract(final Vector2D other) {
        return add(other.negate());
    }

    @Override
    public Comparator<Vector2D> compareByCoordinate(final int coordinate) {
        return COMPARATORS.get(coordinate);
    }

    @Override
    public Vector2D withCoordinateFrom(final Vector2D other, final int coordinate) {
        return new Vector2D(coordinate == COORDINATE_X ? other.x : x, coordinate == COORDINATE_Z ? other.z : z);
    }

    public Vector3D add(final Vector3D vec) {
        return new Vector3D(x + vec.getX(), vec.getY(), z + vec.getZ());
    }

    public Vector3D toVec3D(final long y) {
        return new Vector3D(x, y, z);
    }

    @Override
    public Vector2D negate() {
        return new Vector2D(-x, -z);
    }

    @Override
    public Vector2D getSelf() {
        return this;
    }

    @Override
    public String toString() {
        return "<Vec2D:" + x + "," + z + ">";
    }
}
