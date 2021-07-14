package com.runetide.services.internal.worldgen.server.domain;

import com.runetide.common.dto.BlockRef;
import com.runetide.common.dto.ChunkSectionRef;

import java.nio.charset.StandardCharsets;

public class ChunkSeed extends SubSeed<RegionSeed> {
    private static final byte[] CHUNK_SECTION = "ChunkSection|".getBytes(StandardCharsets.UTF_8);

    ChunkSeed(final RegionSeed parent, final byte[] seed) {
        super(parent, seed);
    }

    public ChunkSectionSeed chunkSection(final ChunkSectionRef chunkSectionRef) {
        return new ChunkSectionSeed(this, ref(CHUNK_SECTION, chunkSectionRef));
    }

    public BlockSeed block(final BlockRef blockRef) {
        return chunkSection(blockRef.getChunkSectionRef()).block(blockRef);
    }
}
