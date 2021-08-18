package com.runetide.common.domain.geometry;

import java.util.Objects;

public class Matrix2L implements SquareMatrix<Matrix2L, Vector2L, Long> {
    public static final Matrix2L IDENTITY = new Matrix2L(new Vector2L(1, 0), new Vector2L(0, 1));

    private final Vector2L columnA;
    private final Vector2L columnB;

    public Matrix2L(final Vector2L columnA, final Vector2L columnB) {
        this.columnA = Objects.requireNonNull(columnA);
        this.columnB = Objects.requireNonNull(columnB);
    }

    @Override
    public Matrix2L getSelf() {
        return this;
    }

    @Override
    public Matrix2L add(final Matrix2L other) {
        return new Matrix2L(columnA.add(other.columnA), columnB.add(other.columnB));
    }

    @Override
    public Matrix2L multiply(final Matrix2L other) {
        return new Matrix2L(multiply(other.columnA), multiply(other.columnB));
    }

    @Override
    public Matrix2L multiply(final Long scalar) {
        final Vector2L scaleVec = new Vector2L(scalar, scalar);
        return new Matrix2L(columnA.scale(scaleVec), columnB.scale(scaleVec));
    }

    @Override
    public Matrix2L transpose() {
        return new Matrix2L(new Vector2L(columnA.getX(), columnB.getX()), new Vector2L(columnA.getZ(), columnB.getZ()));
    }

    @Override
    public Matrix2L rotateRight() {
        return new Matrix2L(columnB, columnA);
    }

    @Override
    public Matrix2L rotateLeft() {
        /* We only have two columns, so either direction just swaps them */
        return rotateRight();
    }

    @Override
    public Matrix2L negate() {
        return new Matrix2L(columnA.negate(), columnB.negate());
    }

    @Override
    public Vector2L multiply(final Vector2L other) {
        final Matrix2L transposed = transpose();
        return new Vector2L(other.dot(transposed.columnA), other.dot(transposed.columnB));
    }

    @Override
    public Vector2L diagonalVector() {
        return new Vector2L(columnA.getX(), columnB.getZ());
    }

    @Override
    public Vector2L antidiagonalVector() {
        return new Vector2L(columnB.getX(), columnA.getZ());
    }

    @Override
    public boolean isUpperTriangular() {
        return columnA.getZ() == 0;
    }

    @Override
    public Matrix2L upperTriangularize() {
        return new Matrix2L(new Vector2L(columnA.getX(), 0), columnB);
    }

    @Override
    public boolean isLowerTriangular() {
        return columnB.getX() == 0;
    }

    @Override
    public Matrix2L lowerTriangularize() {
        return new Matrix2L(columnA, new Vector2L(0, columnB.getZ()));
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
