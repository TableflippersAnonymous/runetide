package com.runetide.common.dto;

import com.runetide.common.domain.geometry.BoundingBox;
import com.runetide.common.domain.geometry.Point;
import com.runetide.common.domain.geometry.Vector;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public interface ContainerRef<Child extends Point<Child, VecType>, VecType extends Vector<VecType>>
        extends Iterable<Child> {
    @Contract(pure = true)
    Child getStart();
    @Contract(pure = true)
    Child getEnd();

    @Contract(pure = true)
    default BoundingBox<Child, VecType> asBoundingBox() {
        return new BoundingBox<>(getStart(), getEnd());
    }

    @Contract(pure = true)
    default boolean contains(final Child child) {
        return asBoundingBox().contains(child);
    }

    @Nonnull
    @Override
    default Iterator<Child> iterator() {
        return asBoundingBox().iterator();
    }

    @Override
    default void forEach(Consumer<? super Child> action) {
        asBoundingBox().forEach(action);
    }

    @Override
    default Spliterator<Child> spliterator() {
        return asBoundingBox().spliterator();
    }
}
