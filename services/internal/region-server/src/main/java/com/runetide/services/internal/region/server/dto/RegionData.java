package com.runetide.services.internal.region.server.dto;

import com.runetide.common.Constants;
import com.runetide.common.dto.ChunkDataRef;
import com.runetide.common.dto.RegionRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.UUID;

public class RegionData {
    public static final long CURRENT_VERSION = 0;
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final long version;
    private UUID id;
    private final RegionRef regionId;
    private final long timestamp;
    private final byte[][] compressedChunks;

    public RegionData(final long version, final UUID id, final RegionRef regionId, final long timestamp, final byte[][] compressedChunks) {
        this.version = version;
        this.id = id;
        this.regionId = regionId;
        this.timestamp = timestamp;
        this.compressedChunks = compressedChunks;
    }

    public static RegionData decode(final DataInputStream dataInputStream) throws IOException {
        final long version = dataInputStream.readLong();
        final long idLo = dataInputStream.readLong();
        final long idHi = dataInputStream.readLong();
        final UUID id = new UUID(idHi, idLo);
        final RegionRef regionRef = RegionRef.decode(dataInputStream);
        final long timestamp = dataInputStream.readLong();
        final byte[][] compressedChunks = new byte[Constants.CHUNKS_PER_REGION][];
        for(int i = 0; i < Constants.CHUNKS_PER_REGION; i++) {
            final byte[] compressedChunk = new byte[dataInputStream.readInt()];
            dataInputStream.readFully(compressedChunk);
            compressedChunks[i] = compressedChunk;
        }
        return new RegionData(version, id, regionRef, timestamp, compressedChunks);
    }

    public long getVersion() {
        return version;
    }

    public UUID getId() {
        return id;
    }

    public void setId(final UUID newId) {
        id = newId;
    }

    public RegionRef getRegionId() {
        return regionId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public byte[][] getCompressedChunks() {
        return compressedChunks;
    }

    public void encode(final DataOutputStream dataOutputStream) {
        try {
            dataOutputStream.writeLong(version);
            dataOutputStream.writeLong(id.getLeastSignificantBits());
            dataOutputStream.writeLong(id.getMostSignificantBits());
            regionId.encode(dataOutputStream);
            dataOutputStream.writeLong(timestamp);
            for (final byte[] compressedChunk : compressedChunks) {
                dataOutputStream.writeInt(compressedChunk.length);
                dataOutputStream.write(compressedChunk);
            }
        } catch (final IOException e) {
            LOG.error("IO Exception encoding Region: {}", id, e);
        }
    }

    public ChunkDataRef toRef() {
        return new ChunkDataRef(id);
    }
}
