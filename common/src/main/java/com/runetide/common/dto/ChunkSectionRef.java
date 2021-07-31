package com.runetide.common.dto;

import com.google.common.collect.ImmutableList;
import com.runetide.common.Constants;
import com.runetide.common.domain.Vec3D;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class ChunkSectionRef implements Ref<ChunkSectionRef>, XYZCoordinates<ChunkSectionRef> {
    public static final Comparator<ChunkSectionRef> COMPARE_BY_X = Comparator
            .comparing(ChunkSectionRef::getChunkRef, ChunkRef.COMPARE_BY_X);
    public static final Comparator<ChunkSectionRef> COMPARE_BY_Y = Comparator
            .comparing(ChunkSectionRef::getWorldRef)
            .thenComparingInt(ChunkSectionRef::getY);
    public static final Comparator<ChunkSectionRef> COMPARE_BY_Z = Comparator
            .comparing(ChunkSectionRef::getChunkRef, ChunkRef.COMPARE_BY_Z);
    public static final List<Comparator<ChunkSectionRef>> COMPARATORS = ImmutableList.of(COMPARE_BY_X, COMPARE_BY_Z,
            COMPARE_BY_Y);

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

    @Override
    public void encode(final DataOutput dataOutput) throws IOException {
        chunkRef.encode(dataOutput);
        dataOutput.writeByte(y);
    }

    public static ChunkSectionRef decode(final DataInputStream dataInputStream) throws IOException {
        final ChunkRef chunkRef = ChunkRef.decode(dataInputStream);
        final int y = dataInputStream.readUnsignedByte();
        return new ChunkSectionRef(chunkRef, y);
    }

    public WorldRef getWorldRef() {
        return chunkRef.getWorldRef();
    }

    public RegionRef getRegionRef() {
        return chunkRef.getRegionRef();
    }

    public ColumnRef column(final int x, final int z) {
        return chunkRef.column(x, z);
    }

    public BlockRef block(final int x, final int y, final int z) {
        return new BlockRef(this, x, y, z);
    }

    @Override
    public boolean isSameCoordinateSystem(final ChunkSectionRef other) {
        return chunkRef.isSameCoordinateSystem(other.chunkRef);
    }

    @Override
    public ChunkSectionRef add(final Vec3D vec) {
        final Vec3D sum = vec.add(new Vec3D(0, y, 0));
        final Vec3D modulo = sum.modulo(Constants.CHUNK_SECTIONS_PER_CHUNK_VEC);
        return chunkRef.add(sum.divide(Constants.CHUNK_SECTIONS_PER_CHUNK_VEC).toVec2D())
                .section((int) modulo.getY());
    }

    @Override
    public ChunkSectionRef getSelf() {
        return this;
    }

    @Override
    public Vec3D subtract(final ChunkSectionRef other) {
        return chunkRef.subtract(other.chunkRef)
                .scale(Constants.CHUNK_SECTIONS_PER_CHUNK_VEC)
                .add(new Vec3D(0, y - other.y, 0));
    }

    @Override
    public Comparator<ChunkSectionRef> compareByCoordinate(final int coordinate) {
        return COMPARATORS.get(coordinate);
    }

    @Override
    public ChunkSectionRef withCoordinateFrom(final ChunkSectionRef other, final int coordinate) {
        if(coordinate >= coordinateSize())
            return this;
        return chunkRef.withCoordinateFrom(other.chunkRef, coordinate)
                .section(coordinate == COORDINATE_Y ? other.y : y);
    }
}
