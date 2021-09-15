package com.runetide.services.internal.worldgen.server.domain;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.runetide.common.dto.BlockRef;
import com.runetide.common.dto.ChunkSectionRef;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class ChunkSeed extends SubSeed<RegionSeed> {
    private static final byte[] CHUNK_SECTION = "ChunkSection|".getBytes(StandardCharsets.UTF_8);

    private final LoadingCache<ChunkSectionRef, ChunkSectionSeed> chunkSectionCache = CacheBuilder.newBuilder()
            .expireAfterAccess(30, TimeUnit.SECONDS)
            .build(CacheLoader.from(chunkSectionRef -> new ChunkSectionSeed(this, ref(CHUNK_SECTION, chunkSectionRef))));

    ChunkSeed(final RegionSeed parent, final byte[] seed) {
        super(parent, seed);
    }

    public ChunkSectionSeed chunkSection(final ChunkSectionRef chunkSectionRef) {
        return chunkSectionCache.getUnchecked(chunkSectionRef);
    }

    public BlockSeed block(final BlockRef blockRef) {
        return chunkSection(blockRef.getChunkSectionRef()).block(blockRef);
    }
}
