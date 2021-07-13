package com.runetide.common.dto;

import com.runetide.common.Constants;
import com.runetide.common.domain.Vec3D;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.Objects;

public class BlockRef {
    public static final Comparator<BlockRef> COMPARE_BY_X = Comparator
            .comparing(BlockRef::getChunkSectionRef, ChunkSectionRef.COMPARE_BY_X)
            .thenComparingInt(BlockRef::getX);
    public static final Comparator<BlockRef> COMPARE_BY_Y = Comparator
            .comparing(BlockRef::getChunkSectionRef, ChunkSectionRef.COMPARE_BY_Y)
            .thenComparingInt(BlockRef::getY);
    public static final Comparator<BlockRef> COMPARE_BY_Z = Comparator
            .comparing(BlockRef::getChunkSectionRef, ChunkSectionRef.COMPARE_BY_Z)
            .thenComparingInt(BlockRef::getZ);

    private final ChunkSectionRef chunkSectionRef;
    private final int x;
    private final int y;
    private final int z;

    public BlockRef(ChunkSectionRef chunkSectionRef, int x, int y, int z) {
        if(x < 0 || x >= Constants.BLOCKS_PER_CHUNK_SECTION_X
                || y < 0 || y >= Constants.BLOCKS_PER_CHUNK_SECTION_Y
                || z < 0 || z >= Constants.BLOCKS_PER_CHUNK_SECTION_Z)
            throw new IndexOutOfBoundsException("x/y/z out of range");
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

    public ColumnRef column() {
        return chunkSectionRef.column(x, z);
    }

    public BlockRef add(final Vec3D vec) {
        final Vec3D sum = vec.add(new Vec3D(x, y, z));
        final Vec3D modulo = sum.modulo(Constants.BLOCKS_PER_CHUNK_SECTION_VEC);
        return chunkSectionRef.add(sum.divide(Constants.BLOCKS_PER_CHUNK_SECTION_VEC))
                .block((int) modulo.getX(), (int) modulo.getY(), (int) modulo.getZ());
    }

    public BlockRef withXFrom(final BlockRef other) {
        return chunkSectionRef.withXFrom(other.chunkSectionRef)
                .block(other.x, y, z);
    }

    public BlockRef withYFrom(final BlockRef other) {
        return chunkSectionRef.withYFrom(other.chunkSectionRef)
                .block(x, other.y, z);
    }

    public BlockRef withZFrom(final BlockRef other) {
        return chunkSectionRef.withZFrom(other.chunkSectionRef)
                .block(x, y, other.z);
    }

    public BlockRef withMinXFrom(final BlockRef other) {
        if(BlockRef.COMPARE_BY_X.compare(this, other) <= 0)
            return this;
        return withXFrom(other);
    }

    public BlockRef withMinYFrom(final BlockRef other) {
        if(BlockRef.COMPARE_BY_Y.compare(this, other) <= 0)
            return this;
        return withYFrom(other);
    }

    public BlockRef withMinZFrom(final BlockRef other) {
        if(BlockRef.COMPARE_BY_Z.compare(this, other) <= 0)
            return this;
        return withZFrom(other);
    }

    public BlockRef withMaxXFrom(final BlockRef other) {
        if(BlockRef.COMPARE_BY_X.compare(this, other) >= 0)
            return this;
        return withXFrom(other);
    }

    public BlockRef withMaxYFrom(final BlockRef other) {
        if(BlockRef.COMPARE_BY_Y.compare(this, other) >= 0)
            return this;
        return withYFrom(other);
    }

    public BlockRef withMaxZFrom(final BlockRef other) {
        if(BlockRef.COMPARE_BY_Z.compare(this, other) >= 0)
            return this;
        return withZFrom(other);
    }

    public BlockRef minCoordinates(final BlockRef other) {
        return withMinXFrom(other).withMinYFrom(other).withMinZFrom(other);
    }

    public BlockRef maxCoordinates(final BlockRef other) {
        return withMaxXFrom(other).withMaxYFrom(other).withMaxZFrom(other);
    }

    public WorldRef getWorldRef() {
        return chunkSectionRef.getWorldRef();
    }

    public Vec3D subtract(final BlockRef other) {
        return chunkSectionRef.subtract(other.chunkSectionRef)
                .scale(Constants.BLOCKS_PER_CHUNK_SECTION_VEC)
                .add(new Vec3D(x - other.x, y - other.y, z - other.z));
    }
}
