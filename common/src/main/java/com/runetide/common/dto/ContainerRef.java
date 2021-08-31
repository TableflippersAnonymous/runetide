package com.runetide.common.dto;

import com.runetide.common.domain.geometry.locus.BoundingBox;
import com.runetide.common.domain.geometry.locus.FixedBoundingBoxSingle;
import com.runetide.common.domain.geometry.vector.FixedVector;
import com.runetide.common.domain.geometry.point.FixedPoint;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public interface ContainerRef<Self extends ContainerRef<Self, VecType, ParentType, ChildType, ChildVecType>,
        VecType extends FixedVector<VecType>, ParentType extends ContainerBase<ParentType>,
        ChildType extends ContainerRef<ChildType, ChildVecType, ?, ?, ?>,
        ChildVecType extends FixedVector<ChildVecType>>
        extends Ref<Self>, FixedPoint<Self, VecType>, ContainerBase<Self>, Iterable<ChildType> {
    @Contract(pure = true)
    VecType offsetTo(final ContainerBase<?> basis);
    @Contract(pure = true)
    ParentType getParent();

    @Contract(pure = true)
    ChildType getStart();
    @Contract(pure = true)
    ChildType getEnd();

    @Contract(pure = true)
    default FixedBoundingBoxSingle<ChildType, ChildVecType> asBoundingBox() {
        return BoundingBox.of(getStart(), getEnd());
    }

    @Contract(pure = true)
    default boolean contains(final ChildType child) {
        return asBoundingBox().contains(child);
    }

    @Nonnull
    @Override
    default Iterator<ChildType> iterator() {
        return asBoundingBox().iterator();
    }

    @Override
    default void forEach(Consumer<? super ChildType> action) {
        asBoundingBox().forEach(action);
    }

    @Override
    default Spliterator<ChildType> spliterator() {
        return asBoundingBox().spliterator();
    }

    @Override
    default <T extends ContainerBase<T>> T getOffsetBasis(final Class<T> clazz) {
        if(clazz.isInstance(this))
            return clazz.cast(this);
        if(ContainerBase.CONTAINING_COMPARATOR.compare(clazz, getClass()) < 0)
            return getParent().getOffsetBasis(clazz);
        return getStart().getOffsetBasis(clazz);
    }

    @Override
    default <T extends ContainerBase<T> & FixedPoint<T, TVecType>, TVecType extends FixedVector<TVecType>>
    FixedBoundingBoxSingle<T, TVecType> asBoundingBox(final Class<T> clazz) {
        if(clazz.isInstance(this))
            return BoundingBox.of(clazz.cast(this), clazz.cast(this));
        if(ContainerBase.CONTAINING_COMPARATOR.compare(clazz, getClass()) < 0)
            return getParent().asBoundingBox(clazz);
        final T start = getStart().asBoundingBox(clazz).getStart();
        final T end = getEnd().asBoundingBox(clazz).getEnd();
        return BoundingBox.of(start, end);
    }
}
