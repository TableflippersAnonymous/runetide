package com.runetide.services.internal.worldgen.server.domain;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.runetide.common.dto.BlockRef;
import com.runetide.common.dto.ChunkRef;
import com.runetide.common.dto.ChunkSectionRef;
import com.runetide.common.dto.RegionRef;
import com.runetide.common.dto.SectorRef;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class WorldSeed extends BaseSeed {
    private static final byte[] SECTOR = "Sector|".getBytes(StandardCharsets.UTF_8);
    private static final byte[] REGION = "Region|".getBytes(StandardCharsets.UTF_8);

    private final LoadingCache<SectorRef, SectorSeed> sectorCache = CacheBuilder.newBuilder()
            .expireAfterAccess(30, TimeUnit.SECONDS)
            .build(CacheLoader.from(sectorRef -> new SectorSeed(this, ref(SECTOR, sectorRef))));
    private final LoadingCache<RegionRef, RegionSeed> regionCache = CacheBuilder.newBuilder()
            .expireAfterAccess(30, TimeUnit.SECONDS)
            .build(CacheLoader.from(regionRef -> new RegionSeed(this, ref(REGION, regionRef))));

    public WorldSeed(final long seed) {
        super(seed);
    }

    public SectorSeed sector(final SectorRef sectorRef) {
        return sectorCache.getUnchecked(sectorRef);
    }

    public Function<SectorRef, Long> sector(final SeedPurpose purpose) {
        return sectorRef -> sector(sectorRef).getSeed(purpose);
    }

    public RegionSeed region(final RegionRef regionRef) {
        return regionCache.getUnchecked(regionRef);
    }

    public Function<RegionRef, Long> region(final SeedPurpose purpose) {
        return regionRef -> region(regionRef).getSeed(purpose);
    }

    public ChunkSeed chunk(final ChunkRef chunkRef) {
        return region(chunkRef.getRegionRef()).chunk(chunkRef);
    }

    public Function<ChunkRef, Long> chunk(final SeedPurpose purpose) {
        return chunkRef -> chunk(chunkRef).getSeed(purpose);
    }

    public ChunkSectionSeed chunkSection(final ChunkSectionRef chunkSectionRef) {
        return region(chunkSectionRef.getRegionRef()).chunkSection(chunkSectionRef);
    }

    public Function<ChunkSectionRef, Long> chunkSection(final SeedPurpose purpose) {
        return chunkSectionRef -> chunkSection(chunkSectionRef).getSeed(purpose);
    }

    public BlockSeed block(final BlockRef blockRef) {
        return region(blockRef.getRegionRef()).block(blockRef);
    }

    public Function<BlockRef, Long> block(final SeedPurpose purpose) {
        return blockRef -> block(blockRef).getSeed(purpose);
    }
}
