package com.runetide.common.domain.geometry;

import com.google.common.collect.ImmutableList;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Vector2F implements FloatVector<Vector2F>, Vector2<Vector2F, Double> {
    public static final Vector2F IDENTITY = of(0, 0);
    public static final Vector2F UNIT_X = of(1, 0);
    public static final Vector2F UNIT_Z = of(0, 1);
    public static final Vector2F UNIT_NEG_X = UNIT_X.negate();
    public static final Vector2F UNIT_NEG_Z = UNIT_Z.negate();
    public static final List<Vector2F> AXIS = ImmutableList.of(UNIT_X, UNIT_Z);
    public static final List<Comparator<Vector2F>> COMPARATORS = ImmutableList.of(Comparator.comparing(Vector2F::getX),
            Comparator.comparing(Vector2F::getZ));

    private final double x;
    private final double z;

    public static Vector2F of(final double x, final double z) {
        return new Vector2F(x, z);
    }

    private Vector2F(final double x, final double z) {
        this.x = x;
        this.z = z;
    }

    @Override
    public Vector2F getSelf() {
        return this;
    }

    @Override
    public Vector2F add(final Vector2F other) {
        return of(x + other.x, z + other.z);
    }

    @Override
    public Vector2F subtract(final Vector2F other) {
        return of(x - other.x, z - other.z);
    }

    @Override
    public Comparator<Vector2F> compareByCoordinate(final int coordinate) {
        return COMPARATORS.get(coordinate);
    }

    @Override
    public int coordinateSize() {
        return 2;
    }

    @Override
    public Vector2F withCoordinateFrom(final Vector2F other, final int coordinate) {
        return of(coordinate == COORDINATE_X ? other.x : x, coordinate == COORDINATE_Z ? other.z : z);
    }

    @Override
    public Vector2F negate() {
        return of(-x, -z);
    }

    @Override
    public Vector2F scale(final Vector2F other) {
        return of(x * other.x, z * other.z);
    }

    @Override
    public Vector2F divide(final Vector2F other) {
        return of(x / other.x, z / other.z);
    }

    @Override
    public Vector2F modulo(final Vector2F other) {
        return of(x % other.x, z % other.z);
    }

    @Override
    public List<Vector2F> axisVectors() {
        return AXIS;
    }

    @Override
    public Double sum() {
        return x + z;
    }

    @Override
    public Double product() {
        return x * z;
    }

    @Override
    public Double crossSquareLength(final Vector2F other) {
        return crossLength(other) * crossLength(other);
    }

    public double crossLength(final Vector2F vec) {
        return z * vec.x - x * vec.z;
    }

    @Override
    public Double getX() {
        return x;
    }

    @Override
    public Double getZ() {
        return z;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Vector2F vector2F = (Vector2F) o;
        return Double.compare(vector2F.x, x) == 0 && Double.compare(vector2F.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z);
    }

    @Override
    public String toString() {
        return "<Vec2F:" + x + "," + z + ">";
    }
}
