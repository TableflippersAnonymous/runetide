package com.runetide.services.internal.region.server.services;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.runetide.common.dto.ChunkDataRef;
import com.runetide.services.internal.region.server.dto.RegionChunkJournalEntry;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.ByteArrayCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Objects;

@Singleton
public class Journaler {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RedissonClient redissonClient;
    private final ObjectMapper mapper = new ObjectMapper(new JsonFactory());

    @Inject
    public Journaler(final RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public void journal(final ChunkDataRef regionDataRef, final RegionChunkJournalEntry regionChunkJournalEntry) {
        final RList<byte[]> journal = redissonClient.getList("region:journal:" + regionDataRef, ByteArrayCodec.INSTANCE);
        try {
            journal.add(mapper.writeValueAsBytes(regionChunkJournalEntry));
        } catch (final JsonProcessingException e) {
            LOG.error("Caught exception serializing journal: {}", regionDataRef, e);
        }
    }

    public Iterable<RegionChunkJournalEntry> replay(final ChunkDataRef chunkDataRef) {
        final RList<byte[]> journal = redissonClient.getList("region:journal:" + chunkDataRef, ByteArrayCodec.INSTANCE);
        return journal.stream().map(b -> {
            try {
                return mapper.readValue(b, RegionChunkJournalEntry.class);
            } catch (final IOException e) {
                LOG.error("Caught exception deserializing journal: {} {}", chunkDataRef, b, e);
            }
            return null;
        }).filter(Objects::nonNull)::iterator;
    }

    public void delete(final ChunkDataRef chunkDataRef) {
        final RList<byte[]> journal = redissonClient.getList("region:journal:" + chunkDataRef, ByteArrayCodec.INSTANCE);
        journal.delete();
    }
}
