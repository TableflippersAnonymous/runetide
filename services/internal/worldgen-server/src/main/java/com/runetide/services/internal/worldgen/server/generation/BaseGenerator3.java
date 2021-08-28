package com.runetide.services.internal.worldgen.server.generation;

import com.runetide.common.domain.geometry.point.FixedPoint;
import com.runetide.common.domain.geometry.vector.Vector3L;

public abstract class BaseGenerator3<PointType extends FixedPoint<PointType, Vector3L>, ReturnArrayType>
        extends BaseGenerator<PointType, Vector3L, ReturnArrayType[][]> {

}
