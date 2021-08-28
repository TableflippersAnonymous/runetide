package com.runetide.common.domain.geometry.point;

import com.runetide.common.domain.geometry.vector.FixedVector;
import org.jetbrains.annotations.Contract;

import java.util.Iterator;

public interface FixedPoint<Self extends FixedPoint<Self, VecType>, VecType extends FixedVector<VecType>>
        extends Point<Self, VecType, Long> {
    @Contract(pure = true)
    Self add(final long val);

    @Contract(pure = true)
    Iterator<Self> iteratorTo(final Self end);
}
