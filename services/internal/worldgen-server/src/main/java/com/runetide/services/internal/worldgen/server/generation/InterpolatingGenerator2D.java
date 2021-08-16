package com.runetide.services.internal.worldgen.server.generation;

import com.runetide.common.Constants;
import com.runetide.common.domain.geometry.BoundingBox;
import com.runetide.common.domain.geometry.Vector2D;
import com.runetide.common.dto.Ref;
import com.runetide.common.domain.geometry.Point;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;

public class InterpolatingGenerator2D<SeedType extends Ref<SeedType>, PointType extends Point<PointType, Vector2D>>
        extends BaseGenerator2D<SeedType, PointType> {
    private final UnivariateInterpolator interpolator;
    private final BaseGenerator2D<SeedType, PointType> generator;
    private final Vector2D interpolateBorder;
    private final Vector2D interpolateDistance;
    private final Vector2D scale;
    private final Vector2D sectorSize;

    public InterpolatingGenerator2D(final UnivariateInterpolator interpolator,
                                    final BaseGenerator2D<SeedType, PointType> generator, final Vector2D interpolateBorder,
                                    final Vector2D interpolateDistance, final Vector2D scale) {
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
    protected void generateValues(final BoundingBox<PointType, Vector2D> boundingBox, final int[][] out) {
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
