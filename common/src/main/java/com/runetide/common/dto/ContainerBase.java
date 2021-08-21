package com.runetide.common.dto;

import com.runetide.common.domain.geometry.FixedBoundingBoxSingle;
import com.runetide.common.domain.geometry.FixedPoint;
import com.runetide.common.domain.geometry.FixedVector;
import org.apache.commons.collections4.comparators.FixedOrderComparator;
import org.jetbrains.annotations.Contract;

import java.util.Comparator;

public interface ContainerBase<Self extends ContainerBase<Self>> extends Ref<Self> {
    class Initialize {
        private static Comparator<Class<?>> initialize() {
            final FixedOrderComparator<Class<?>> fixedOrderComparator = new FixedOrderComparator<>(
                    WorldRef.class, SectorRef.class, RegionRef.class, ChunkRef.class, ChunkSectionRef.class, BlockRef.class,
                    PositionRef.class, PositionLookRef.class
            );
            fixedOrderComparator.addAsEqual(ChunkSectionRef.class, ColumnRef.class);
            fixedOrderComparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.EXCEPTION);
            assert fixedOrderComparator.compare(WorldRef.class, SectorRef.class) < 0;
            return fixedOrderComparator;
        }
    }

    Comparator<Class<?>> CONTAINING_COMPARATOR = Initialize.initialize();

    @Contract(pure = true)
    default <T extends ContainerBase<T>> T getOffsetBasis(final Class<T> clazz) {
        if(clazz.isInstance(this))
            return clazz.cast(this);
        throw new IllegalArgumentException("Invalid OffsetBasis: " + clazz);
    }

    @Contract(pure = true)
    default <T extends ContainerBase<T> & FixedPoint<T, VecType>, VecType extends FixedVector<VecType>>
    FixedBoundingBoxSingle<T, VecType> asBoundingBox(final Class<T> clazz) {
        if(clazz.isInstance(this))
            return FixedBoundingBoxSingle.of(clazz.cast(this), clazz.cast(this));
        throw new IllegalArgumentException("Invalid OffsetBasis: " + clazz);
    }
}
