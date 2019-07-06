package com.runetide.services.internal.region.server.domain;

import com.runetide.common.Constants;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class LoadedChunk {
    private LoadedChunkSection[] loadedChunkSections = new LoadedChunkSection[Constants.CHUNK_SECTIONS_PER_CHUNK];
    private byte[] columns = new byte[Constants.COLUMNS_PER_CHUNK*3]; // x/z; 3 bytes for: top (12 bits), biome (12 bits)

    public LoadedChunk(final byte[] encoded) throws IOException {
        decode(encoded);
    }

    public void decode(final byte[] encoded) throws IOException {
        try(final DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(encoded))) {
            for (int i = 0; i < loadedChunkSections.length; i++)
                loadedChunkSections[i] = new LoadedChunkSection(dataInputStream);
            dataInputStream.readFully(columns);
        }
    }
}
