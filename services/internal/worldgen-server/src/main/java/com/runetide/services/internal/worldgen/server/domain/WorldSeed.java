package com.runetide.services.internal.worldgen.server.domain;

import com.runetide.common.dto.BlockRef;
import com.runetide.common.dto.ChunkRef;
import com.runetide.common.dto.ChunkSectionRef;
import com.runetide.common.dto.RegionRef;
import com.runetide.common.dto.SectorRef;

import java.nio.charset.StandardCharsets;

public class WorldSeed extends BaseSeed {
    private static final byte[] SECTOR = "Sector|".getBytes(StandardCharsets.UTF_8);
    private static final byte[] REGION = "Region|".getBytes(StandardCharsets.UTF_8);

    public WorldSeed(final long seed) {
        super(seed);
    }

    public SectorSeed sector(final SectorRef sectorRef) {
        return new SectorSeed(this, ref(SECTOR, sectorRef));
    }

    public RegionSeed region(final RegionRef regionRef) {
        return new RegionSeed(this, ref(REGION, regionRef));
    }

    public ChunkSeed chunk(final ChunkRef chunkRef) {
        return region(chunkRef.getRegionRef()).chunk(chunkRef);
    }

    public ChunkSectionSeed chunkSection(final ChunkSectionRef chunkSectionRef) {
        return region(chunkSectionRef.getRegionRef()).chunkSection(chunkSectionRef);
    }

    public BlockSeed block(final BlockRef blockRef) {
        return region(blockRef.getRegionRef()).block(blockRef);
    }
}
