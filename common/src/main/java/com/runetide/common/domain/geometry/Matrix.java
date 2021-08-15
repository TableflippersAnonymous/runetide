package com.runetide.common.domain.geometry;

import org.jetbrains.annotations.Contract;

public interface Matrix<MatrixType extends Matrix<MatrixType>> {
    @Contract(pure = true)
    MatrixType add(final MatrixType other);
    @Contract(pure = true)
    MatrixType multiply(final MatrixType other);
    @Contract(pure = true)
    MatrixType multiply(final long scalar);
    @Contract(pure = true)
    MatrixType transpose();
    @Contract(pure = true)
    MatrixType rotateRight();
    @Contract(pure = true)
    MatrixType rotateLeft();
}
