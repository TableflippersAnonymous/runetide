package com.runetide.services.internal.worldgen.server.generation;

import com.runetide.common.domain.geometry.BoundingBox;
import com.runetide.common.domain.geometry.Vec2D;
import com.runetide.common.dto.Ref;
import com.runetide.common.domain.geometry.Point;
import com.runetide.services.internal.worldgen.server.vendor.OpenSimplex2S;

public class OpenSimplex2SGenerator2D<SeedType extends Ref<SeedType>, PointType extends Point<PointType, Vec2D>>
        extends BaseGenerator2D<SeedType, PointType> {
    private final OpenSimplex2S openSimplex2S;
    private final OpenSimplex2S.GenerateContext2D context;

    @Override
    protected int[][] generateValues(final BoundingBox<PointType, Vec2D> boundingBox) {

        openSimplex2S.generate2(context, );
    }

    @Override
    protected long seed(final SeedType point) {
        return 0;
    }
}
