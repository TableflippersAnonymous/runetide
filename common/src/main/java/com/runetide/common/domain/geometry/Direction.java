package com.runetide.common.domain.geometry;

import java.util.Comparator;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.Predicate;

public interface Direction<FixedVecType extends FixedVector<FixedVecType>,
        FloatVecType extends FloatVector<FloatVecType>>
        extends Vector<FixedVecType, Long> {
    FixedVecType asVectorL();
    FloatVecType asVectorF();

    Direction2 asDirection2();
    Direction3 asDirection3();

    @Override
    default FixedVecType getSelf() {
        return asVectorL();
    }

    @Override
    default FixedVecType add(final FixedVecType other) {
        return asVectorL().add(other);
    }

    @Override
    default FixedVecType subtract(final FixedVecType other) {
        return asVectorL().subtract(other);
    }

    @Override
    default Comparator<FixedVecType> compareByCoordinate(final int coordinate) {
        return asVectorL().compareByCoordinate(coordinate);
    }

    @Override
    default int coordinateSize() {
        return asVectorL().coordinateSize();
    }

    @Override
    default FixedVecType withCoordinateFrom(final FixedVecType other, final int coordinate) {
        return asVectorL().withCoordinateFrom(other, coordinate);
    }

    @Override
    default boolean isBetween(final FixedVecType start, final FixedVecType end) {
        return asVectorL().isBetween(start, end);
    }

    @Override
    default boolean isBetweenEndExclusive(final FixedVecType start, final FixedVecType end) {
        return asVectorL().isBetweenEndExclusive(start, end);
    }

    @Override
    default FixedVecType withMinCoordinateFrom(final FixedVecType other, final int coordinate) {
        return asVectorL().withMinCoordinateFrom(other, coordinate);
    }

    @Override
    default FixedVecType minCoordinates(final FixedVecType other) {
        return asVectorL().minCoordinates(other);
    }

    @Override
    default FixedVecType withMaxCoordinateFrom(final FixedVecType other, final int coordinate) {
        return asVectorL().withMaxCoordinateFrom(other, coordinate);
    }

    @Override
    default FixedVecType maxCoordinates(final FixedVecType other) {
        return asVectorL().maxCoordinates(other);
    }

    @Override
    default boolean anyCoordinateCompares(final Predicate<Integer> predicate, final FixedVecType other) {
        return asVectorL().anyCoordinateCompares(predicate, other);
    }

    @Override
    default boolean allCoordinatesCompare(final Predicate<Integer> predicate, final FixedVecType other) {
        return asVectorL().allCoordinatesCompare(predicate, other);
    }

    @Override
    default FixedVecType negate() {
        return asVectorL().negate();
    }

    @Override
    default FixedVecType scale(final FixedVecType other) {
        return asVectorL().scale(other);
    }

    @Override
    default FixedVecType scale(final Long scalar) {
        return asVectorL().scale(scalar);
    }

    @Override
    default FixedVecType divide(final FixedVecType other) {
        return asVectorL().divide(other);
    }

    @Override
    default FixedVecType divide(final Long scalar) {
        return asVectorL().divide(scalar);
    }

    @Override
    default FixedVecType modulo(final FixedVecType other) {
        return asVectorL().modulo(other);
    }

    @Override
    default List<FixedVecType> axisVectors() {
        return asVectorL().axisVectors();
    }

    @Override
    default Long sum() {
        return asVectorL().sum();
    }

    @Override
    default Long product() {
        return asVectorL().product();
    }

    @Override
    default OptionalInt getAlignedAxis() {
        return asVectorL().getAlignedAxis();
    }

    @Override
    default Long crossSquareLength(final FixedVecType other) {
        return asVectorL().crossSquareLength(other);
    }

    @Override
    default FloatVector<?> toFloat() {
        return asVectorF();
    }

    @Override
    default FixedVector<?> toFixed() {
        return asVectorL();
    }

    @Override
    default Long dot(final FixedVecType other) {
        return asVectorL().dot(other);
    }

    @Override
    default Long squareLength() {
        return asVectorL().squareLength();
    }

    @Override
    default boolean isAxisAligned() {
        return asVectorL().isAxisAligned();
    }

    @Override
    default FixedVecType transform(final SquareMatrix<?, FixedVecType, Long> transform) {
        return asVectorL().transform(transform);
    }

    @Override
    default FloatVector<?> normalize() {
        return asVectorF();
    }

    @Override
    default boolean isSameCoordinateSystem(final FixedVecType other) {
        return asVectorL().isSameCoordinateSystem(other);
    }
}
