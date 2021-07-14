package com.runetide.services.internal.worldgen.server.domain;

public class BlockSeed extends SubSeed<ChunkSectionSeed> {
    BlockSeed(final ChunkSectionSeed parent, final byte[] seed) {
        super(parent, seed);
    }
}
