package com.runetide.services.internal.region.server.domain;

import com.runetide.common.Constants;
import com.runetide.services.internal.region.common.Block;
import com.runetide.services.internal.region.common.ChunkSection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class LoadedChunkSection {
    private byte[] encodedBlocks = new byte[Constants.BLOCKS_PER_CHUNK_SECTION*3]; // y/x/z; 3 bytes for: block id (12 bits), block subtype (4 bits), block light (4 bits), natural light (4 bits)
    private byte[] encodedData;

    public LoadedChunkSection(final DataInputStream dataInputStream) throws IOException {
        dataInputStream.readFully(encodedBlocks);
        final int length = dataInputStream.readUnsignedShort();
        encodedData = new byte[length];
        dataInputStream.readFully(encodedData);
    }

    public ChunkSection toClient() {
        final byte[] blocks;
        final byte[] data;
        final byte[] light;

        return new ChunkSection(blocks, data, light);
    }

    public void setBlock(final int bx, final int by, final int bz, final Block block) {

    }

    public void encode(final DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.write(encodedBlocks);
        dataOutputStream.writeShort(encodedData.length);
        dataOutputStream.write(encodedData);
    }

    /*public BlockRef getBlock(final byte x, final byte y, final byte z) {
        return new BlockRef(this, x, y, z);
    }*/
}
