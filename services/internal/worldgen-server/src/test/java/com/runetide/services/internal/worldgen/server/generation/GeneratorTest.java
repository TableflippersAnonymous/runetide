package com.runetide.services.internal.worldgen.server.generation;

import com.runetide.common.domain.geometry.locus.BoundingBox;
import com.runetide.common.domain.geometry.locus.FixedBoundingBoxSingle;
import com.runetide.common.domain.geometry.vector.Vector;
import com.runetide.common.domain.geometry.vector.Vector2L;
import com.runetide.common.dto.*;
import com.runetide.services.internal.worldgen.server.domain.WorldSeed;
import com.runetide.services.internal.worldgen.server.vendor.OpenSimplex2S;
import org.apache.commons.math3.analysis.interpolation.BicubicInterpolator;
import org.testng.annotations.Test;

import java.util.Optional;
import java.util.stream.StreamSupport;

public class GeneratorTest {
    private static final WorldRef WORLD = WorldRef.random();
    private static final WorldSeed WORLD_SEED = new WorldSeed(1234567890L);

    @Test
    public void testInterpolation() {
        final Generator2<RegionRef, long[]> sut = new InterpolatingGenerator2L<>(
                SectorRef.class, RegionRef.class, new BicubicInterpolator(),
                new Generator2FTo2L<>(new OpenSimplex2SGenerator2F<>(SectorRef.class, RegionRef.class, ref -> 0L,
                        new OpenSimplex2S.GenerateContext2D(OpenSimplex2S.LatticeOrientation2D.Standard, 0.1,
                                0.1, 1)), -1, 1, 0, 256),
                Vector.of(5, 5), Vector.of(3, 3));

        final int xStart = -100;
        final int xEnd = 100;
        final int zStart = -100;
        final int zEnd = 100;
        final int count = 1000;

        long[][] vals = sut.generate(BoundingBox.of(WORLD.region(xStart, zStart), WORLD.region(xEnd, zEnd)));
        long startTime = System.nanoTime();
        for(int i = 0; i < count; i++)
            vals = sut.generate(BoundingBox.of(WORLD.region(xStart, zStart), WORLD.region(xEnd, zEnd)));
        System.out.println("Duration = " + (System.nanoTime() - startTime) / count + "ns");
        printGrid(vals, Vector.of(xStart, zStart));
    }

    @Test
    public void testDifficulty() {
        final DifficultyGenerator sut = new DifficultyGenerator(WORLD_SEED);
        long[][] vals = sut.generate(BoundingBox.of(WORLD.region(-100, -100), WORLD.region(100, 100)));
        printGrid(vals, Vector.of(-100, -100));
    }

    @Test
    public void findDifficultyZero() {
        final DifficultyGenerator sut = new DifficultyGenerator(WORLD_SEED);
        final RegionRef origin = WORLD.region(0, 0);
        final var bb = StreamSupport.stream(BoundingBox.of(origin, WORLD.region(35, 35))
                .expandingOut(Optional.empty()).spliterator(), false)
                .filter(box -> {
                    System.out.println("Testing box: " + box);
                    final long[][] ret = sut.generate(box);
                    for(int x = 0; x < ret.length; x++)
                        for(int z = 0; z < ret[x].length; z++)
                            if(ret[x][z] == 0) {
                                System.out.println("Found at " + box.getStart().subtract(origin).add(Vector.of(x, z)));
                                return true;
                            }
                    return false;
                })
                .findFirst()
                .orElseThrow();
        final long[][] vals = sut.generate(bb);
        printGrid(vals, bb.getStart().subtract(origin));
    }

    @Test
    public void testElevationGeneration() {
        final ElevationGenerator sut = new ElevationGenerator(WORLD_SEED);
        long[][] vals = sut.generate(BoundingBox.of(WORLD.region(-100, -100), WORLD.region(100, 100)));
        printGrid(vals, Vector.of(-100, -100));
    }

    @Test
    public void testHeightGeneration() {
        final HeightGenerator sut = new HeightGenerator(WORLD_SEED);
        final ElevationGenerator elevation = new ElevationGenerator(WORLD_SEED);
        final SlopeGenerator slope = new SlopeGenerator(WORLD_SEED);
        final ColumnRef origin = WORLD.region(0, 0).chunk(0, 0).column(0, 0);
        final FixedBoundingBoxSingle<ColumnRef, Vector2L> columnBox = BoundingBox.of(origin)
                .outset(Vector.of(8*256, 8*256));
        final FixedBoundingBoxSingle<ChunkRef, Vector2L> chunkBox = columnBox.map(ColumnRef::getChunkRef);
        final FixedBoundingBoxSingle<RegionRef, Vector2L> regionBox = chunkBox.map(ChunkRef::getRegionRef);
        long[][] vals = sut.generate(columnBox);
        printGrid(vals, columnBox.getStart().offsetTo(origin));
        printGrid(slope.generate(chunkBox), chunkBox.getStart().offsetTo(origin));
        printGrid(elevation.generate(regionBox), regionBox.getStart().offsetTo(origin));
    }

    private void printGrid(final long[][] vals, final Vector2L start) {
        for(int x = 0; x < vals.length; x++) {
            final StringBuilder sb1 = new StringBuilder();
            final StringBuilder sb2 = new StringBuilder();
            for (int z = 0; z < vals[x].length; z++) {
                sb1.append(" | ").append(String.format("%4d", vals[x][z]));
                sb2.append("-+-----");
            }
            sb1.append("  (x").append(x + start.getX()).append(")");
            System.out.println(sb1);
            System.out.println(sb2);
        }

        final StringBuilder sb1 = new StringBuilder("z");

        for (int z = 0; z < vals[0].length; z++) {
            sb1.append("|").append(String.format("%6d", z + start.getZ()));
        }
        System.out.println(sb1);
    }

    private void printGrid(final double[][] vals, final Vector2L start) {
        for(int x = 0; x < vals.length; x++) {
            final StringBuilder sb1 = new StringBuilder();
            final StringBuilder sb2 = new StringBuilder();
            for (int z = 0; z < vals[x].length; z++) {
                sb1.append(" | ").append(String.format("%2.2f", vals[x][z]));
                sb2.append("-+-----");
            }
            sb1.append("  (x").append(x + start.getX()).append(")");
            System.out.println(sb1);
            System.out.println(sb2);
        }

        final StringBuilder sb1 = new StringBuilder("z");

        for (int z = 0; z < vals[0].length; z++) {
            sb1.append("|").append(String.format("%6d", z + start.getZ()));
        }
        System.out.println(sb1);
    }
}
