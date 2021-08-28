package com.runetide.common.domain.geometry.locus;

import com.runetide.common.domain.geometry.point.FixedPoint;
import com.runetide.common.domain.geometry.vector.FixedVector;
import org.jetbrains.annotations.Contract;

import java.util.function.Function;

public interface FixedBoundingBox<BBType extends FixedBoundingBox<BBType, PointType, VecType>,
        PointType extends FixedPoint<PointType, VecType>, VecType extends FixedVector<VecType>>
        extends BoundingBox<BBType, PointType, VecType, Long>,
        IterableLocus<BBType, PointType, VecType> {
    @Contract(pure = true)
    <NewPointType extends FixedPoint<NewPointType, NewVecType>, NewVecType extends FixedVector<NewVecType>>
    FixedBoundingBox<?, NewPointType, NewVecType> map(final Function<PointType, NewPointType> startMapper,
                                                      final Function<PointType, NewPointType> endMapper);

    @Contract(pure = true)
    default <NewPointType extends FixedPoint<NewPointType, NewVecType>, NewVecType extends FixedVector<NewVecType>>
    FixedBoundingBox<?, NewPointType, NewVecType> map(final Function<PointType, NewPointType> mapper) {
        return map(mapper, mapper);
    }
}
