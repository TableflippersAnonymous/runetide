package com.runetide.services.internal.region.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    private transient Map<String, String>[] decodedData;

    public Block blockAt(final int x, final int y, final int z) {
        return null;
    }

    public BlockType blockTypeAt(final int x, final int y, final int z) {
        final int base = y << 9 | x << 5 | z << 1;
        return BlockType.values()[Byte.toUnsignedInt(blocks[base]) << 8 | Byte.toUnsignedInt(blocks[base + 1])];
    }

    public Map<String, String> blockDataAt(final int x, final int y, final int z) throws IOException {
        decodeData();
        final int base = y << 8 | x << 4 | z;
        return decodedData[base];
    }

    public int naturalLightAt(final int x, final int y, final int z) {
        return 0;
    }

    private void decodeData() throws IOException {
        if(decodedData != null)
            return;
        @SuppressWarnings("unchecked")
        final Map<String, String>[] decoded = new Map[16*16*16];
        try(final DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data))) {
            final int count = dis.readUnsignedShort();
            for(int i = 0; i < count; i++) {
                final int base = dis.readUnsignedShort();
                final Map<String, String> map = new HashMap<>();
                final int entryCount = dis.readUnsignedByte();
                for(int j = 0; j < entryCount; j++) {
                    final String key = dis.readUTF();
                    final String value = dis.readUTF();
                    map.put(key, value);
                }
                decoded[base] = map;
            }
        }
        decodedData = decoded;
    }
}
