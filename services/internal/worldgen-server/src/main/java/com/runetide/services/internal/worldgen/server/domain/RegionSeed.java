package com.runetide.services.internal.worldgen.server.domain;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.runetide.common.dto.BlockRef;
import com.runetide.common.dto.ChunkRef;
import com.runetide.common.dto.ChunkSectionRef;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class RegionSeed extends SubSeed<WorldSeed> {
    private static final byte[] CHUNK = "Chunk|".getBytes(StandardCharsets.UTF_8);

    private final LoadingCache<ChunkRef, ChunkSeed> chunkCache = CacheBuilder.newBuilder()
            .expireAfterAccess(30, TimeUnit.SECONDS)
            .build(CacheLoader.from(chunkRef -> new ChunkSeed(this, ref(CHUNK, chunkRef))));

    RegionSeed(final WorldSeed parent, final byte[] seed) {
        super(parent, seed);
    }

    public ChunkSeed chunk(final ChunkRef chunkRef) {
        return new ChunkSeed(this, ref(CHUNK, chunkRef));
    }

    public ChunkSectionSeed chunkSection(final ChunkSectionRef chunkSectionRef) {
        return chunk(chunkSectionRef.getChunkRef()).chunkSection(chunkSectionRef);
    }

    public BlockSeed block(final BlockRef blockRef) {
        return chunk(blockRef.getChunkRef()).block(blockRef);
    }
}
