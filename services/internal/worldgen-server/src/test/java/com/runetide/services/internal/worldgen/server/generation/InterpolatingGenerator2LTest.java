package com.runetide.services.internal.worldgen.server.generation;

import com.runetide.common.domain.geometry.locus.BoundingBox;
import com.runetide.common.domain.geometry.vector.Vector;
import com.runetide.common.dto.RegionRef;
import com.runetide.common.dto.SectorRef;
import com.runetide.common.dto.WorldRef;
import com.runetide.services.internal.worldgen.server.vendor.OpenSimplex2S;
import org.apache.commons.math3.analysis.interpolation.BicubicInterpolator;
import org.testng.annotations.Test;

public class InterpolatingGenerator2LTest {
    @Test
    public void testInterpolation() {
        final Generator2<RegionRef, long[]> sut = new InterpolatingGenerator2L<>(
                SectorRef.class, RegionRef.class, new BicubicInterpolator(),
                new Generator2FTo2L<>(new OpenSimplex2SGenerator2F<>(SectorRef.class, RegionRef.class, ref -> 0L,
                        new OpenSimplex2S.GenerateContext2D(OpenSimplex2S.LatticeOrientation2D.Standard, 0.1,
                                0.1, 1)), -1, 1, 0, 256),
                Vector.of(5, 5), Vector.of(3, 3));

        final WorldRef world = WorldRef.random();

        long[][] vals = sut.generate(BoundingBox.of(world.region(-10, -10), world.region(10, 10)));
        long startTime = System.nanoTime();
        for(int i = 0; i < 10000; i++)
            vals = sut.generate(BoundingBox.of(world.region(-10, -10), world.region(10, 10)));
        System.out.println("Duration = " + (System.nanoTime() - startTime) / 10000F + "ns");

        for(int x = 0; x < vals.length; x++) {
            final StringBuilder sb1 = new StringBuilder();
            final StringBuilder sb2 = new StringBuilder();
            for (int z = 0; z < vals[x].length; z++) {
                sb1.append(" | ").append(String.format("%3d", vals[x][z]));
                sb2.append("-+----");
            }
            sb1.append("  (x").append(x - 10).append(")");
            System.out.println(sb1);
            System.out.println(sb2);
        }

        final StringBuilder sb1 = new StringBuilder("z");

        for (int z = 0; z < vals[0].length; z++) {
            sb1.append("|").append(String.format("(%3d)", z - 10));
        }
        System.out.println(sb1);
    }
}
