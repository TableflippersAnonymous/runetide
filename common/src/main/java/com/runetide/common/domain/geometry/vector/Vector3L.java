package com.runetide.common.domain.geometry.vector;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.runetide.common.domain.geometry.point.XYZCoordinates;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.OptionalInt;

@SuppressWarnings("UnstableApiUsage")
public class Vector3L implements FixedVector<Vector3L>, Vector3<Vector3L, Long>, XYZCoordinates<Vector3L> {
    private static final Vector3L[][][] CACHE = new Vector3L[256][256][256];
    private static final Interner<Vector3L> INTERNER = Interners.newWeakInterner();

    public static final Vector3L IDENTITY = of(0, 0, 0);
    public static final Vector3L UNIT_X = of(1, 0, 0);
    public static final Vector3L UNIT_Y = of(0, 1, 0);
    public static final Vector3L UNIT_Z = of(0, 0, 1);
    public static final Vector3L UNIT_NEG_X = UNIT_X.negate();
    public static final Vector3L UNIT_NEG_Y = UNIT_Y.negate();
    public static final Vector3L UNIT_NEG_Z = UNIT_Z.negate();
    public static final List<Vector3L> AXIS = ImmutableList.of(UNIT_X, UNIT_Z, UNIT_Y);
    public static final List<Comparator<Vector3L>> COMPARATORS = ImmutableList.of(Comparator.comparing(Vector3L::getX),
            Comparator.comparing(Vector3L::getZ), Comparator.comparing(Vector3L::getY));

    private final long x;
    private final long y;
    private final long z;

    @Nullable
    private transient Vector3L negatedCache;
    private final OptionalInt alignedAxis;

    public static Vector3L of(final long x, final long y, final long z) {
        final int cacheOffset = CACHE.length / 2;
        if(x < -cacheOffset || x >= cacheOffset || y < -cacheOffset || y >= cacheOffset
                || z < -cacheOffset || z >= cacheOffset)
            return INTERNER.intern(new Vector3L(x, y, z));
        if(CACHE[(int) x + cacheOffset][(int) y + cacheOffset][(int) z + cacheOffset] == null)
            CACHE[(int) x + cacheOffset][(int) y + cacheOffset][(int) z + cacheOffset] = INTERNER.intern(new Vector3L(x, y, z));
        return CACHE[(int) x + cacheOffset][(int) y + cacheOffset][(int) z + cacheOffset];
    }

    private Vector3L(final long x, final long y, final long z) {
        this.x = x;
        this.y = y;
        this.z = z;
        if(x != 0 && y == 0 && z == 0)
            this.alignedAxis = OptionalInt.of(COORDINATE_X);
        else if(x == 0 && y != 0 && z == 0)
            this.alignedAxis = OptionalInt.of(COORDINATE_Y);
        else if(x == 0 && y == 0 && z != 0)
            this.alignedAxis = OptionalInt.of(COORDINATE_Z);
        else
            this.alignedAxis = OptionalInt.empty();
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
        return of(Math.floorDiv(x, vec.x), Math.floorDiv(y, vec.y), Math.floorDiv(z, vec.z));
    }

    @Override
    public Vector3L divide(final Long scalar) {
        return of(Math.floorDiv(x, scalar), Math.floorDiv(y, scalar), Math.floorDiv(z, scalar));
    }

    @Override
    public Vector3L modulo(final Vector3L vec) {
        return of(Math.floorMod(x, vec.x), Math.floorMod(y, vec.y), Math.floorMod(z, vec.z));
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
        return of(x * vec.x, y * vec.y, z * vec.z);
    }

    @Override
    public Vector3L scale(final Long scalar) {
        return of(x * scalar, y * scalar, z * scalar);
    }

    @Override
    public Vector3L cross(final Vector3L vec) {
        return of(y * vec.z - z * vec.y, z * vec.x - x * vec.z, x * vec.y - y * vec.x);
    }

    @Override
    public Vector3F toFloat() {
        return Vector.of((double) x, (double) y, (double) z);
    }

    @Override
    public Vector3L toFixed() {
        return this;
    }

    @Override
    public Vector3L add(final Vector3L vec) {
        return of(x + vec.x, y + vec.y, z + vec.z);
    }

    @Override
    public Vector3L negate() {
        if(negatedCache == null) {
            negatedCache = of(-x, -y, -z);
            negatedCache.negatedCache = this;
        }
        return negatedCache;
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
        return of(coordinate == COORDINATE_X ? other.x : x, coordinate == COORDINATE_Y ? other.y : y,
                coordinate == COORDINATE_Z ? other.z : z);
    }

    @Override
    public OptionalInt getAlignedAxis() {
        return alignedAxis;
    }

    public Vector2L toVec2D() {
        return Vector.of(x, z);
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
