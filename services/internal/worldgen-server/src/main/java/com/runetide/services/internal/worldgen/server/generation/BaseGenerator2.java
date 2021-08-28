package com.runetide.services.internal.worldgen.server.generation;

import com.runetide.common.domain.geometry.vector.Vector2L;
import com.runetide.common.dto.ContainerRef;

import java.util.function.Function;

public abstract class BaseGenerator2<GenerationParent extends ContainerRef<GenerationParent, Vector2L, ?, ?, ?>,
        PointType extends ContainerRef<PointType, Vector2L, ?, ?, ?>, ReturnArrayType>
        extends BaseGenerator<GenerationParent, Vector2L, PointType, Vector2L, ReturnArrayType[]> {
    protected BaseGenerator2(final Function<Vector2L, ReturnArrayType[]> allocateArray,
                             final Class<GenerationParent> generationParentClass,
                             final Class<PointType> pointTypeClass) {
        super(allocateArray, generationParentClass, pointTypeClass);
    }
}
