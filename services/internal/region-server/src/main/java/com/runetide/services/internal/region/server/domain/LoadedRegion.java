package com.runetide.services.internal.region.server.domain;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.RemovalListener;
import com.runetide.common.TopicManager;
import com.runetide.common.util.Compressor;
import com.runetide.services.internal.region.common.RegionChunkData;
import com.runetide.services.internal.region.server.dto.Region;
import com.runetide.services.internal.region.server.dto.RegionData;

import java.util.concurrent.TimeUnit;

public class LoadedRegion {
    private final Compressor compressor;
    private final TopicManager topicManager;

    private final Region region;
    private final RegionData regionData;
    private byte[][] compressedChunks;
    private Cache<Integer, LoadedChunk> loadedChunks;

    public LoadedRegion(final Region region, final RegionData regionData, final byte[][] compressedChunks) {
        this.region = region;
        this.regionData = regionData;
        this.compressedChunks = compressedChunks;
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

    public RegionChunkData toClientRegion() {

        return new RegionChunkData(regionData.getVersion(), regionData.toRef(), region.toRef(), regionData.getCreated(),
                )
    }

    private synchronized LoadedChunk decompress(final Integer key) {
        return new LoadedChunk(compressor.decompress(compressedChunks[key]));
    }

    private synchronized void compress(final Integer key, final LoadedChunk value) {
        this.compressedChunks[key] = compressor.compress(value.encode());
    }
}
