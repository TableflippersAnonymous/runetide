package com.runetide.services.internal.worldgen.server.domain;

import com.runetide.common.dto.BlockRef;

import java.nio.charset.StandardCharsets;

public class ChunkSectionSeed extends SubSeed<ChunkSeed> {
    private static final byte[] BLOCK = "Block|".getBytes(StandardCharsets.UTF_8);

    ChunkSectionSeed(final ChunkSeed parent, final byte[] seed) {
        super(parent, seed);
    }

    public BlockSeed block(final BlockRef blockRef) {
        return new BlockSeed(this, ref(BLOCK, blockRef));
    }
}
