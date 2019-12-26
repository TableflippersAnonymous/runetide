package com.runetide.services.internal.region.server.services;

import com.runetide.common.*;
import com.runetide.common.dto.RegionRef;
import com.runetide.common.services.servicediscovery.ServiceData;
import com.runetide.services.internal.region.common.BulkBlockUpdateEntry;
import com.runetide.services.internal.region.common.BulkBlockUpdateRequest;
import com.runetide.services.internal.region.common.Chunk;
import com.runetide.services.internal.region.common.ChunkSection;
import com.runetide.services.internal.region.common.RegionChunkData;
import com.runetide.services.internal.region.server.domain.LoadedChunk;
import com.runetide.services.internal.region.server.domain.LoadedChunkSection;
import com.runetide.services.internal.region.server.domain.LoadedRegion;
import com.runetide.services.internal.region.common.BlockUpdateMessage;
import com.runetide.services.internal.region.server.dto.RegionChunkJournalEntry;
import com.runetide.services.internal.region.common.RegionLoadMessage;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.x.discovery.ServiceInstance;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

@Singleton
public class RegionManager extends UniqueLoadingManager<RegionRef, LoadedRegion> {
    private static final String REGION_NAMESPACE = "region";

    private final RegionLoader regionLoader;
    private final TopicManager topicManager;
    private final Journaler journaler;

    public RegionManager(final String myUrl, final LockManager lockManager, final ServiceRegistry serviceRegistry,
                         final ExecutorService executorService, final RedissonClient redissonClient,
                         final CuratorFramework curatorFramework, final RegionLoader regionLoader,
                         final TopicManager topicManager, final Journaler journaler) throws InterruptedException {
        super(myUrl, REGION_NAMESPACE, lockManager, serviceRegistry, executorService, redissonClient, curatorFramework);
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
        topicManager.publish("region:" + key + ":load", new RegionLoadMessage(key));
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
            topicManager.publish("region:" + regionRef + ":blockupdate", new BlockUpdateMessage(
                    regionRef.getWorldRef(), regionRef.getX(), regionRef.getZ(),
                    bulkBlockUpdateEntry.getCx(), bulkBlockUpdateEntry.getCz(), bulkBlockUpdateEntry.getSy(),
                    bulkBlockUpdateEntry.getBx(), bulkBlockUpdateEntry.getBy(), bulkBlockUpdateEntry.getBz(),
                    bulkBlockUpdateEntry.getBlock()
            ));
        }
    }
}
