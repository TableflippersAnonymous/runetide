package com.runetide.common.dto;

import com.runetide.common.Constants;
import com.runetide.common.domain.Vec3D;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.Objects;

public class ChunkSectionRef {
    public static final Comparator<ChunkSectionRef> COMPARE_BY_X = Comparator
            .comparing(ChunkSectionRef::getChunkRef, ChunkRef.COMPARE_BY_X);
    public static final Comparator<ChunkSectionRef> COMPARE_BY_Y = Comparator
            .comparing(ChunkSectionRef::getWorldRef)
            .thenComparingInt(ChunkSectionRef::getY);
    public static final Comparator<ChunkSectionRef> COMPARE_BY_Z = Comparator
            .comparing(ChunkSectionRef::getChunkRef, ChunkRef.COMPARE_BY_Z);

    private final ChunkRef chunkRef;
    private final int y;

    public ChunkSectionRef(ChunkRef chunkRef, int y) {
        if(y < 0 || y >= Constants.CHUNK_SECTIONS_PER_CHUNK)
            throw new IndexOutOfBoundsException("y out of range");
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

    public WorldRef getWorldRef() {
        return chunkRef.getWorldRef();
    }

    public ColumnRef column(final int x, final int z) {
        return chunkRef.column(x, z);
    }

    public BlockRef block(final int x, final int y, final int z) {
        return new BlockRef(this, x, y, z);
    }

    public ChunkSectionRef add(final Vec3D vec) {
        final Vec3D sum = vec.add(new Vec3D(0, y, 0));
        final Vec3D modulo = sum.modulo(Constants.CHUNK_SECTIONS_PER_CHUNK_VEC);
        return chunkRef.add(sum.divide(Constants.CHUNK_SECTIONS_PER_CHUNK_VEC))
                .section((int) modulo.getY());
    }

    public ChunkSectionRef withXFrom(final ChunkSectionRef other) {
        return chunkRef.withXFrom(other.chunkRef).section(y);
    }

    public ChunkSectionRef withYFrom(final ChunkSectionRef other) {
        return chunkRef.section(other.y);
    }

    public ChunkSectionRef withZFrom(final ChunkSectionRef other) {
        return chunkRef.withZFrom(other.chunkRef).section(y);
    }

    public Vec3D subtract(final ChunkSectionRef other) {
        return chunkRef.subtract(other.chunkRef)
                .scale(Constants.CHUNK_SECTIONS_PER_CHUNK_VEC)
                .add(new Vec3D(0, y - other.y, 0));
    }
}
