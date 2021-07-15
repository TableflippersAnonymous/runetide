package com.runetide.common.dto;

import com.runetide.common.Constants;
import com.runetide.common.domain.Vec2D;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Comparator;
import java.util.Objects;

public class ColumnRef implements Ref<ColumnRef>, XZCoordinates<ColumnRef> {
    public static final Comparator<ColumnRef> COMPARE_BY_X = Comparator
            .comparing(ColumnRef::getChunkRef, ChunkRef.COMPARE_BY_X)
            .thenComparingInt(ColumnRef::getX);
    public static final Comparator<ColumnRef> COMPARE_BY_Z = Comparator
            .comparing(ColumnRef::getChunkRef, ChunkRef.COMPARE_BY_Z)
            .thenComparingInt(ColumnRef::getZ);

    private final ChunkRef chunkRef;
    private final int x;
    private final int z;

    public ColumnRef(final ChunkRef chunkRef, final int x, final int z) {
        if(x < 0 || x >= Constants.BLOCKS_PER_CHUNK_SECTION_X
                || z < 0 || z >= Constants.BLOCKS_PER_CHUNK_SECTION_Z)
            throw new IndexOutOfBoundsException("x/z out of range");
        this.chunkRef = chunkRef;
        this.x = x;
        this.z = z;
    }

    public ChunkRef getChunkRef() {
        return chunkRef;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ColumnRef columnRef = (ColumnRef) o;
        return x == columnRef.x && z == columnRef.z && chunkRef.equals(columnRef.chunkRef);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chunkRef, x, z);
    }

    @Override
    public String toString() {
        return chunkRef + ":" + x + ":" + z;
    }

    public static ColumnRef valueOf(final String stringValue) {
        final String[] parts = stringValue.split(":", 7);
        if(parts.length != 7)
            throw new IllegalArgumentException("Invalid ColumnRef: " + stringValue);
        final String encodedChunk = parts[0] + ":" + parts[1] + ":" + parts[2] + ":" + parts[3] + ":" + parts[4];
        final ChunkRef chunkRef = ChunkRef.valueOf(encodedChunk);
        final int x = Integer.parseInt(parts[5]);
        final int z = Integer.parseInt(parts[6]);
        return new ColumnRef(chunkRef, x, z);
    }

    @Override
    public void encode(final DataOutput dataOutput) throws IOException {
        chunkRef.encode(dataOutput);
        dataOutput.writeByte(x << 4 | z);
    }

    public static ColumnRef decode(final DataInputStream dataInputStream) throws IOException {
        final ChunkRef chunkRef = ChunkRef.decode(dataInputStream);
        final int xz = dataInputStream.readUnsignedByte();
        final int x = xz >> 4;
        final int z = xz & 0xf;
        return new ColumnRef(chunkRef, x, z);
    }

    public WorldRef getWorldRef() {
        return chunkRef.getWorldRef();
    }

    public BlockRef block(final int y) {
        return chunkRef.block(x, y, z);
    }

    @Override
    public ColumnRef add(final Vec2D vec) {
        final Vec2D sum = vec.add(new Vec2D(x, z));
        final Vec2D modulo = sum.modulo(Constants.BLOCKS_PER_CHUNK_SECTION_VEC);
        return chunkRef.add(sum.divide(Constants.BLOCKS_PER_CHUNK_SECTION_VEC))
                .column((int) modulo.getX(), (int) modulo.getZ());
    }

    public ColumnRef withXFrom(final ColumnRef other) {
        return chunkRef.withXFrom(other.chunkRef)
                .column(other.x, z);
    }

    public ColumnRef withZFrom(final ColumnRef other) {
        return chunkRef.withZFrom(other.chunkRef)
                .column(x, other.z);
    }

    @Override
    public Comparator<ColumnRef> getXComparator() {
        return COMPARE_BY_X;
    }

    @Override
    public Comparator<ColumnRef> getZComparator() {
        return COMPARE_BY_Z;
    }

    @Override
    public ColumnRef getSelf() {
        return this;
    }

    @Override
    public boolean isSameCoordinateSystem(final ColumnRef other) {
        return getWorldRef().equals(other.getWorldRef());
    }

    @Override
    public Vec2D subtract(final ColumnRef other) {
        return chunkRef.subtract(other.chunkRef)
                .scale(Constants.COLUMNS_PER_CHUNK_VEC)
                .add(new Vec2D(x - other.x, z - other.z));
    }
}
