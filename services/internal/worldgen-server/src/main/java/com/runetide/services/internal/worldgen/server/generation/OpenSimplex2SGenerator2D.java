package com.runetide.services.internal.worldgen.server.generation;

import com.runetide.common.domain.geometry.FixedBoundingBoxSingle;
import com.runetide.common.domain.geometry.Vector2L;
import com.runetide.common.dto.ContainerRef;
import com.runetide.services.internal.worldgen.server.vendor.OpenSimplex2S;

public class OpenSimplex2SGenerator2D<GenerationParent extends ContainerRef<GenerationParent, Vector2L, ?, ?, ?>,
        PointType extends ContainerRef<PointType, Vector2L, ?, ?, ?>>
        extends BaseGenerator2D<GenerationParent, PointType, double[]> {
    private final OpenSimplex2S openSimplex2S;
    private final OpenSimplex2S.GenerateContext2D context;

    protected OpenSimplex2SGenerator2D(final Class<GenerationParent> generationParentClass,
                                       final Class<PointType> pointTypeClass) {
        super(vec -> new double[(int) vec.getX()][(int) vec.getZ()], generationParentClass, pointTypeClass);
    }

    @Override
    protected void generateValues(final double[][] ret, final GenerationParent parent,
                                  final FixedBoundingBoxSingle<PointType, Vector2L> boundingBox, final Vector2L retOffset,
                                  final Vector2L parentOffset) {
        final Vector2L dimensions = boundingBox.getDimensions();
        /* generate2 orders ret in ret[y][x], so we swap the coordinates of X and Z to get ret[x][z]. */
        openSimplex2S.generate2(context, ret, (int) parentOffset.getZ(), (int) parentOffset.getX(),
                (int) dimensions.getZ(), (int) dimensions.getX(), (int) retOffset.getZ(), (int) retOffset.getX());
    }
}
