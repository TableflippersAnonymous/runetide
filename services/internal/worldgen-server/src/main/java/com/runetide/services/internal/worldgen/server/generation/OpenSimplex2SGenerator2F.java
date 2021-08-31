package com.runetide.services.internal.worldgen.server.generation;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.runetide.common.domain.geometry.locus.FixedBoundingBoxSingle;
import com.runetide.common.domain.geometry.vector.Vector2L;
import com.runetide.common.dto.ContainerRef;
import com.runetide.services.internal.worldgen.server.vendor.OpenSimplex2S;

import java.util.function.Function;

public class OpenSimplex2SGenerator2F<GenerationParent extends ContainerRef<GenerationParent, Vector2L, ?, ?, ?>,
        PointType extends ContainerRef<PointType, Vector2L, ?, ?, ?>>
        extends BaseGenerator2<GenerationParent, PointType, double[]> {
    private static final LoadingCache<Long, OpenSimplex2S> CACHE = CacheBuilder.newBuilder().maximumSize(100000)
            .build(CacheLoader.from(OpenSimplex2S::new));

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
        final OpenSimplex2S openSimplex2S = CACHE.getUnchecked(seedProvider.apply(parent));
        final Vector2L dimensions = boundingBox.getDimensions();
        /* generate2 orders ret in ret[y][x], so we swap the coordinates of X and Z to get ret[x][z]. */
        final double[][] buf = new double[dimensions.getX().intValue()][dimensions.getZ().intValue()];
        openSimplex2S.generate2(context, buf, parentOffset.getZ().intValue(), parentOffset.getX().intValue());
        for(int x = 0; x < buf.length; x++)
            System.arraycopy(buf[x], 0, ret[x + retOffset.getX().intValue()], retOffset.getZ().intValue(),
                    buf[x].length);
    }
}
