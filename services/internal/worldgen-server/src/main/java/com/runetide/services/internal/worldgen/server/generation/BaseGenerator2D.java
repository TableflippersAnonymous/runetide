package com.runetide.services.internal.worldgen.server.generation;

public abstract class BaseGenerator2D {

    protected abstract void generateValues(final long sectorX, final long sectorZ,
                                           final int x, final int z, int[][] out);

    protected abstract long seed(final long sectorX, final long sectorZ);
}
