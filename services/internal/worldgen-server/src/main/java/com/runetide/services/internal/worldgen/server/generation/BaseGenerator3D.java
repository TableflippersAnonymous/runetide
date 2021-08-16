package com.runetide.services.internal.worldgen.server.generation;

import com.runetide.common.domain.geometry.Point;
import com.runetide.common.domain.geometry.Vector3D;

public abstract class BaseGenerator3D<PointType extends Point<PointType, Vector3D>, ReturnArrayType>
        extends BaseGenerator<PointType, Vector3D, ReturnArrayType[][]> {

}
