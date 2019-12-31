package com.runetide.services.internal.region.server.services;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import com.runetide.common.Constants;
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
import java.util.Set;

@Singleton
public class Journaler {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RedissonClient redissonClient;
    private final ObjectMapper mapper = new ObjectMapper(new JsonFactory());
    private final SetMultimap<ChunkDataRef, ChunkDataRef> replicationMap = Multimaps.synchronizedSetMultimap(HashMultimap.create());

    @Inject
    public Journaler(final RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public void journal(final ChunkDataRef regionDataRef, final RegionChunkJournalEntry regionChunkJournalEntry) {
        journalOne(regionDataRef, regionChunkJournalEntry);
        final Set<ChunkDataRef> replicationChildren = replicationMap.get(regionDataRef);
        if(replicationChildren == null)
            return;
        for(final ChunkDataRef childDataRef : replicationChildren)
            journalOne(childDataRef, regionChunkJournalEntry);
    }

    public Iterable<RegionChunkJournalEntry> replay(final ChunkDataRef chunkDataRef) {
        final RList<byte[]> journal = redissonClient.getList(Constants.REGION_JOURNAL_PREFIX + chunkDataRef, ByteArrayCodec.INSTANCE);
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
        final RList<byte[]> journal = redissonClient.getList(Constants.REGION_JOURNAL_PREFIX + chunkDataRef, ByteArrayCodec.INSTANCE);
        journal.delete();
        replicationMap.removeAll(chunkDataRef);
    }

    public void beginReplication(final ChunkDataRef oldChunkDataRef, final ChunkDataRef newChunkDataRef) {
        replicationMap.put(oldChunkDataRef, newChunkDataRef);
    }

    public void endReplication(final ChunkDataRef oldChunkDataRef, final ChunkDataRef newChunkDataRef) {
        replicationMap.remove(oldChunkDataRef, newChunkDataRef);
    }

    private void journalOne(final ChunkDataRef regionDataRef, final RegionChunkJournalEntry regionChunkJournalEntry) {
        final RList<byte[]> journal = redissonClient.getList(Constants.REGION_JOURNAL_PREFIX + regionDataRef, ByteArrayCodec.INSTANCE);
        try {
            journal.add(mapper.writeValueAsBytes(regionChunkJournalEntry));
        } catch (final JsonProcessingException e) {
            LOG.error("Caught exception serializing journal: {}", regionDataRef, e);
        }
    }
}
