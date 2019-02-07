package com.runetide.services.internal.region.server.domain;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.RemovalListener;
import com.runetide.common.TopicManager;
import com.runetide.common.util.Compressor;

import java.util.concurrent.TimeUnit;

public class LoadedRegion {
    private final Compressor compressor;
    private final TopicManager topicManager;

    private byte[][] compressedChunks;
    private Cache<Integer, LoadedChunk> loadedChunks;

    public LoadedRegion(final byte[][] compressedChunks) {
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

    private synchronized LoadedChunk decompress(final Integer key) {
        return new LoadedChunk(compressor.decompress(compressedChunks[key]));
    }

    private synchronized void compress(final Integer key, final LoadedChunk value) {
        this.compressedChunks[key] = compressor.compress(value.encode());
    }
}
