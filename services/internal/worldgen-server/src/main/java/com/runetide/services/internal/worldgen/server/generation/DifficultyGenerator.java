package com.runetide.services.internal.worldgen.server.generation;

import com.runetide.common.dto.RegionRef;
import com.runetide.services.internal.worldgen.server.vendor.OpenSimplex2S;

public class DifficultyGenerator {
    private final long seed;

    public DifficultyGenerator(final long seed) {
        this.seed = seed;
    }

    public int getDifficulty(final RegionRef regionRef) {
        final OpenSimplex2S generator = new OpenSimplex2S(seed);
        final double[][] buf = new double[120][40];
        final int max = 256;
        final int ampl = max / 2;
        generator.generate2(
                new OpenSimplex2S.GenerateContext2D(OpenSimplex2S.LatticeOrientation2D.Standard, .01, .01, ampl),
                buf, 8880, 200);
        return 0;
    }
}
