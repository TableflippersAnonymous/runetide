package com.runetide.services.internal.worldgen.server.generation;

import com.runetide.common.domain.geometry.locus.FixedBoundingBoxSingle;
import com.runetide.common.domain.geometry.vector.Vector2L;
import com.runetide.common.dto.ContainerRef;
import com.runetide.services.internal.worldgen.server.vendor.OpenSimplex2S;

import java.util.function.Function;

public class OpenSimplex2SGenerator2F<GenerationParent extends ContainerRef<GenerationParent, Vector2L, ?, ?, ?>,
        PointType extends ContainerRef<PointType, Vector2L, ?, ?, ?>>
        extends BaseGenerator2<GenerationParent, PointType, double[]> {
    private final Function<GenerationParent, Long> seedProvider;
    private final OpenSimplex2S.GenerateContext2D context;

    protected OpenSimplex2SGenerator2F(final Class<GenerationParent> generationParentClass,
                                       final Class<PointType> pointTypeClass,
                                       final Function<GenerationParent, Long> seedProvider,
                                       final OpenSimplex2S.GenerateContext2D context) {
        super(vec -> new double[vec.getX().intValue()][vec.getZ().intValue()], generationParentClass, pointTypeClass);
        this.seedProvider = seedProvider;
        this.context = context;
    }

    @Override
    protected void generateValues(final double[][] ret, final GenerationParent parent,
                                  final FixedBoundingBoxSingle<PointType, Vector2L> boundingBox,
                                  final FixedBoundingBoxSingle<PointType, Vector2L> _parentBox,
                                  final Vector2L retOffset, final Vector2L parentOffset) {
        final OpenSimplex2S openSimplex2S = new OpenSimplex2S(seedProvider.apply(parent));
        final Vector2L dimensions = boundingBox.getDimensions();
        /* generate2 orders ret in ret[y][x], so we swap the coordinates of X and Z to get ret[x][z]. */
        openSimplex2S.generate2(context, ret, parentOffset.getZ().intValue(), parentOffset.getX().intValue(),
                dimensions.getZ().intValue(), dimensions.getX().intValue(),
                retOffset.getZ().intValue(), retOffset.getX().intValue());
    }
}
