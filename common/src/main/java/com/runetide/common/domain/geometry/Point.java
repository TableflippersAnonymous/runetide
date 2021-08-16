package com.runetide.common.domain.geometry;

import org.jetbrains.annotations.Contract;

import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public interface Point<Self extends Point<Self, VecType>, VecType extends Vector<VecType>> {
    int COORDINATE_X = 0;
    int COORDINATE_Z = 1;
    int COORDINATE_Y = 2;

    @Contract(pure = true, value = "-> this")
    Self getSelf();

    @Contract(pure = true)
    boolean isSameCoordinateSystem(final Self other);

    @Contract(pure = true)
    Self add(final VecType other);
    @Contract(pure = true)
    Self add(final long val);
    @Contract(pure = true)
    VecType subtract(final Self other);

    @Contract(pure = true)
    Comparator<Self> compareByCoordinate(final int coordinate);

    @Contract(pure = true)
    Iterator<Self> iteratorTo(final Self end);

    @Contract(pure = true)
    int coordinateSize();

    @Contract(pure = true)
    Self withCoordinateFrom(final Self other, final int coordinate);

    @Contract(pure = true)
    default boolean isBetween(final Self start, final Self end) {
        return allCoordinatesCompare(c -> c >= 0, start)
                && allCoordinatesCompare(c -> c <= 0, end);
    }

    @Contract(pure = true)
    default Self withMinCoordinateFrom(final Self other, final int coordinate) {
        if(compareByCoordinate(coordinate).compare(getSelf(), other) <= 0)
            return getSelf();
        return withCoordinateFrom(other, coordinate);
    }

    @Contract(pure = true)
    default Self minCoordinates(final Self other) {
        Self min = getSelf();
        for(int coordinate = 0; coordinate < coordinateSize(); coordinate++)
            min = min.withMinCoordinateFrom(other, coordinate);
        return min;
    }

    @Contract(pure = true)
    default Self withMaxCoordinateFrom(final Self other, final int coordinate) {
        if(compareByCoordinate(coordinate).compare(getSelf(), other) >= 0)
            return getSelf();
        return withCoordinateFrom(other, coordinate);
    }

    @Contract(pure = true)
    default Self maxCoordinates(final Self other) {
        Self max = getSelf();
        for(int coordinate = 0; coordinate < coordinateSize(); coordinate++)
            max = max.withMaxCoordinateFrom(other, coordinate);
        return max;
    }

    @Contract(pure = true)
    default boolean anyCoordinateCompares(final Predicate<Integer> predicate, final Self other) {
        return IntStream.range(0, coordinateSize())
                .mapToObj(this::compareByCoordinate)
                .anyMatch(c -> predicate.test(c.compare(getSelf(), other)));
    }

    @Contract(pure = true)
    default boolean allCoordinatesCompare(final Predicate<Integer> predicate, final Self other) {
        return IntStream.range(0, coordinateSize())
                .mapToObj(this::compareByCoordinate)
                .allMatch(c -> predicate.test(c.compare(getSelf(), other)));
    }
}
