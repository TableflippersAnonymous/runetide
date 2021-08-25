package com.runetide.common.domain.geometry;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;

import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public class Matrix3L implements SquareMatrix<Matrix3L, Vector3L, Long> {
    private static final Interner<Matrix3L> INTERNER = Interners.newWeakInterner();

    public static final Matrix3L IDENTITY = of(Vector3L.of(1, 0, 0), Vector3L.of(0, 1, 0),
            Vector3L.of(0, 0, 1));

    private final Vector3L columnA;
    private final Vector3L columnB;
    private final Vector3L columnC;

    public static Matrix3L of(final Vector3L columnA, final Vector3L columnB, final Vector3L columnC) {
        return INTERNER.intern(new Matrix3L(columnA, columnB, columnC));
    }

    private Matrix3L(final Vector3L columnA, final Vector3L columnB, final Vector3L columnC) {
        this.columnA = Objects.requireNonNull(columnA);
        this.columnB = Objects.requireNonNull(columnB);
        this.columnC = Objects.requireNonNull(columnC);
    }

    @Override
    public Matrix3L getSelf() {
        return this;
    }

    @Override
    public Matrix3L add(final Matrix3L other) {
        return of(columnA.add(other.columnA), columnB.add(other.columnB), columnC.add(other.columnC));
    }

    @Override
    public Matrix3L multiply(final Matrix3L other) {
        return of(multiply(other.columnA), multiply(other.columnB), multiply(other.columnC));
    }

    @Override
    public Matrix3L multiply(final Long scalar) {
        return of(columnA.scale(scalar), columnB.scale(scalar), columnC.scale(scalar));
    }

    @Override
    public Matrix3L transpose() {
        return of(Vector3L.of(columnA.getX(), columnB.getX(), columnC.getX()),
                Vector3L.of(columnA.getY(), columnB.getY(), columnC.getY()),
                Vector3L.of(columnA.getZ(), columnB.getZ(), columnC.getZ()));
    }

    @Override
    public Matrix3L rotateRight() {
        return of(columnC, columnA, columnB);
    }

    @Override
    public Matrix3L rotateLeft() {
        return of(columnB, columnC, columnA);
    }

    @Override
    public Matrix3L negate() {
        return of(columnA.negate(), columnB.negate(), columnC.negate());
    }

    @Override
    public Vector3L multiply(final Vector3L other) {
        final Matrix3L transposed = transpose();
        return Vector3L.of(other.dot(transposed.columnA), other.dot(transposed.columnB), other.dot(transposed.columnC));
    }

    @Override
    public Vector3L diagonalVector() {
        return Vector3L.of(columnA.getX(), columnB.getY(), columnC.getZ());
    }

    @Override
    public Vector3L antidiagonalVector() {
        return Vector3L.of(columnC.getX(), columnB.getY(), columnA.getZ());
    }

    @Override
    public boolean isUpperTriangular() {
        return columnA.getY() == 0 && columnA.getZ() == 0 && columnB.getZ() == 0;
    }

    @Override
    public Matrix3L upperTriangularize() {
        return of(Vector3L.of(columnA.getX(), 0, 0), Vector3L.of(columnB.getX(), columnB.getY(), 0),
                columnC);
    }

    @Override
    public boolean isLowerTriangular() {
        return columnB.getX() == 0 && columnC.getX() == 0 && columnC.getY() == 0;
    }

    @Override
    public Matrix3L lowerTriangularize() {
        return of(columnA, Vector3L.of(0, columnB.getY(), columnB.getZ()),
                Vector3L.of(0, 0, columnC.getZ()));
    }

    @Override
    public Matrix3L identity() {
        return IDENTITY;
    }

    @Override
    public Long determinant() {
        final Matrix3L rotateRight = rotateRight();
        final Matrix3L rotateLeft = rotateLeft();
        return diagonalVector().product() - antidiagonalVector().product()
                + rotateLeft.diagonalVector().product() - rotateLeft.antidiagonalVector().product()
                + rotateRight.diagonalVector().product() - rotateRight.antidiagonalVector().product();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Matrix3L matrix3L = (Matrix3L) o;
        return Objects.equals(columnA, matrix3L.columnA) && Objects.equals(columnB, matrix3L.columnB)
                && Objects.equals(columnC, matrix3L.columnC);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnA, columnB, columnC);
    }

    @Override
    public String toString() {
        return "<Matrix3L:" + columnA + "," + columnB + "," + columnC + ">";
    }
}
