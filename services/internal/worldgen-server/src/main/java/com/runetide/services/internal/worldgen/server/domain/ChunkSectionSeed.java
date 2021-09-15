package com.runetide.services.internal.worldgen.server.domain;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.runetide.common.dto.BlockRef;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class ChunkSectionSeed extends SubSeed<ChunkSeed> {
    private static final byte[] BLOCK = "Block|".getBytes(StandardCharsets.UTF_8);

    private final LoadingCache<BlockRef, BlockSeed> blockCache = CacheBuilder.newBuilder()
            .expireAfterAccess(30, TimeUnit.SECONDS)
            .build(CacheLoader.from(blockRef -> new BlockSeed(this, ref(BLOCK, blockRef))));

    ChunkSectionSeed(final ChunkSeed parent, final byte[] seed) {
        super(parent, seed);
    }

    public BlockSeed block(final BlockRef blockRef) {
        return blockCache.getUnchecked(blockRef);
    }
}
