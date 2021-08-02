package com.runetide.services.internal.region.server.domain;

import com.runetide.common.Constants;
import com.runetide.common.domain.BiomeType;
import com.runetide.common.dto.ChunkRef;
import com.runetide.common.dto.RegionRef;
import com.runetide.services.internal.region.common.Block;
import com.runetide.services.internal.region.common.Chunk;
import com.runetide.services.internal.region.common.ChunkSection;
import com.runetide.services.internal.region.common.Column;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.stream.IntStream;

public class LoadedChunk {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ChunkRef ref;
    private LoadedChunkSection[] loadedChunkSections = new LoadedChunkSection[Constants.CHUNK_SECTIONS_PER_CHUNK];
    private byte[] columns = new byte[Constants.COLUMNS_PER_CHUNK * Constants.BYTES_PER_COLUMN]; // x/z; 3 bytes for: top (12 bits), biome (12 bits)

    public LoadedChunk(final RegionRef regionRef, final int x, final int z, final byte[] encoded) throws IOException {
        this.ref = regionRef.chunk(x, z);
        decode(encoded);
    }

    private LoadedChunk(final ChunkRef ref, final LoadedChunkSection[] loadedChunkSections, final byte[] columns) {
        this.ref = ref;
        this.loadedChunkSections = loadedChunkSections;
        this.columns = columns;
    }

    private void decode(final byte[] encoded) throws IOException {
        try(final DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(encoded))) {
            for (int i = 0; i < loadedChunkSections.length; i++)
                loadedChunkSections[i] = new LoadedChunkSection(ref, i, dataInputStream);
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

    public synchronized byte[] encode() {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream)) {
            for(final LoadedChunkSection loadedChunkSection : loadedChunkSections)
                loadedChunkSection.encode(dataOutputStream);
            dataOutputStream.write(columns);
        } catch (final IOException e) {
            LOG.error("IO Exception encoding Chunk", e);
        }
        return byteArrayOutputStream.toByteArray();
    }

    public int getTop(final int x, final int z) {
        final int base = (x * Constants.COLUMNS_PER_CHUNK_Z + z) * Constants.BYTES_PER_COLUMN;
        return (((int) columns[base]) << 4) | (columns[base + 1] >> 4);
    }

    private void setTop(final int x, final int z, final int y) {
        final int base = (x * Constants.COLUMNS_PER_CHUNK_Z + z) * Constants.BYTES_PER_COLUMN;
        columns[base] = (byte)(y >> 4);
        columns[base + 1] = (byte)(((y & 0xf) << 4) | (columns[base + 1] & 0xf));
    }

    public Block getBlock(final int x, final int y, final int z) {
        final LoadedChunkSection chunkSection = getSection(y / Constants.BLOCKS_PER_CHUNK_SECTION_Y);
        return chunkSection.getBlock(x, y % Constants.BLOCKS_PER_CHUNK_SECTION_Y, z);
    }

    public synchronized void setBlock(final int x, final int y, final int z, final Block block) {
        final LoadedChunkSection chunkSection = getSection(y / Constants.BLOCKS_PER_CHUNK_SECTION_Y);
        chunkSection.setBlock(x, y % Constants.BLOCKS_PER_CHUNK_SECTION_Y, z, block);
        if(block.getType().isTransparent() && getTop(x, z) == y)
            setTop(x, z, IntStream.range(0, Constants.BLOCKS_PER_CHUNK_Y)
                    .map(h -> Constants.BLOCKS_PER_CHUNK_Y - h - 1)
                    .filter(h -> !getBlock(x, h, z).getType().isTransparent())
                    .findFirst()
                    .orElse(0));
        else if(!block.getType().isTransparent() && getTop(x, z) < y)
            setTop(x, z, y);
    }

    public synchronized LoadedChunk snapshot() {
        final byte[] columns = this.columns.clone();
        final LoadedChunkSection[] loadedChunkSections = Arrays.stream(this.loadedChunkSections)
                .map(LoadedChunkSection::snapshot).toArray(LoadedChunkSection[]::new);
        return new LoadedChunk(ref, loadedChunkSections, columns);
    }
}
