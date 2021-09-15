package com.runetide.services.internal.worldgen.server.generation;

import com.runetide.common.domain.geometry.locus.FixedBoundingBoxSingle;
import com.runetide.common.domain.geometry.vector.Vector2L;
import com.runetide.common.dto.ContainerRef;

public abstract class DelegatingGenerator2<PointType extends ContainerRef<PointType, Vector2L, ?, ?, ?>, ReturnArrayType>
        implements Generator2<PointType, ReturnArrayType> {
    protected final Generator2<PointType, ReturnArrayType> generator;

    protected DelegatingGenerator2(final Generator2<PointType, ReturnArrayType> generator) {
        this.generator = generator;
    }

    @Override
    public ReturnArrayType[] generate(final FixedBoundingBoxSingle<PointType, Vector2L> boundingBox) {
        return generator.generate(boundingBox);
    }
}
