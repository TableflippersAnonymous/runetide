package com.runetide.common.dto;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

public class ChunkSectionRef {
    private final ChunkRef chunkRef;
    private final int y;

    public ChunkSectionRef(ChunkRef chunkRef, int y) {
        this.chunkRef = chunkRef;
        this.y = y;
    }

    public ChunkRef getChunkRef() {
        return chunkRef;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChunkSectionRef that = (ChunkSectionRef) o;
        return y == that.y &&
                chunkRef.equals(that.chunkRef);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chunkRef, y);
    }

    @Override
    public String toString() {
        return chunkRef + ":" + y;
    }

    public static ChunkSectionRef valueOf(final String stringValue) {
        final String[] parts = stringValue.split(":", 6);
        if(parts.length != 6)
            throw new IllegalArgumentException("Invalid ChunkSectionRef: " + stringValue);
        final String encodedChunk = parts[0] + ":" + parts[1] + ":" + parts[2] + ":" + parts[3] + ":" + parts[4];
        final ChunkRef chunkRef = ChunkRef.valueOf(encodedChunk);
        final int y = Integer.parseInt(parts[5]);
        return new ChunkSectionRef(chunkRef, y);
    }

    public void encode(final DataOutputStream dataOutputStream) throws IOException {
        chunkRef.encode(dataOutputStream);
        dataOutputStream.writeByte(y);
    }

    public static ChunkSectionRef decode(final DataInputStream dataInputStream) throws IOException {
        final ChunkRef chunkRef = ChunkRef.decode(dataInputStream);
        final int y = dataInputStream.readUnsignedByte();
        return new ChunkSectionRef(chunkRef, y);
    }

    public BlockRef block(final int x, final int y, final int z) {
        return new BlockRef(this, x, y, z);
    }
}
