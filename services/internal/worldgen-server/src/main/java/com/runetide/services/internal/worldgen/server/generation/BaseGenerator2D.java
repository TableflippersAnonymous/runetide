package com.runetide.services.internal.worldgen.server.generation;

import com.runetide.common.domain.BoundingBox;
import com.runetide.common.domain.Vec2D;
import com.runetide.common.dto.Ref;
import com.runetide.common.dto.VectorLike;

public abstract class BaseGenerator2D<SeedType extends Ref<SeedType>, PointType extends VectorLike<PointType, Vec2D>> {

    protected abstract void generateValues(final BoundingBox<PointType, Vec2D> boundingBox, int[][] out);

    protected abstract long seed(final SeedType point);
}
