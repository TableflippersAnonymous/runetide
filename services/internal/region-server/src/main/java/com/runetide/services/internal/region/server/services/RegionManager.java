package com.runetide.services.internal.region.server.services;

import com.runetide.common.*;
import com.runetide.common.dto.RegionRef;
import com.runetide.services.internal.region.common.*;
import com.runetide.services.internal.region.server.domain.LoadedChunk;
import com.runetide.services.internal.region.server.domain.LoadedRegion;
import com.runetide.services.internal.region.server.dto.RegionChunkJournalEntry;
import org.apache.curator.framework.CuratorFramework;
import org.redisson.api.RedissonClient;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Singleton
public class RegionManager extends SavingUniqueLoadingManager<RegionRef, LoadedRegion> {
    private final RegionLoader regionLoader;
    private final TopicManager topicManager;
    private final Journaler journaler;

    @Inject
    public RegionManager(@Named("myUrl") final String myUrl, final LockManager lockManager,
                         final ServiceRegistry serviceRegistry, final ScheduledExecutorService executorService,
                         final RedissonClient redissonClient, final CuratorFramework curatorFramework,
                         final RegionLoader regionLoader, final TopicManager topicManager,
                         final Journaler journaler) throws Exception {
        super(myUrl, Constants.REGION_LOADING_NAMESPACE, Constants.SAVE_RATE_MS, TimeUnit.MILLISECONDS, lockManager,
                serviceRegistry, executorService, redissonClient, curatorFramework);
        this.regionLoader = regionLoader;
        this.topicManager = topicManager;
        this.journaler = journaler;
    }

    @Override
    protected LoadedRegion handleLoad(final RegionRef key) throws IOException {
        return regionLoader.load(key);
    }

    @Override
    protected void postLoad(RegionRef key, LoadedRegion value) {
        topicManager.publish(Constants.REGION_TOPIC_PREFIX + key + ":load", new RegionLoadMessage(key));
    }

    @Override
    protected void postUnload(RegionRef key) {

    }

    @Override
    protected void handleUnload(RegionRef key, LoadedRegion value) throws IOException {
        regionLoader.save(value);
    }

    @Override
    protected void handleReset() {

    }

    @Override
    protected void handleSuspend() {

    }

    @Override
    protected void handleResume() {

    }

    @Override
    protected RegionRef keyFromString(String key) {
        return RegionRef.valueOf(key);
    }

    @Override
    protected void handleSave(RegionRef key, LoadedRegion value) throws IOException {
        regionLoader.save(value);
    }

    public Collection<LoadedRegion> getLoadedRegions() {
        return loaded.values();
    }

    public LoadedRegion getLoadedRegion(final RegionRef regionRef) {
        return loaded.get(regionRef);
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
        awaitLive();
        final LoadedRegion loadedRegion = getLoadedRegion(regionRef);
        for(final BulkBlockUpdateEntry bulkBlockUpdateEntry : bulkBlockUpdateRequest.getUpdates()) {
            final LoadedChunk chunk = loadedRegion.getChunk(bulkBlockUpdateEntry.getCx(), bulkBlockUpdateEntry.getCz());
            journaler.journal(loadedRegion.getChunkDataRef(), new RegionChunkJournalEntry(
                    bulkBlockUpdateEntry.getCx() * Constants.BLOCKS_PER_CHUNK_SECTION_X + bulkBlockUpdateEntry.getBx(),
                    bulkBlockUpdateEntry.getSy() * Constants.BLOCKS_PER_CHUNK_SECTION_Y + bulkBlockUpdateEntry.getBy(),
                    bulkBlockUpdateEntry.getCz() * Constants.BLOCKS_PER_CHUNK_SECTION_Z + bulkBlockUpdateEntry.getBz(),
                    bulkBlockUpdateEntry.getBlock()
            ));
            chunk.setBlock(bulkBlockUpdateEntry.getBx(),
                    bulkBlockUpdateEntry.getSy() * Constants.BLOCKS_PER_CHUNK_SECTION_Y + bulkBlockUpdateEntry.getBy(),
                    bulkBlockUpdateEntry.getBz(), bulkBlockUpdateEntry.getBlock());
            topicManager.publish(Constants.REGION_TOPIC_PREFIX + regionRef + ":blockupdate", new BlockUpdateMessage(
                    regionRef.getWorldRef(), regionRef.getX(), regionRef.getZ(),
                    bulkBlockUpdateEntry.getCx(), bulkBlockUpdateEntry.getCz(), bulkBlockUpdateEntry.getSy(),
                    bulkBlockUpdateEntry.getBx(), bulkBlockUpdateEntry.getBy(), bulkBlockUpdateEntry.getBz(),
                    bulkBlockUpdateEntry.getBlock()
            ));
        }
    }
}
