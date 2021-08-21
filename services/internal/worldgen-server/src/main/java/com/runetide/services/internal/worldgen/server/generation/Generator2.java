package com.runetide.services.internal.worldgen.server.generation;

import com.runetide.common.domain.geometry.Vector2L;
import com.runetide.common.dto.ContainerRef;

public interface Generator2<PointType extends ContainerRef<PointType, Vector2L, ?, ?, ?>, ReturnArrayType>
        extends Generator<PointType, Vector2L, ReturnArrayType[]> {
}
