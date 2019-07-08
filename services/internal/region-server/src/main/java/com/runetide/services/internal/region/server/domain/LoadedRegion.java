package com.runetide.services.internal.region.server.domain;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.RemovalListener;
import com.runetide.common.TopicManager;
import com.runetide.common.dto.DungeonRef;
import com.runetide.common.dto.InstanceRef;
import com.runetide.common.dto.SettlementRef;
import com.runetide.common.util.Compressor;
import com.runetide.services.internal.region.common.InstanceTemplate;
import com.runetide.services.internal.region.common.RegionChunkData;
import com.runetide.services.internal.region.server.dto.Region;
import com.runetide.services.internal.region.server.dto.RegionData;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class LoadedRegion {
    private final Compressor compressor;
    private final TopicManager topicManager;

    private final Region region;
    private final RegionData regionData;
    private Cache<Integer, LoadedChunk> loadedChunks;

    public LoadedRegion(final Region region, final RegionData regionData) {
        this.region = region;
        this.regionData = regionData;
        loadedChunks = CacheBuilder.newBuilder()
                .expireAfterAccess(1, TimeUnit.MINUTES)
                .removalListener((RemovalListener<Integer, LoadedChunk>) notification -> compress(notification.getKey(), notification.getValue()))
                .softValues()
                .build(new CacheLoader<Integer, LoadedChunk>() {
                    @Override
                    public LoadedChunk load(final Integer key) throws Exception {
                        return decompress(key);
                    }
                });
    }

    public RegionChunkData toClientRegionChunkData() {

        return new RegionChunkData(regionData.getVersion(), regionData.toRef(), region.toRef(),
                regionData.getTimestamp(),
                )
    }

    public com.runetide.services.internal.region.common.Region toClientRegion() {
        return new com.runetide.services.internal.region.common.Region(
                region.toRef().getWorldRef(), region.getX(), region.getZ(), regionData.toRef(),
                region.getInstanceIds().stream().map(InstanceRef::new).collect(Collectors.toList()),
                new InstanceTemplate(/* FIXME */), region.getDifficultyLevel(),
                region.getSettlementIds().stream().map(SettlementRef::new).collect(Collectors.toList()),
                region.getDungeonIds().stream().map(DungeonRef::new).collect(Collectors.toList())
        );
    }

    private synchronized LoadedChunk decompress(final Integer key) {
        return new LoadedChunk(compressor.decompress(regionData.getCompressedChunks()[key]));
    }

    private synchronized void compress(final Integer key, final LoadedChunk value) {
        regionData.getCompressedChunks()[key] = compressor.compress(value.encode());
    }
}
