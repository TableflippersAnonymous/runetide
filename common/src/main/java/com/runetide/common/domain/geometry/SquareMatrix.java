package com.runetide.common.domain.geometry;

import org.jetbrains.annotations.Contract;

public interface SquareMatrix<MatrixType extends SquareMatrix<MatrixType, VecType>, VecType extends Vector<VecType>>
        extends Matrix<MatrixType> {
    @Contract(pure = true)
    VecType multiply(final VecType other);

    @Contract(pure = true)
    VecType diagonalVector();
    @Contract(pure = true)
    VecType antidiagonalVector();

    @Contract(pure = true)
    boolean isUpperTriangular();
    @Contract(pure = true)
    MatrixType upperTriangularize();
    @Contract(pure = true)
    boolean isLowerTriangular();
    @Contract(pure = true)
    MatrixType lowerTriangularize();

    @Contract(pure = true)
    MatrixType identity();
    @Contract(pure = true)
    long determinant();

    @Contract(pure = true)
    default boolean isDiagonal() {
        return isUpperTriangular() && isLowerTriangular();
    }
    @Contract(pure = true)
    default MatrixType diagonalize() {
        return upperTriangularize().lowerTriangularize();
    }

    @Contract(pure = true)
    default long trace() {
        return diagonalVector().sum();
    }

    @Contract(pure = true)
    default boolean isSymmetric() {
        return equals(transpose());
    }

    @Contract(pure = true)
    default boolean isSkewSymmetric() {
        return equals(transpose().multiply(-1));
    }

    @Contract(pure = true)
    default boolean isOrthogonal() {
        return multiply(transpose()).equals(identity());
    }

    @Contract(pure = true)
    default boolean isSingular() {
        return determinant() == 0;
    }

    @Contract(pure = true)
    default boolean isNormal() {
        return isSymmetric() || isSkewSymmetric() || isOrthogonal();
    }

    @Contract(pure = true)
    default MatrixType transform(final MatrixType other) {
        return other.multiply(getSelf());
    }
}
