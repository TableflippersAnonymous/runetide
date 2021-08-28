package com.runetide.common.domain.geometry.matrix;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.runetide.common.domain.geometry.vector.Vector2L;

import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public class Matrix2L implements SquareMatrix<Matrix2L, Vector2L, Long> {
    private static final Interner<Matrix2L> INTERNER = Interners.newWeakInterner();

    public static final Matrix2L IDENTITY = of(Vector2L.of(1, 0), Vector2L.of(0, 1));

    private final Vector2L columnA;
    private final Vector2L columnB;

    public static Matrix2L of(final Vector2L columnA, final Vector2L columnB) {
        return INTERNER.intern(new Matrix2L(columnA, columnB));
    }

    private Matrix2L(final Vector2L columnA, final Vector2L columnB) {
        this.columnA = Objects.requireNonNull(columnA);
        this.columnB = Objects.requireNonNull(columnB);
    }

    @Override
    public Matrix2L getSelf() {
        return this;
    }

    @Override
    public Matrix2L add(final Matrix2L other) {
        return of(columnA.add(other.columnA), columnB.add(other.columnB));
    }

    @Override
    public Matrix2L multiply(final Matrix2L other) {
        return of(multiply(other.columnA), multiply(other.columnB));
    }

    @Override
    public Matrix2L multiply(final Long scalar) {
        return of(columnA.scale(scalar), columnB.scale(scalar));
    }

    @Override
    public Matrix2L transpose() {
        return of(Vector2L.of(columnA.getX(), columnB.getX()), Vector2L.of(columnA.getZ(), columnB.getZ()));
    }

    @Override
    public Matrix2L rotateRight() {
        return of(columnB, columnA);
    }

    @Override
    public Matrix2L rotateLeft() {
        /* We only have two columns, so either direction just swaps them */
        return rotateRight();
    }

    @Override
    public Matrix2L negate() {
        return of(columnA.negate(), columnB.negate());
    }

    @Override
    public Vector2L multiply(final Vector2L other) {
        final Matrix2L transposed = transpose();
        return Vector2L.of(other.dot(transposed.columnA), other.dot(transposed.columnB));
    }

    @Override
    public Vector2L diagonalVector() {
        return Vector2L.of(columnA.getX(), columnB.getZ());
    }

    @Override
    public Vector2L antidiagonalVector() {
        return Vector2L.of(columnB.getX(), columnA.getZ());
    }

    @Override
    public boolean isUpperTriangular() {
        return columnA.getZ() == 0;
    }

    @Override
    public Matrix2L upperTriangularize() {
        return of(Vector2L.of(columnA.getX(), 0), columnB);
    }

    @Override
    public boolean isLowerTriangular() {
        return columnB.getX() == 0;
    }

    @Override
    public Matrix2L lowerTriangularize() {
        return of(columnA, Vector2L.of(0, columnB.getZ()));
    }

    @Override
    public Matrix2L identity() {
        return IDENTITY;
    }

    @Override
    public Long determinant() {
        return columnA.getX() * columnB.getZ() - columnB.getX() * columnA.getZ();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Matrix2L matrix2L = (Matrix2L) o;
        return Objects.equals(columnA, matrix2L.columnA) && Objects.equals(columnB, matrix2L.columnB);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnA, columnB);
    }

    @Override
    public String toString() {
        return "<Matrix2L:" + columnA + "," + columnB + ">";
    }
}
