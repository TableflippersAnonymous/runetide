package com.runetide.services.internal.worldgen.server.generation;

import com.runetide.common.domain.BoundingBox;
import com.runetide.common.domain.Vec2D;
import com.runetide.common.dto.Ref;
import com.runetide.common.dto.VectorLike;

public class OpenSimplex2SGenerator2D<SeedType extends Ref<SeedType>, PointType extends VectorLike<PointType, Vec2D>>
        extends BaseGenerator2D<SeedType, PointType> {
    private final ;

    @Override
    protected void generateValues(final BoundingBox<PointType, Vec2D> boundingBox, final int[][] out) {

    }

    @Override
    protected long seed(final SeedType point) {
        return 0;
    }
}
