package com.runetide.common.dto;

import com.runetide.common.domain.geometry.Point;
import com.runetide.common.domain.geometry.Vector;
import org.jetbrains.annotations.Contract;

public interface OffsetRef<Self extends OffsetRef<Self, VecType, ParentType, ChildType, ChildVecType>,
        VecType extends Vector<VecType>, ParentType extends OffsetBasis<ParentType>,
        ChildType extends OffsetRef<ChildType, ChildVecType, ?, ?, ?>, ChildVecType extends Vector<ChildVecType>>
        extends Ref<Self>, Point<Self, VecType>, OffsetBasis<Self>, ContainerRef<ChildType, ChildVecType> {
    @Contract(pure = true)
    VecType offsetTo(final OffsetBasis<?> basis);
    @Contract(pure = true)
    ParentType getParent();

    @Override
    default <T extends OffsetBasis<T>> T getOffsetBasis(final Class<T> clazz) {
        if(clazz.isInstance(this))
            return clazz.cast(this);
        if(OffsetBasis.CONTAINING_COMPARATOR.compare(getClass(), clazz) < 0)
            return getParent().getOffsetBasis(clazz);
        return getStart().getOffsetBasis(clazz);
    }
}
