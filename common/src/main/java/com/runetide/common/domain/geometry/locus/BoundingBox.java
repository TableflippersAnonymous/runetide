package com.runetide.common.domain.geometry.locus;

import com.runetide.common.domain.geometry.point.FixedPoint;
import com.runetide.common.domain.geometry.point.Point;
import com.runetide.common.domain.geometry.vector.FixedVector;
import com.runetide.common.domain.geometry.vector.Vector;
import org.jetbrains.annotations.Contract;

import java.util.Optional;

public interface BoundingBox<BBType extends BoundingBox<BBType, PointType, VecType, NumberType>,
        PointType extends Point<PointType, VecType, NumberType>, VecType extends Vector<VecType, NumberType>,
        NumberType extends Number>
        extends Locus<BBType, PointType> {
    static <PointType extends FixedPoint<PointType, VecType>, VecType extends FixedVector<VecType>>
    FixedBoundingBoxSingle<PointType, VecType> of(final PointType start, final PointType end) {
        return FixedBoundingBoxSingle.of(start, end);
    }

    static <PointType extends FixedPoint<PointType, VecType>, VecType extends FixedVector<VecType>>
    FixedBoundingBoxSingle<PointType, VecType> of(final PointType point) {
        return FixedBoundingBoxSingle.of(point, point);
    }

    static <PointType extends Point<PointType, VecType, Double>, VecType extends Vector<VecType, Double>>
    FloatBoundingBoxSingle<PointType, VecType> of(final PointType start, final PointType end) {
        return FloatBoundingBoxSingle.of(start, end);
    }

    static <PointType extends FixedPoint<PointType, VecType>, VecType extends FixedVector<VecType>>
    FixedBoundingBoxSet<PointType, VecType> of(final FixedBoundingBoxSingle<PointType, VecType> box) {
        return FixedBoundingBoxSet.of(box);
    }

    static <PointType extends Point<PointType, VecType, Double>, VecType extends Vector<VecType, Double>>
    FloatBoundingBoxSet<PointType, VecType> of(final FloatBoundingBoxSingle<PointType, VecType> box) {
        return FloatBoundingBoxSet.of(box);
    }

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
