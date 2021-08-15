package com.runetide.common.domain.geometry;

import java.util.Objects;

public class Matrix2D implements SquareMatrix<Matrix2D, Vec2D> {
    public static final Matrix2D IDENTITY = new Matrix2D(new Vec2D(1, 0), new Vec2D(0, 1));

    private final Vec2D columnA;
    private final Vec2D columnB;

    public Matrix2D(final Vec2D columnA, final Vec2D columnB) {
        this.columnA = Objects.requireNonNull(columnA);
        this.columnB = Objects.requireNonNull(columnB);
    }

    @Override
    public Matrix2D add(final Matrix2D other) {
        return new Matrix2D(columnA.add(other.columnA), columnB.add(other.columnB));
    }

    @Override
    public Matrix2D multiply(final Matrix2D other) {
        return new Matrix2D(multiply(other.columnA), multiply(other.columnB));
    }

    @Override
    public Matrix2D multiply(final long scalar) {
        final Vec2D scaleVec = new Vec2D(scalar, scalar);
        return new Matrix2D(columnA.scale(scaleVec), columnB.scale(scaleVec));
    }

    @Override
    public Matrix2D transpose() {
        return new Matrix2D(new Vec2D(columnA.getX(), columnB.getX()), new Vec2D(columnA.getZ(), columnB.getZ()));
    }

    @Override
    public Matrix2D rotateRight() {
        return new Matrix2D(columnB, columnA);
    }

    @Override
    public Matrix2D rotateLeft() {
        /* We only have two columns, so either direction just swaps them */
        return rotateRight();
    }

    @Override
    public Vec2D multiply(final Vec2D other) {
        final Matrix2D transposed = transpose();
        return new Vec2D(other.dot(transposed.columnA), other.dot(transposed.columnB));
    }

    @Override
    public Vec2D diagonalVector() {
        return new Vec2D(columnA.getX(), columnB.getZ());
    }

    @Override
    public Vec2D antidiagonalVector() {
        return new Vec2D(columnB.getX(), columnA.getZ());
    }

    @Override
    public boolean isUpperTriangular() {
        return columnA.getZ() == 0;
    }

    @Override
    public Matrix2D upperTriangularize() {
        return new Matrix2D(new Vec2D(columnA.getX(), 0), columnB);
    }

    @Override
    public boolean isLowerTriangular() {
        return columnB.getX() == 0;
    }

    @Override
    public Matrix2D lowerTriangularize() {
        return new Matrix2D(columnA, new Vec2D(0, columnB.getZ()));
    }

    @Override
    public Matrix2D identity() {
        return IDENTITY;
    }

    @Override
    public long determinant() {
        return columnA.getX() * columnB.getZ() - columnB.getX() * columnA.getZ();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Matrix2D matrix2D = (Matrix2D) o;
        return Objects.equals(columnA, matrix2D.columnA) && Objects.equals(columnB, matrix2D.columnB);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnA, columnB);
    }

    @Override
    public String toString() {
        return "<Matrix2D:" + columnA + "," + columnB + ">";
    }
}
