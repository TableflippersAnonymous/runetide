package com.runetide.services.internal.worldgen.server.generation;

import com.runetide.common.Constants;
import com.runetide.common.domain.geometry.FixedBoundingBoxSingle;
import com.runetide.common.domain.geometry.Vector2L;
import com.runetide.common.dto.Ref;
import com.runetide.common.domain.geometry.FixedPoint;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;

public class InterpolatingGenerator2D<SeedType extends Ref<SeedType>, PointType extends FixedPoint<PointType, Vector2L>>
        extends BaseGenerator2D<SeedType, PointType> {
    private final UnivariateInterpolator interpolator;
    private final BaseGenerator2D<SeedType, PointType> generator;
    private final Vector2L interpolateBorder;
    private final Vector2L interpolateDistance;
    private final Vector2L scale;
    private final Vector2L sectorSize;

    public InterpolatingGenerator2D(final UnivariateInterpolator interpolator,
                                    final BaseGenerator2D<SeedType, PointType> generator, final Vector2L interpolateBorder,
                                    final Vector2L interpolateDistance, final Vector2L scale) {
        this.interpolator = interpolator;
        this.generator = generator;
        this.interpolateBorder = interpolateBorder;
        this.interpolateDistance = interpolateDistance;
        this.scale = scale;
        this.sectorSize = Constants.REGIONS_PER_SECTOR_VEC.scale(Constants.CHUNKS_PER_REGION_VEC)
                .scale(Constants.CHUNK_SECTIONS_PER_CHUNK_VEC).scale(Constants.BLOCKS_PER_CHUNK_SECTION_VEC)
                .divide(scale);

    }

    @Override
    protected void generateValues(final FixedBoundingBoxSingle<PointType, Vector2L> boundingBox, final int[][] out) {
        generator.generateValues(boundingBox, out);

        /* At this point, out contains the filled output from the child generator.  We need to check if any of the
         * elements of out are within interpolateBorder from the edge of sectorRef.
         */

        if(!needInterpolation(sectorRef, start))
            return;

        interpolator.interpolate()
        sectorRef
    }

    @Override
    protected long seed(final SeedType point) {
        return generator.seed(point);
    }
}
