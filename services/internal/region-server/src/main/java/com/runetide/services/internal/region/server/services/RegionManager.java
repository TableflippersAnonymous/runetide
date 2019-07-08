package com.runetide.services.internal.region.server.services;

import com.runetide.common.Constants;
import com.runetide.common.TopicManager;
import com.runetide.common.dto.RegionRef;
import com.runetide.services.internal.region.common.BulkBlockUpdateEntry;
import com.runetide.services.internal.region.common.BulkBlockUpdateRequest;
import com.runetide.services.internal.region.common.Chunk;
import com.runetide.services.internal.region.common.ChunkSection;
import com.runetide.services.internal.region.common.RegionChunkData;
import com.runetide.services.internal.region.server.domain.LoadedChunk;
import com.runetide.services.internal.region.server.domain.LoadedChunkSection;
import com.runetide.services.internal.region.server.domain.LoadedRegion;
import com.runetide.services.internal.region.server.dto.BlockUpdateMessage;
import com.runetide.services.internal.region.server.dto.RegionChunkJournalEntry;
import com.runetide.services.internal.region.server.dto.RegionLoadMessage;

import javax.inject.Singleton;
import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutorService;

@Singleton
public class RegionManager {
    private final ExecutorService executorService;
    private final RegionLoader regionLoader;
    private final TopicManager topicManager;
    private final Map<RegionRef, LoadedRegion> loadedRegions;

    public URI queueLoad(final RegionRef region) {
        executorService.submit(()->load(region));
        return null; //FIXME
    }

    public boolean queueUnload(final RegionRef regionRef) {
        if(!loadedRegions.containsKey(regionRef))
            return false;
        executorService.submit(()->unload(regionRef));
        return true;
    }

    private void load(final RegionRef region) {
        final LoadedRegion loadedRegion = regionLoader.load(region);
        loadedRegions.put(region, loadedRegion);
        topicManager.publish("region:" + region + ":load", new RegionLoadMessage(region));
    }

    private void unload(final RegionRef regionRef) {

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

    public synchronized void update(final RegionRef regionRef, final BulkBlockUpdateRequest bulkBlockUpdateRequest) {
        final LoadedRegion loadedRegion = getLoadedRegion(regionRef);
        for(final BulkBlockUpdateEntry bulkBlockUpdateEntry : bulkBlockUpdateRequest.getUpdates()) {
            final LoadedChunk chunk = loadedRegion.getChunk(bulkBlockUpdateEntry.getCx(), bulkBlockUpdateEntry.getCz());
            final LoadedChunkSection chunkSection = chunk.getSection(bulkBlockUpdateEntry.getSy());
            journal(regionRef, new RegionChunkJournalEntry(
                    bulkBlockUpdateEntry.getCx() * Constants.BLOCKS_PER_CHUNK_SECTION_X + bulkBlockUpdateEntry.getBx(),
                    bulkBlockUpdateEntry.getSy() * Constants.BLOCKS_PER_CHUNK_SECTION_Y + bulkBlockUpdateEntry.getBy(),
                    bulkBlockUpdateEntry.getCz() * Constants.BLOCKS_PER_CHUNK_SECTION_Z + bulkBlockUpdateEntry.getBz(),
                    bulkBlockUpdateEntry.getBlock()
            ));
            chunkSection.setBlock(bulkBlockUpdateEntry.getBx(), bulkBlockUpdateEntry.getBy(),
                    bulkBlockUpdateEntry.getBz(), bulkBlockUpdateEntry.getBlock());
            topicManager.publish("region:" + regionRef + ":blockupdate", new BlockUpdateMessage(
                    regionRef.getWorldRef(), regionRef.getX(), regionRef.getZ(),
                    bulkBlockUpdateEntry.getCx(), bulkBlockUpdateEntry.getCz(), bulkBlockUpdateEntry.getSy(),
                    bulkBlockUpdateEntry.getBx(), bulkBlockUpdateEntry.getBy(), bulkBlockUpdateEntry.getBz(),
                    bulkBlockUpdateEntry.getBlock()
            ));
        }
    }

    private void journal(final RegionRef regionRef, final RegionChunkJournalEntry regionChunkJournalEntry) {
        //FIXME
    }
}
