package com.runetide.common.domain;

import com.runetide.common.dto.Vec;
import com.runetide.common.dto.VectorLike;
import org.jetbrains.annotations.Contract;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface BoundingBoxLike<BBType extends BoundingBoxLike<BBType, PointType, VecType>,
        PointType extends VectorLike<PointType, VecType>, VecType extends Vec<VecType>> extends Iterable<PointType> {
    @Contract(pure = true)
    boolean contains(final PointType element);
    @Contract(pure = true)
    boolean intersectsWith(final BoundingBox<PointType, VecType> other);
    @Contract(pure = true)
    Optional<BBType> intersect(final BoundingBox<PointType, VecType> other);
    @Contract(pure = true)
    BoundingBoxSet<PointType, VecType> union(final BoundingBox<PointType, VecType> other);
    @Contract(pure = true)
    Optional<BoundingBoxSet<PointType, VecType>> subtract(final BoundingBox<PointType, VecType> other);
    @Contract(pure = true)
    VecType getDimensions();

    @Contract(pure = true)
    <NewPointType extends VectorLike<NewPointType, NewVecType>, NewVecType extends Vec<NewVecType>>
    BoundingBoxLike<?, NewPointType, NewVecType> map(final Function<PointType, NewPointType> startMapper,
                                                     final Function<PointType, NewPointType> endMapper);

    @Contract(pure = true)
    default Stream<PointType> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    @Contract(pure = true)
    default <NewPointType extends VectorLike<NewPointType, NewVecType>, NewVecType extends Vec<NewVecType>>
    BoundingBoxLike<?, NewPointType, NewVecType> map(final Function<PointType, NewPointType> mapper) {
        return map(mapper, mapper);
    }

    @Contract(pure = true)
    default BoundingBoxLike<?, PointType, VecType> move(final VecType direction) {
        return map(e -> e.add(direction));
    }
}
