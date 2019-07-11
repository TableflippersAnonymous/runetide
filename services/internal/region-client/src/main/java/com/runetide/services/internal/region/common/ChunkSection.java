package com.runetide.services.internal.region.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.runetide.common.Constants;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChunkSection {
    @JsonProperty("b")
    private byte[] blocks;
    @JsonProperty("d")
    private byte[] data;
    @JsonProperty("l")
    private byte[] light;
    @JsonIgnore
    private transient Map<Integer, String>[] decodedData;

    public ChunkSection() {
    }

    public ChunkSection(final byte[] blocks, final byte[] data, final byte[] light) {
        this.blocks = blocks;
        this.light = light;
        this.data = data;
    }

    public Block blockAt(final int x, final int y, final int z) {
        return new Block(blockTypeAt(x, y, z), blockVariantAt(x, y, z), blockDataAt(x, y, z), naturalLightAt(x, y, z), artificialLightAt(x, y, z));
    }

    public BlockType blockTypeAt(final int x, final int y, final int z) {
        final int base = (y * Constants.BLOCKS_PER_CHUNK_SECTION_X * Constants.BLOCKS_PER_CHUNK_SECTION_Z
                + x * Constants.BLOCKS_PER_CHUNK_SECTION_Z + z) * Constants.BYTES_PER_BLOCK_ID;
        return BlockType.values()[Byte.toUnsignedInt(blocks[base]) << 4 | Byte.toUnsignedInt(blocks[base + 1]) >> 4];
    }

    public int blockVariantAt(final int x, final int y, final int z) {
        final int base = (y * Constants.BLOCKS_PER_CHUNK_SECTION_X * Constants.BLOCKS_PER_CHUNK_SECTION_Z
                + x * Constants.BLOCKS_PER_CHUNK_SECTION_Z + z) * Constants.BYTES_PER_BLOCK_ID;
        return blocks[base + 1] & 0x0f;
    }

    public Map<Integer, String> blockDataAt(final int x, final int y, final int z) {
        decodeData();
        final int base = y * Constants.BLOCKS_PER_CHUNK_SECTION_X * Constants.BLOCKS_PER_CHUNK_SECTION_Z
                + x * Constants.BLOCKS_PER_CHUNK_SECTION_Z + z;
        return decodedData[base];
    }

    public int artificialLightAt(final int x, final int y, final int z) {
        final int base = (y * Constants.BLOCKS_PER_CHUNK_SECTION_X * Constants.BLOCKS_PER_CHUNK_SECTION_Z
                + x * Constants.BLOCKS_PER_CHUNK_SECTION_Z + z) * Constants.BYTES_PER_LIGHT;
        return light[base] >> 4;
    }

    public int naturalLightAt(final int x, final int y, final int z) {
        final int base = (y * Constants.BLOCKS_PER_CHUNK_SECTION_X * Constants.BLOCKS_PER_CHUNK_SECTION_Z
                + x * Constants.BLOCKS_PER_CHUNK_SECTION_Z + z) * Constants.BYTES_PER_LIGHT;
        return light[base] & 0x0f;
    }

    private void decodeData() {
        if(decodedData != null)
            return;
        @SuppressWarnings("unchecked")
        final Map<Integer, String>[] decoded = new Map[Constants.BLOCKS_PER_CHUNK_SECTION];
        try(final DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data))) {
            final int count = dis.readUnsignedShort();
            for(int i = 0; i < count; i++) {
                final int base = dis.readUnsignedShort();
                final int length = dis.readUnsignedShort();
                try(final DataInputStream mapDis = new DataInputStream(new ByteArrayInputStream(data, data.length - dis.available(), length))) {
                    final Map<Integer, String> map = new HashMap<>();
                    while(mapDis.available() > 0) {
                        final Integer key = dis.readInt();
                        final String value = dis.readUTF();
                        map.put(key, value);
                    }
                    decoded[base] = map;
                }
                dis.skipBytes(length);
            }
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
        decodedData = decoded;
    }
}
