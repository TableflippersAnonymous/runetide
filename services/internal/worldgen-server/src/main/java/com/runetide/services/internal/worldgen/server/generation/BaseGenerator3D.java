package com.runetide.services.internal.worldgen.server.generation;

import com.runetide.common.domain.geometry.FixedPoint;
import com.runetide.common.domain.geometry.Vector3L;

public abstract class BaseGenerator3D<PointType extends FixedPoint<PointType, Vector3L>, ReturnArrayType>
        extends BaseGenerator<PointType, Vector3L, ReturnArrayType[][]> {

}
