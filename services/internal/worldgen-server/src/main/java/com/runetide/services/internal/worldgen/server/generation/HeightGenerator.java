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
import org.apache.commons.math3.analysis.interpolation.PiecewiseBicubicSplineInterpolator;

import java.util.Optional;

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
        public long[][] generate(final FixedBoundingBoxSingle<ColumnRef, Vector2L> boundingBox) {
            final Vector2L dimensions = boundingBox.getDimensions();
            final long[][] ret = new long[dimensions.getX().intValue()][dimensions.getZ().intValue()];
            final FixedBoundingBoxSingle<ChunkRef, Vector2L> chunkBB = boundingBox.map(ColumnRef::getChunkRef);
            final FixedBoundingBoxSingle<RegionRef, Vector2L> regionBB = chunkBB.map(ChunkRef::getRegionRef);
            final long[][] elevation = elevationGenerator.generate(regionBB);
            final double[][] slope = slopeGenerator.generate(chunkBB);
            final long[][] heights = terrainHeightGenerator.generate(boundingBox);
            for(final RegionRef region : regionBB) {
                final Optional<FixedBoundingBoxSingle<ChunkRef, Vector2L>> chunksInRegion
                        = chunkBB.intersect(region.asBoundingBox());
                if(chunksInRegion.isEmpty())
                    continue;
                final Vector2L regionOffset = region.subtract(regionBB.getStart());
                final int xOffsetRegion = regionOffset.getX().intValue();
                final int zOffsetRegion = regionOffset.getZ().intValue();
                for(final ChunkRef chunk : chunksInRegion.get()) {
                    final Optional<FixedBoundingBoxSingle<ColumnRef, Vector2L>> columnsInChunk
                            = boundingBox.intersect(chunk.asColumnBoundingBox());
                    if(columnsInChunk.isEmpty())
                        continue;
                    final Vector2L chunkOffset = chunk.subtract(chunkBB.getStart());
                    final int xOffsetChunk = chunkOffset.getX().intValue();
                    final int zOffsetChunk = chunkOffset.getZ().intValue();
                    final Vector2L columnOffset = chunk.column(0, 0).subtract(boundingBox.getStart());
                    final int xOffset = columnOffset.getX().intValue();
                    final int zOffset = columnOffset.getZ().intValue();
                    final int xStart = columnsInChunk.get().getStart().getX();
                    final int xEnd = columnsInChunk.get().getEnd().getX();
                    final int zStart = columnsInChunk.get().getStart().getZ();
                    final int zEnd = columnsInChunk.get().getEnd().getZ();
                    for(int x = xStart; x < xEnd; x++) {
                        for(int z = zStart; z < zEnd; z++) {
                            ret[xOffset + x][zOffset + z] = elevation[xOffsetRegion][zOffsetRegion]
                                    + (long) (slope[xOffsetChunk][zOffsetChunk] * heights[xOffset + x][zOffset + z]) / 8;
                        }
                    }
                }
            }
            return ret;
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
        super(new InterpolatingGenerator2L<>(RegionRef.class, ColumnRef.class, new PiecewiseBicubicSplineInterpolator(),
                new InterpolatingGenerator2L<>(ChunkRef.class, ColumnRef.class, new PiecewiseBicubicSplineInterpolator(),
                        new Impl(worldSeed), Vector.of(4, 4), Vector.of(2, 2)),
                Vector.of(16, 16), Vector.of(4, 4)));
    }
}
