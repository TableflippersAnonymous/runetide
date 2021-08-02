package com.runetide.common.dto;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.runetide.common.Constants;
import com.runetide.common.domain.Vec2D;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class ColumnRef implements Ref<ColumnRef>, XZCoordinates<ColumnRef> {
    public static final Comparator<ColumnRef> COMPARE_BY_X = Comparator
            .comparing(ColumnRef::getChunkRef, ChunkRef.COMPARE_BY_X)
            .thenComparingInt(ColumnRef::getX);
    public static final Comparator<ColumnRef> COMPARE_BY_Z = Comparator
            .comparing(ColumnRef::getChunkRef, ChunkRef.COMPARE_BY_Z)
            .thenComparingInt(ColumnRef::getZ);
    public static final List<Comparator<ColumnRef>> COMPARATORS = ImmutableList.of(COMPARE_BY_X, COMPARE_BY_Z);
    public static final String PATH_REGEX = ChunkRef.PATH_REGEX + ":[0-9a-f]+";
    public static final int PATH_PARTS = ChunkRef.PATH_PARTS + 1;

    private final ChunkRef chunkRef;
    private final int x;
    private final int z;

    ColumnRef(final ChunkRef chunkRef, final int x, final int z) {
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
        return chunkRef + ":" + Integer.toString(x * Constants.BLOCKS_PER_CHUNK_SECTION_Z + z, 16);
    }

    public static ColumnRef valueOf(final String stringValue) {
        final String[] parts = stringValue.split(":", PATH_PARTS);
        if(parts.length != PATH_PARTS)
            throw new IllegalArgumentException("Invalid ColumnRef: " + stringValue);
        final ChunkRef chunkRef = ChunkRef.valueOf(Joiner.on(":").join(Arrays.copyOf(parts, ChunkRef.PATH_PARTS)));
        final int xz = Integer.parseInt(parts[PATH_PARTS - 1], 16);
        final int x = xz / Constants.BLOCKS_PER_CHUNK_SECTION_Z;
        final int z = xz % Constants.BLOCKS_PER_CHUNK_SECTION_Z;
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

    @Override
    public Comparator<ColumnRef> compareByCoordinate(final int coordinate) {
        return COMPARATORS.get(coordinate);
    }

    @Override
    public ColumnRef withCoordinateFrom(final ColumnRef other, final int coordinate) {
        if(coordinate >= coordinateSize())
            return this;
        return chunkRef.withCoordinateFrom(other.chunkRef, coordinate)
                .column(coordinate == COORDINATE_X ? other.x : x, coordinate == COORDINATE_Z ? other.z : z);
    }
}
