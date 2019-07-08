package com.runetide.services.internal.region.server.domain;

import com.runetide.common.Constants;
import com.runetide.services.internal.region.common.BiomeType;
import com.runetide.services.internal.region.common.Chunk;
import com.runetide.services.internal.region.common.ChunkSection;
import com.runetide.services.internal.region.common.Column;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;

public class LoadedChunk {
    private LoadedChunkSection[] loadedChunkSections = new LoadedChunkSection[Constants.CHUNK_SECTIONS_PER_CHUNK];
    private byte[] columns = new byte[Constants.COLUMNS_PER_CHUNK*3]; // x/z; 3 bytes for: top (12 bits), biome (12 bits)

    public LoadedChunk(final byte[] encoded) throws IOException {
        decode(encoded);
    }

    private void decode(final byte[] encoded) throws IOException {
        try(final DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(encoded))) {
            for (int i = 0; i < loadedChunkSections.length; i++)
                loadedChunkSections[i] = new LoadedChunkSection(dataInputStream);
            dataInputStream.readFully(columns);
        }
    }

    public Chunk toClient() {
        final ChunkSection[] chunkSections = Arrays.stream(loadedChunkSections)
                .map(LoadedChunkSection::toClient).toArray(ChunkSection[]::new);
        final Column[][] columns = new Column[Constants.COLUMNS_PER_CHUNK_X][Constants.COLUMNS_PER_CHUNK_Z];
        for(int x = 0; x < columns.length; x++) {
            for(int z = 0; z < columns[x].length; z++) {
                final int i = x * Constants.COLUMNS_PER_CHUNK_Z + z;
                columns[x][z] = new Column(
                        (this.columns[i * 3 + 2] << 4) + (this.columns[i * 3 + 1] >> 4),
                        BiomeType.values()[((this.columns[i * 3 + 1] & 0x0f) << 8) + this.columns[i * 3]]
                );
            }
        }
        return new Chunk(chunkSections, columns);
    }

    public LoadedChunkSection getSection(final int sy) {
        return loadedChunkSections[sy];
    }
}
