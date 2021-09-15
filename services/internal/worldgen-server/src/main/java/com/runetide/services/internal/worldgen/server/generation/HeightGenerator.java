package com.runetide.services.internal.worldgen.server.generation;

import com.runetide.common.domain.geometry.locus.BoundingBox;
import com.runetide.common.domain.geometry.locus.FixedBoundingBoxSingle;
import com.runetide.common.domain.geometry.vector.Vector;
import com.runetide.common.domain.geometry.vector.Vector2L;
import com.runetide.common.dto.ChunkRef;
import com.runetide.common.dto.ColumnRef;
import com.runetide.common.dto.RegionRef;
import com.runetide.common.dto.SectorRef;
import com.runetide.services.internal.worldgen.server.domain.SeedPurpose;
import com.runetide.services.internal.worldgen.server.domain.WorldSeed;
import org.apache.commons.math3.analysis.interpolation.BicubicInterpolator;

public class HeightGenerator extends DelegatingGenerator2<ColumnRef, long[]> {
    private static class Impl extends BaseGenerator2<ChunkRef, ColumnRef, long[]> {
        private final Generator2<RegionRef, long[]> elevationGenerator;
        private final Generator2<ChunkRef, double[]> slopeGenerator;
        private final Generator2<ColumnRef, long[]> terrainHeightGenerator;

        protected Impl(final WorldSeed worldSeed) {
            super(vec -> new long[vec.getX().intValue()][vec.getZ().intValue()], ChunkRef.class, ColumnRef.class);
            elevationGenerator = new ElevationGenerator(worldSeed);
            slopeGenerator = new SlopeGenerator(worldSeed);
            terrainHeightGenerator = new OpenSimplex2SGenerator2L<>(SectorRef.class, ColumnRef.class,
                    worldSeed.sector(SeedPurpose.HEIGHT), 0.001, -128 * 8, 128 * 8);
        }

        @Override
        protected void generateValues(final long[][] ret, final ChunkRef chunkRef,
                                      final FixedBoundingBoxSingle<ColumnRef, Vector2L> boundingBox,
                                      final FixedBoundingBoxSingle<ColumnRef, Vector2L> parentBox,
                                      final Vector2L retOffset, final Vector2L parentOffset) {
            final long elevation = elevationGenerator.generate(BoundingBox.of(chunkRef.getParent()))[0][0];
            final double slope = slopeGenerator.generate(BoundingBox.of(chunkRef))[0][0];
            final long[][] heights = terrainHeightGenerator.generate(boundingBox);
            final Vector2L dimensions = boundingBox.getDimensions();
            final int xEnd = dimensions.getX().intValue();
            final int zEnd = dimensions.getZ().intValue();
            final int xOffset = retOffset.getX().intValue();
            final int zOffset = retOffset.getZ().intValue();
            for(int x = 0; x < xEnd; x++)
                for(int z = 0; z < zEnd; z++)
                    ret[xOffset + x][zOffset + z] = elevation + (long) (slope * heights[x][z]) / 8;
        }
    }

    public HeightGenerator(final WorldSeed worldSeed) {
        super(new InterpolatingGenerator2L<>(RegionRef.class, ColumnRef.class, new BicubicInterpolator(),
                new InterpolatingGenerator2L<>(ChunkRef.class, ColumnRef.class, new BicubicInterpolator(),
                        new Impl(worldSeed), Vector.of(4, 4), Vector.of(2, 2)),
                Vector.of(16, 16), Vector.of(4, 4)));
    }
}
