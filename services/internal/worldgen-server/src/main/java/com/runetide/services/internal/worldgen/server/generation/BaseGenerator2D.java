package com.runetide.services.internal.worldgen.server.generation;

import com.runetide.common.domain.geometry.BoundingBox;
import com.runetide.common.domain.geometry.Vec2D;
import com.runetide.common.dto.Ref;
import com.runetide.common.domain.geometry.Point;

public abstract class BaseGenerator2D<SeedType extends Ref<SeedType>, PointType extends Point<PointType, Vec2D>> {

    protected abstract int[][] generateValues(final BoundingBox<PointType, Vec2D> boundingBox);

    protected abstract long seed(final SeedType point);
}
