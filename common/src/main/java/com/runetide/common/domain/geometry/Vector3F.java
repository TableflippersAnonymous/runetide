package com.runetide.common.domain.geometry;

import com.google.common.collect.ImmutableList;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Vector3F implements FloatVector<Vector3F>, Vector3<Vector3F, Double> {
    public static final Vector3F IDENTITY = new Vector3F(0, 0, 0);
    public static final Vector3F UNIT_X = new Vector3F(1, 0, 0);
    public static final Vector3F UNIT_Y = new Vector3F(0, 1, 0);
    public static final Vector3F UNIT_Z = new Vector3F(0, 0, 1);
    public static final Vector3F UNIT_NEG_X = UNIT_X.negate();
    public static final Vector3F UNIT_NEG_Y = UNIT_Y.negate();
    public static final Vector3F UNIT_NEG_Z = UNIT_Z.negate();
    public static final List<Vector3F> AXIS = ImmutableList.of(UNIT_X, UNIT_Z, UNIT_Y);
    public static final List<Comparator<Vector3F>> COMPARATORS = ImmutableList.of(Comparator.comparing(Vector3F::getX),
            Comparator.comparing(Vector3F::getZ), Comparator.comparing(Vector3F::getY));

    private final double x;
    private final double y;
    private final double z;

    public Vector3F(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public Vector3F getSelf() {
        return this;
    }

    @Override
    public Vector3F add(final Vector3F other) {
        return new Vector3F(x + other.x, y + other.y, z + other.z);
    }

    @Override
    public Vector3F subtract(final Vector3F other) {
        return new Vector3F(x - other.x, y - other.y, z - other.y);
    }

    @Override
    public Comparator<Vector3F> compareByCoordinate(final int coordinate) {
        return COMPARATORS.get(coordinate);
    }

    @Override
    public int coordinateSize() {
        return 3;
    }

    @Override
    public Vector3F withCoordinateFrom(final Vector3F other, final int coordinate) {
        return new Vector3F(coordinate == COORDINATE_X ? other.x : x, coordinate == COORDINATE_Y ? other.y : y,
                coordinate == COORDINATE_Z ? other.z : z);
    }

    @Override
    public Vector3F negate() {
        return new Vector3F(-x, -y, -z);
    }

    @Override
    public Vector3F scale(final Vector3F other) {
        return new Vector3F(x * other.x, y * other.y, z * other.z);
    }

    @Override
    public Vector3F divide(final Vector3F other) {
        return new Vector3F(x / other.x, y / other.y, z / other.z);
    }

    @Override
    public Vector3F modulo(final Vector3F other) {
        return new Vector3F(x % other.x, y % other.y, z % other.z);
    }

    @Override
    public List<Vector3F> axisVectors() {
        return AXIS;
    }

    @Override
    public Double sum() {
        return x + y + z;
    }

    @Override
    public Double product() {
        return x * y * z;
    }

    @Override
    public Double getX() {
        return x;
    }

    @Override
    public Double getY() {
        return y;
    }

    @Override
    public Double getZ() {
        return z;
    }

    @Override
    public Vector3F cross(final Vector3F other) {
        return new Vector3F(y * other.z - z * other.y, z * other.x - x * other.z, x * other.y - y * other.x);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Vector3F vector3F = (Vector3F) o;
        return Double.compare(vector3F.x, x) == 0 && Double.compare(vector3F.y, y) == 0 && Double.compare(vector3F.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return "<Vec3F:" + x + "," + y + "," + z + ">";
    }
}
