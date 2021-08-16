package com.runetide.common.domain.geometry;

import org.jetbrains.annotations.Contract;

import java.util.Optional;
import java.util.function.Function;

public interface BoundingBoxLike<BBType extends BoundingBoxLike<BBType, PointType, VecType>,
        PointType extends Point<PointType, VecType>, VecType extends Vector<VecType>>
        extends IterableLocus<BBType, PointType, VecType> {

    @Override
    Optional<BBType> intersect(final BBType other);

    @Override
    BoundingBoxSet<PointType, VecType> union(final BBType other);

    @Override
    Optional<BoundingBoxSet<PointType, VecType>> subtract(final BBType other);

    @Contract(pure = true)
    <NewPointType extends Point<NewPointType, NewVecType>, NewVecType extends Vector<NewVecType>>
    BoundingBoxLike<?, NewPointType, NewVecType> map(final Function<PointType, NewPointType> startMapper,
                                           final Function<PointType, NewPointType> endMapper);


    @Contract(pure = true)
    default <NewPointType extends Point<NewPointType, NewVecType>, NewVecType extends Vector<NewVecType>>
    BoundingBoxLike<?, NewPointType, NewVecType> map(final Function<PointType, NewPointType> mapper) {
        return map(mapper, mapper);
    }

    @Contract(pure = true)
    default BoundingBoxLike<?, PointType, VecType> move(final VecType direction) {
        return map(e -> e.add(direction));
    }
}
