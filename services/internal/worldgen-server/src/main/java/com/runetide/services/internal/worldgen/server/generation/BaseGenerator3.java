package com.runetide.services.internal.worldgen.server.generation;

import com.runetide.common.domain.geometry.vector.Vector3L;
import com.runetide.common.dto.ContainerRef;

import java.util.function.Function;

public abstract class BaseGenerator3<GenerationParent extends ContainerRef<GenerationParent, Vector3L, ?, ?, ?>,
        PointType extends ContainerRef<PointType, Vector3L, ?, ?, ?>, ReturnArrayType>
        extends BaseGenerator<GenerationParent, Vector3L, PointType, Vector3L, ReturnArrayType[][]> {

    //FIXME
    protected BaseGenerator3(final Function<Vector3L, ReturnArrayType[][]> allocateArray,
                             final Class<GenerationParent> generationParentClass,
                             final Class<PointType> pointTypeClass) {
        super(allocateArray, generationParentClass, pointTypeClass);
    }
}
