package com.runetide.services.internal.worldgen.server.generation;

import com.runetide.common.domain.geometry.BoundingBox;
import com.runetide.common.domain.geometry.Vector2D;
import com.runetide.common.domain.geometry.Point;
import com.runetide.services.internal.worldgen.server.vendor.OpenSimplex2S;

public class OpenSimplex2SGenerator2D<PointType extends Point<PointType, Vector2D>>
        extends BaseGenerator2D<PointType, double[]> {
    private final OpenSimplex2S openSimplex2S;
    private final OpenSimplex2S.GenerateContext2D context;

    @Override
    protected double[][] generateValues(final BoundingBox<PointType, Vector2D> boundingBox) {
        final Vector2D dimensions = boundingBox.getDimensions();
        boundingBox.getStart()
        final double[][] buf = new double[(int) dimensions.getX()][(int) dimensions.getZ()];
        openSimplex2S.generate2(context, buf, );
        return buf;
    }
}
