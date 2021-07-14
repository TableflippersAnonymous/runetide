package com.runetide.services.internal.worldgen.server.domain;

public class SectorSeed extends SubSeed<WorldSeed> {
    SectorSeed(final WorldSeed parent, final byte[] seed) {
        super(parent, seed);
    }
}
