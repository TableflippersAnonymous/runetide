package com.runetide.services.internal.region.server.domain;

import com.runetide.common.Constants;
import com.runetide.services.internal.region.common.Block;
import com.runetide.services.internal.region.common.ChunkSection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;

public class LoadedChunkSection {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private byte[] encodedBlocks = new byte[Constants.BLOCKS_PER_CHUNK_SECTION*2]; // y/x/z; 2 bytes for: block id (12 bits), block subtype (4 bits)
    private byte[] encodedLight = new byte[Constants.BLOCKS_PER_CHUNK_SECTION]; // y/x/z; 1 byte for block light (4 bits), natural light (4 bits)
    private byte[][] encodedData = new byte[Constants.BLOCKS_PER_CHUNK_SECTION][]; // y/x/z

    public LoadedChunkSection(final DataInputStream dataInputStream) throws IOException {
        dataInputStream.readFully(encodedBlocks);
        dataInputStream.readFully(encodedLight);
        final int count = dataInputStream.readUnsignedShort();
        for(int i = 0; i < count; i++) {
            final int base = dataInputStream.readUnsignedShort();
            final int length = dataInputStream.readUnsignedShort();
            encodedData[base] = new byte[length];
            dataInputStream.readFully(encodedData[base]);
        }
    }

    public ChunkSection toClient() {
        return new ChunkSection(encodedBlocks, packData(), encodedLight);
    }

    public void setBlock(final int bx, final int by, final int bz, final Block block) {
        final int base = by * Constants.BLOCKS_PER_CHUNK_SECTION_Z * Constants.BLOCKS_PER_CHUNK_SECTION_X
                + bx * Constants.BLOCKS_PER_CHUNK_SECTION_Z + bz;
        encodedBlocks[base * Constants.BYTES_PER_BLOCK_ID] = (byte) ((block.getType().toValue() >> 4) & 0xff);
        encodedBlocks[base * Constants.BYTES_PER_BLOCK_ID + 1] = (byte) (((block.getType().toValue() << 4) & 0xf0)
                | (block.getVariant() & 0x0f));
        encodedLight[base * Constants.BYTES_PER_LIGHT] = (byte) (((block.getArtificialLight() << 4) & 0xf0)
                | (block.getNaturalLight() & 0x0f));
        if(block.getData() == null || block.getData().isEmpty())
            encodedData[base] = null;
        else
            encodedData[base] = block.getEncodedData();
    }

    public void encode(final DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.write(encodedBlocks);
        dataOutputStream.write(encodedLight);
        packData(dataOutputStream);
    }

    private byte[] packData() {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try(final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream)) {
            packData(dataOutputStream);
        } catch (final IOException e) {
            LOG.error("Caught IOException:", e);
            throw new IllegalStateException(e);
        }
        return byteArrayOutputStream.toByteArray();
    }

    private void packData(final DataOutputStream dataOutputStream) throws IOException {
        final int count = (int) Arrays.stream(encodedData).filter(x -> x != null && x.length != 0).count();
        dataOutputStream.writeShort(count);
        for(int i = 0; i < encodedData.length; i++) {
            if(encodedData[i] == null || encodedData[i].length == 0)
                continue;
            dataOutputStream.writeShort(i);
            dataOutputStream.writeShort(encodedData[i].length);
            dataOutputStream.write(encodedData[i]);
        }
    }

    /*public BlockRef getBlock(final byte x, final byte y, final byte z) {
        return new BlockRef(this, x, y, z);
    }*/
}
