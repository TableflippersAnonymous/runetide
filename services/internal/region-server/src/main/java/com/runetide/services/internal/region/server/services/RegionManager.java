package com.runetide.services.internal.region.server.services;

import com.runetide.common.dto.RegionRef;
import com.runetide.services.internal.region.common.BulkBlockUpdateEntry;
import com.runetide.services.internal.region.common.BulkBlockUpdateRequest;
import com.runetide.services.internal.region.common.Chunk;
import com.runetide.services.internal.region.common.ChunkSection;
import com.runetide.services.internal.region.common.RegionChunkData;
import com.runetide.services.internal.region.server.domain.LoadedChunk;
import com.runetide.services.internal.region.server.domain.LoadedChunkSection;
import com.runetide.services.internal.region.server.domain.LoadedRegion;

import javax.inject.Singleton;
import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutorService;

@Singleton
public class RegionManager {
    private final ExecutorService executorService;
    private final RegionLoader regionLoader;
    private final Map<RegionRef, LoadedRegion> loadedRegions;

    public URI queueLoad(final RegionRef region) {
        executorService.submit(()->load(region));
        return null; //FIXME
    }

    public boolean queueUnload(final RegionRef regionRef) {
        return false; //FIXME
    }

    private void load(final RegionRef region) {
        final LoadedRegion loadedRegion = regionLoader.load(region);
        loadedRegions.put(region, loadedRegion);
    }

    public Collection<LoadedRegion> getLoadedRegions() {
        return loadedRegions.values();
    }

    public LoadedRegion getLoadedRegion(final RegionRef regionRef) {
        return loadedRegions.get(regionRef);
    }

    public RegionChunkData getRegionData(final RegionRef regionRef) {
        return getLoadedRegion(regionRef).toClientRegionChunkData();
    }

    public Chunk getChunk(final RegionRef regionRef, final int cx, final int cz) {
        return getLoadedRegion(regionRef).getChunk(cx, cz).toClient();
    }

    public ChunkSection getChunkSection(final RegionRef regionRef, final int cx, final int cz, final int sy) {
        return getLoadedRegion(regionRef).getChunk(cx, cz).getSection(sy).toClient();
    }

    public void update(final RegionRef regionRef, final BulkBlockUpdateRequest bulkBlockUpdateRequest) {
        final LoadedRegion loadedRegion = getLoadedRegion(regionRef);
        for(final BulkBlockUpdateEntry bulkBlockUpdateEntry : bulkBlockUpdateRequest.getUpdates()) {
            final LoadedChunk chunk = loadedRegion.getChunk(bulkBlockUpdateEntry.getCx(), bulkBlockUpdateEntry.getCz());
            final LoadedChunkSection chunkSection = chunk.getSection(bulkBlockUpdateEntry.getSy());
            journal(regionRef, )
            chunkSection.setBlock(bulkBlockUpdateEntry.getBx(), bulkBlockUpdateEntry.getBy(),
                    bulkBlockUpdateEntry.getBz(), bulkBlockUpdateEntry.getBlock());
        }
    }
}
