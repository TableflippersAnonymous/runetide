package com.runetide.services.internal.region.server.domain;

import com.runetide.common.Constants;
import com.runetide.common.domain.BlockType;
import com.runetide.common.dto.BlockRef;
import com.runetide.common.dto.ChunkRef;
import com.runetide.common.dto.ChunkSectionRef;
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
import java.util.Map;

public class LoadedChunkSection {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ChunkSectionRef ref;
    private byte[] encodedBlocks = new byte[Constants.BLOCKS_PER_CHUNK_SECTION * Constants.BYTES_PER_BLOCK_ID]; // y/x/z; 2 bytes for: block id (12 bits), block subtype (4 bits)
    private byte[] encodedLight = new byte[Constants.BLOCKS_PER_CHUNK_SECTION]; // y/x/z; 1 byte for block light (4 bits), natural light (4 bits)
    private byte[][] encodedData = new byte[Constants.BLOCKS_PER_CHUNK_SECTION][]; // y/x/z

    public LoadedChunkSection(final ChunkRef chunkRef, final int sectionY,
                              final DataInputStream dataInputStream) throws IOException {
        this.ref = new ChunkSectionRef(chunkRef, sectionY);
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

    private LoadedChunkSection(final ChunkSectionRef ref, byte[] encodedBlocks, byte[] encodedLight,
                               byte[][] encodedData) {
        this.ref = ref;
        this.encodedBlocks = encodedBlocks;
        this.encodedLight = encodedLight;
        this.encodedData = encodedData;
    }

    public ChunkSection toClient() {
        return new ChunkSection(encodedBlocks, packData(), encodedLight);
    }

    public synchronized void setBlock(final int bx, final int by, final int bz, final Block block) {
        final int base = by * Constants.BLOCKS_PER_CHUNK_SECTION_Z * Constants.BLOCKS_PER_CHUNK_SECTION_X
                + bx * Constants.BLOCKS_PER_CHUNK_SECTION_Z + bz;
        encodedBlocks[base * Constants.BYTES_PER_BLOCK_ID] = (byte) ((block.getType().toValue() >>> 4) & 0xff);
        encodedBlocks[base * Constants.BYTES_PER_BLOCK_ID + 1] = (byte) (((block.getType().toValue() << 4) & 0xf0)
                | (block.getVariant() & 0x0f));
        encodedLight[base * Constants.BYTES_PER_LIGHT] = (byte) (((block.getArtificialLight() << 4) & 0xf0)
                | (block.getNaturalLight() & 0x0f));
        if(block.getData() == null || block.getData().isEmpty())
            encodedData[base] = null;
        else
            encodedData[base] = block.getEncodedData();
    }

    public synchronized void encode(final DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.write(encodedBlocks);
        dataOutputStream.write(encodedLight);
        packData(dataOutputStream);
    }

    public synchronized LoadedChunkSection snapshot() {
        return new LoadedChunkSection(
                ref,
                encodedBlocks.clone(),
                encodedLight.clone(),
                Arrays.stream(encodedData).map(byte[]::clone).toArray(byte[][]::new)
        );
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

    public Block getBlock(final int x, final int y, final int z) {
        final int base = y * Constants.BLOCKS_PER_CHUNK_SECTION_Z * Constants.BLOCKS_PER_CHUNK_SECTION_X
                + x * Constants.BLOCKS_PER_CHUNK_SECTION_Z + z;
        final int blockId = (((int) encodedBlocks[base * Constants.BYTES_PER_BLOCK_ID]) << 4)
                | (((int) encodedBlocks[base * Constants.BYTES_PER_BLOCK_ID + 1]) >>> 4);
        final int variant = encodedBlocks[base * Constants.BYTES_PER_BLOCK_ID + 1] & 0x0f;
        final int naturalLight = encodedLight[base * Constants.BYTES_PER_LIGHT] & 0x0f;
        final int artificialLight = encodedLight[base * Constants.BYTES_PER_LIGHT] >>> 4;
        final Map<Integer, String> data = Block.decodeData(encodedData[base]);
        return new Block(BlockType.fromId(blockId), variant, data, naturalLight, artificialLight);
    }

    public BlockRef getBlockRef(final int x, final int y, final int z) {
        return new BlockRef(toRef(), x, y, z);
    }

    public ChunkSectionRef toRef() {
        return ref;
    }
}
