package com.runetide.services.internal.worldgen.server.generation;

import com.runetide.common.domain.geometry.BoundingBox;
import com.runetide.common.domain.geometry.Point;
import com.runetide.common.domain.geometry.Vector;

public abstract class BaseGenerator<PointType extends Point<PointType, VecType>, VecType extends Vector<VecType>, ReturnType> {
    protected abstract ReturnType generateValues(final BoundingBox<PointType, VecType> boundingBox);
}
