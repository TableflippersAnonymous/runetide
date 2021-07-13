package com.runetide.services.internal.worldgen.server;

import com.runetide.common.dto.SectorRef;

public class WorldSeed {
    private final long seed;

    public WorldSeed(final long seed) {
        this.seed = seed;
    }

    public long getWorldSeed() {
        return seed;
    }

    public SectorSeed sector(final SectorRef sectorRef) {

    }
}
