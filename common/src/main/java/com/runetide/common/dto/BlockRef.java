package com.runetide.common.dto;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

public class BlockRef {
    private final ChunkSectionRef chunkSectionRef;
    private final int x;
    private final int y;
    private final int z;

    public BlockRef(ChunkSectionRef chunkSectionRef, int x, int y, int z) {
        this.chunkSectionRef = chunkSectionRef;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public ChunkSectionRef getChunkSectionRef() {
        return chunkSectionRef;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockRef blockRef = (BlockRef) o;
        return x == blockRef.x &&
                y == blockRef.y &&
                z == blockRef.z &&
                chunkSectionRef.equals(blockRef.chunkSectionRef);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chunkSectionRef, x, y, z);
    }

    @Override
    public String toString() {
        return chunkSectionRef + ":" + x + ":" + y + ":" + z;
    }

    public static BlockRef valueOf(final String stringValue) {
        final String[] parts = stringValue.split(":", 9);
        if(parts.length != 9)
            throw new IllegalArgumentException("Invalid BlockRef: " + stringValue);
        final String encodedChunkSection = parts[0] + ":" + parts[1] + ":" + parts[2] + ":" + parts[3] + ":" + parts[4]
                + ":" + parts[5];
        final ChunkSectionRef chunkSectionRef = ChunkSectionRef.valueOf(encodedChunkSection);
        final int x = Integer.parseInt(parts[6]);
        final int y = Integer.parseInt(parts[7]);
        final int z = Integer.parseInt(parts[8]);
        return new BlockRef(chunkSectionRef, x, y, z);
    }

    public void encode(final DataOutputStream dataOutputStream) throws IOException {
        chunkSectionRef.encode(dataOutputStream);
        dataOutputStream.writeByte(x);
        dataOutputStream.writeByte(y << 4 | z);
    }

    public static BlockRef decode(final DataInputStream dataInputStream) throws IOException {
        final ChunkSectionRef chunkSectionRef = ChunkSectionRef.decode(dataInputStream);
        final int x = dataInputStream.readUnsignedByte();
        final int yz = dataInputStream.readUnsignedByte();
        final int y = yz >> 4;
        final int z = yz & 0xf;
        return new BlockRef(chunkSectionRef, x, y, z);
    }
}
