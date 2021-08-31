package com.runetide.common.domain.geometry.matrix;

import com.runetide.common.domain.geometry.vector.Vector2L;
import com.runetide.common.domain.geometry.vector.Vector3L;
import org.jetbrains.annotations.Contract;

public interface Matrix<MatrixType extends Matrix<MatrixType, NumberType>, NumberType extends Number> {
    static Matrix2L of(final Vector2L columnA, final Vector2L columnB) {
        return Matrix2L.of(columnA, columnB);
    }

    static Matrix3L of(final Vector3L columnA, final Vector3L columnB, final Vector3L columnC) {
        return Matrix3L.of(columnA, columnB, columnC);
    }

    @Contract(value = "-> this", pure = true)
    MatrixType getSelf();
    @Contract(pure = true)
    MatrixType add(final MatrixType other);
    @Contract(pure = true)
    MatrixType multiply(final MatrixType other);
    @Contract(pure = true)
    MatrixType multiply(final NumberType scalar);
    @Contract(pure = true)
    MatrixType transpose();
    @Contract(pure = true)
    MatrixType rotateRight();
    @Contract(pure = true)
    MatrixType rotateLeft();
    @Contract(pure = true)
    MatrixType negate();
}
