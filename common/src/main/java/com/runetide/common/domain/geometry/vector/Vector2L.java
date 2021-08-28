package com.runetide.common.domain.geometry.vector;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.runetide.common.domain.geometry.point.XZCoordinates;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public class Vector2L implements FixedVector<Vector2L>, Vector2<Vector2L, Long>, XZCoordinates<Vector2L> {
    private static final Vector2L[][] CACHE = new Vector2L[200][200];
    private static final Interner<Vector2L> INTERNER = Interners.newWeakInterner();

    public static final Vector2L IDENTITY = of(0, 0);
    public static final Vector2L UNIT_X = of(1, 0);
    public static final Vector2L UNIT_Z = of(0, 1);
    public static final Vector2L UNIT_NEG_X = UNIT_X.negate();
    public static final Vector2L UNIT_NEG_Z = UNIT_Z.negate();
    public static final List<Vector2L> AXIS = ImmutableList.of(UNIT_X, UNIT_Z);
    public static final List<Comparator<Vector2L>> COMPARATORS = ImmutableList.of(Comparator.comparing(Vector2L::getX),
            Comparator.comparing(Vector2L::getZ));

    private final long x;
    private final long z;

    @Nullable
    private transient Vector2L negatedCache = null;

    /* Vector2L (and other geometry objects are immutable once constructed.  This means we can cache them and re-use
     * them without worry about someone mutating them.  To facilitate this, the Vector2L constructor is private, with
     * the static .of() method being used to obtain a vector.
     */
    public static Vector2L of(final long x, final long z) {
        final int cacheOffset = CACHE.length / 2;
        if(x < -cacheOffset || x >= cacheOffset || z < -cacheOffset || z >= cacheOffset)
            return INTERNER.intern(new Vector2L(x, z));
        if(CACHE[(int) x + cacheOffset][(int) z + cacheOffset] == null)
            CACHE[(int) x + cacheOffset][(int) z + cacheOffset] = INTERNER.intern(new Vector2L(x, z));
        return CACHE[(int) x + cacheOffset][(int) z + cacheOffset];
    }

    private Vector2L(final long x, final long z) {
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
    public Vector2F toFloat() {
        return Vector2F.of(x, z);
    }

    @Override
    public Vector2L toFixed() {
        return this;
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
        return of(x / vec.x, z / vec.z);
    }

    @Override
    public Vector2L divide(final Long scalar) {
        return of(x / scalar, z / scalar);
    }

    public Vector2L divide(final Vector3L vec) {
        return of(x / vec.getX(), z / vec.getZ());
    }

    @Override
    public Vector2L modulo(final Vector2L vec) {
        return of(x % vec.x, z % vec.z);
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
        return of(x % vec.getX(), z % vec.getZ());
    }

    @Override
    public Vector2L scale(final Vector2L vec) {
        return of(x * vec.x, z * vec.z);
    }

    @Override
    public Vector2L scale(final Long scalar) {
        return of(x * scalar, z * scalar);
    }

    @Override
    public Long crossSquareLength(final Vector2L vec) {
        return crossLongLength(vec) * crossLongLength(vec);
    }

    public long crossLongLength(final Vector2L vec) {
        return z * vec.x - x * vec.z;
    }

    public Vector2L scale(final Vector3L vec) {
        return of(x * vec.getX(), z * vec.getZ());
    }

    @Override
    public Vector2L add(final Vector2L vec) {
        return of(x + vec.x, z + vec.z);
    }

    @Override
    public Vector2L subtract(final Vector2L other) {
        return of(x - other.x, z - other.z);
    }

    @Override
    public Comparator<Vector2L> compareByCoordinate(final int coordinate) {
        return COMPARATORS.get(coordinate);
    }

    @Override
    public Vector2L withCoordinateFrom(final Vector2L other, final int coordinate) {
        return of(coordinate == COORDINATE_X ? other.x : x, coordinate == COORDINATE_Z ? other.z : z);
    }

    public Vector3L add(final Vector3L vec) {
        return Vector3L.of(x + vec.getX(), vec.getY(), z + vec.getZ());
    }

    public Vector3L toVec3D(final long y) {
        return Vector3L.of(x, y, z);
    }

    @Override
    public Vector2L negate() {
        if(negatedCache == null) {
            negatedCache = of(-x, -z);
            negatedCache.negatedCache = this;
        }
        return negatedCache;
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
