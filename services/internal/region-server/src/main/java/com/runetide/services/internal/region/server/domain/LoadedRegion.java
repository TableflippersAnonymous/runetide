package com.runetide.services.internal.region.server.domain;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.runetide.common.Constants;
import com.runetide.common.dto.*;
import com.runetide.common.util.Compressor;
import com.runetide.services.internal.region.common.Chunk;
import com.runetide.services.internal.region.common.InstanceTemplate;
import com.runetide.services.internal.region.common.RegionChunkData;
import com.runetide.services.internal.region.server.dto.Region;
import com.runetide.services.internal.region.server.dto.RegionData;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class LoadedRegion {
    private final Compressor compressor;

    private final Region region;
    private final RegionData regionData;
    private LoadingCache<Integer, LoadedChunk> loadedChunks;

    public LoadedRegion(final Compressor compressor, final Region region, final RegionData regionData) {
        this.compressor = compressor;
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
        final Chunk[][] chunks = new Chunk[Constants.CHUNKS_PER_REGION_X][Constants.CHUNKS_PER_REGION_Z];
        for(int x = 0; x < chunks.length; x++)
            for(int z = 0; z < chunks[x].length; z++)
                chunks[x][z] = getChunk(x, z).toClient();
        return new RegionChunkData(regionData.getVersion(), regionData.toRef(), region.toRef(),
                regionData.getTimestamp(), chunks);
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

    public ChunkDataRef getChunkDataRef() {
        return regionData.toRef();
    }

    public byte[][] quiesce() {
        final byte[][] compressedChunks;
        final Map<Integer, LoadedChunk> activeChunks;
        synchronized (this) {
            compressedChunks = Arrays.stream(regionData.getCompressedChunks())
                    .map(byte[]::clone).toArray(byte[][]::new);
            activeChunks = loadedChunks.asMap().entrySet().stream().collect(Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> entry.getValue().snapshot()
            ));
        }
        for(final Map.Entry<Integer, LoadedChunk> entry : activeChunks.entrySet())
            compressedChunks[entry.getKey()] = compressor.compress(entry.getValue().encode());
        return compressedChunks;
    }

    public RegionRef getRegionRef() {
        return region.toRef();
    }

    private synchronized LoadedChunk decompress(final Integer key) throws IOException {
        return new LoadedChunk(getRegionRef(), key / Constants.CHUNKS_PER_REGION_Z,
                key % Constants.CHUNKS_PER_REGION_Z, compressor.decompress(regionData.getCompressedChunks()[key]));
    }

    private synchronized void compress(final Integer key, final LoadedChunk value) {
        regionData.getCompressedChunks()[key] = compressor.compress(value.encode());
    }

    public LoadedChunk getChunk(final int cx, final int cz) {
        return loadedChunks.getUnchecked(chunkKey(cx, cz));
    }

    private int chunkKey(final int x, final int z) {
        return x * Constants.CHUNKS_PER_REGION_Z + z;
    }

    public void setChunkDataId(final UUID newId) {
        regionData.setId(newId);
    }
}
