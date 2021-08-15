package com.runetide.common.domain.geometry;

import java.util.Objects;

public class Matrix3D implements SquareMatrix<Matrix3D, Vec3D> {
    public static final Matrix3D IDENTITY = new Matrix3D(new Vec3D(1, 0, 0), new Vec3D(0, 1, 0),
            new Vec3D(0, 0, 1));

    private final Vec3D columnA;
    private final Vec3D columnB;
    private final Vec3D columnC;

    public Matrix3D(final Vec3D columnA, final Vec3D columnB, final Vec3D columnC) {
        this.columnA = Objects.requireNonNull(columnA);
        this.columnB = Objects.requireNonNull(columnB);
        this.columnC = Objects.requireNonNull(columnC);
    }

    @Override
    public Matrix3D add(final Matrix3D other) {
        return new Matrix3D(columnA.add(other.columnA), columnB.add(other.columnB), columnC.add(other.columnC));
    }

    @Override
    public Matrix3D multiply(final Matrix3D other) {
        return new Matrix3D(multiply(other.columnA), multiply(other.columnB), multiply(other.columnC));
    }

    @Override
    public Matrix3D multiply(final long scalar) {
        final Vec3D scaleVec = new Vec3D(scalar, scalar, scalar);
        return new Matrix3D(columnA.scale(scaleVec), columnB.scale(scaleVec), columnC.scale(scaleVec));
    }

    @Override
    public Matrix3D transpose() {
        return new Matrix3D(new Vec3D(columnA.getX(), columnB.getX(), columnC.getX()),
                new Vec3D(columnA.getY(), columnB.getY(), columnC.getY()),
                new Vec3D(columnA.getZ(), columnB.getZ(), columnC.getZ()));
    }

    @Override
    public Matrix3D rotateRight() {
        return new Matrix3D(columnC, columnA, columnB);
    }

    @Override
    public Matrix3D rotateLeft() {
        return new Matrix3D(columnB, columnC, columnA);
    }

    @Override
    public Vec3D multiply(final Vec3D other) {
        final Matrix3D transposed = transpose();
        return new Vec3D(other.dot(transposed.columnA), other.dot(transposed.columnB), other.dot(transposed.columnC));
    }

    @Override
    public Vec3D diagonalVector() {
        return new Vec3D(columnA.getX(), columnB.getY(), columnC.getZ());
    }

    @Override
    public Vec3D antidiagonalVector() {
        return new Vec3D(columnC.getX(), columnB.getY(), columnA.getZ());
    }

    @Override
    public boolean isUpperTriangular() {
        return columnA.getY() == 0 && columnA.getZ() == 0 && columnB.getZ() == 0;
    }

    @Override
    public Matrix3D upperTriangularize() {
        return new Matrix3D(new Vec3D(columnA.getX(), 0, 0), new Vec3D(columnB.getX(), columnB.getY(), 0),
                columnC);
    }

    @Override
    public boolean isLowerTriangular() {
        return columnB.getX() == 0 && columnC.getX() == 0 && columnC.getY() == 0;
    }

    @Override
    public Matrix3D lowerTriangularize() {
        return new Matrix3D(columnA, new Vec3D(0, columnB.getY(), columnB.getZ()),
                new Vec3D(0, 0, columnC.getZ()));
    }

    @Override
    public Matrix3D identity() {
        return IDENTITY;
    }

    @Override
    public long determinant() {
        final Matrix3D rotateRight = rotateRight();
        final Matrix3D rotateLeft = rotateLeft();
        return diagonalVector().product() - antidiagonalVector().product()
                + rotateLeft.diagonalVector().product() - rotateLeft.antidiagonalVector().product()
                + rotateRight.diagonalVector().product() - rotateRight.antidiagonalVector().product();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Matrix3D matrix3D = (Matrix3D) o;
        return Objects.equals(columnA, matrix3D.columnA) && Objects.equals(columnB, matrix3D.columnB) && Objects.equals(columnC, matrix3D.columnC);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnA, columnB, columnC);
    }

    @Override
    public String toString() {
        return "<Matrix3D:" + columnA + "," + columnB + "," + columnC + ">";
    }
}
