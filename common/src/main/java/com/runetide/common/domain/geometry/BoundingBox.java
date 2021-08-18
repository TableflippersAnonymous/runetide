package com.runetide.common.domain.geometry;

import org.jetbrains.annotations.Contract;

import java.util.Optional;
import java.util.function.Function;

public interface BoundingBox<BBType extends BoundingBox<BBType, PointType, VecType, NumberType>,
        PointType extends Point<PointType, VecType, NumberType>, VecType extends Vector<VecType, NumberType>,
        NumberType extends Number>
        extends Locus<BBType, PointType> {

    @Override
    Optional<BBType> intersect(final BBType other);
    @Contract(pure = true)
    boolean intersectsWith(final BBType other);
    @Contract(pure = true)
    VecType getDimensions();
    @Contract(pure = true)
    BBType move(final VecType direction);
    @Contract(pure = true, value = "-> this")
    BBType getSelf();
}
