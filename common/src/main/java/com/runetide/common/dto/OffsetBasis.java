package com.runetide.common.dto;

import org.apache.commons.collections4.comparators.FixedOrderComparator;
import org.jetbrains.annotations.Contract;

import java.util.Comparator;

public interface OffsetBasis<Self extends OffsetBasis<Self>> extends Ref<Self> {
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
    default <T extends OffsetBasis<T>> T getOffsetBasis(final Class<T> clazz) {
        if(clazz.isInstance(this))
            return clazz.cast(this);
        throw new IllegalArgumentException("Invalid OffsetBasis: " + clazz);
    }
}
