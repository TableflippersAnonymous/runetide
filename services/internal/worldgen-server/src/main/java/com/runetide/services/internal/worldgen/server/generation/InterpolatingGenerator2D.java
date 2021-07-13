package com.runetide.services.internal.worldgen.server.generation;

import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;

public class InterpolatingGenerator2D extends BaseGenerator2D {
    private final UnivariateInterpolator interpolator;
    private final BaseGenerator2D generator;

    public InterpolatingGenerator2D(UnivariateInterpolator interpolator, BaseGenerator2D generator) {
        this.interpolator = interpolator;
        this.generator = generator;
    }


    @Override
    protected void generateValues(long sectorX, long sectorZ, int x, int z, int[][] out) {
        generator.generateValues();
    }

    @Override
    protected long seed(long sectorX, long sectorZ) {
        return 0;
    }
}
