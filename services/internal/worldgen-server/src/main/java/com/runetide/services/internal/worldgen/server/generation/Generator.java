package com.runetide.services.internal.worldgen.server.generation;

import com.runetide.common.domain.geometry.FixedBoundingBoxSingle;
import com.runetide.common.domain.geometry.FixedVector;
import com.runetide.common.dto.ContainerRef;

public interface Generator<PointType extends ContainerRef<PointType, VecType, ?, ?, ?>,
        VecType extends FixedVector<VecType>, ReturnType> {
    ReturnType generate(final FixedBoundingBoxSingle<PointType, VecType> boundingBox);
}
