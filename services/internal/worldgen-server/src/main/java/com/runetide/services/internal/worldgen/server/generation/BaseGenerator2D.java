package com.runetide.services.internal.worldgen.server.generation;

import com.runetide.common.domain.geometry.Vector2D;
import com.runetide.common.domain.geometry.Point;

public abstract class BaseGenerator2D<PointType extends Point<PointType, Vector2D>, ReturnArrayType>
        extends BaseGenerator<PointType, Vector2D, ReturnArrayType[]> {

}
