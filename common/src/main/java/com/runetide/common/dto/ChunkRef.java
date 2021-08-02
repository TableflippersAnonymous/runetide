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

public class ChunkRef implements Ref<ChunkRef>, XZCoordinates<ChunkRef> {
    public static final Comparator<ChunkRef> COMPARE_BY_X = Comparator
            .comparing(ChunkRef::getRegionRef, RegionRef.COMPARE_BY_X)
            .thenComparingInt(ChunkRef::getX);
    public static final Comparator<ChunkRef> COMPARE_BY_Z = Comparator
            .comparing(ChunkRef::getRegionRef, RegionRef.COMPARE_BY_Z)
            .thenComparingInt(ChunkRef::getZ);
    public static final List<Comparator<ChunkRef>> COMPARATORS = ImmutableList.of(COMPARE_BY_X, COMPARE_BY_Z);
    public static final String PATH_REGEX = RegionRef.PATH_REGEX + ":[0-9a-f]+";
    public static final int PATH_PARTS = RegionRef.PATH_PARTS + 1;

    private final RegionRef regionRef;
    private final int x;
    private final int z;

    ChunkRef(final RegionRef regionRef, final int x, final int z) {
        if(x < 0 || x >= Constants.CHUNKS_PER_REGION_X
                || z < 0 || z >= Constants.CHUNKS_PER_REGION_Z)
            throw new IndexOutOfBoundsException("x/z out of range");
        this.regionRef = regionRef;
        this.x = x;
        this.z = z;
    }

    public RegionRef getRegionRef() {
        return regionRef;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChunkRef chunkRef = (ChunkRef) o;
        return x == chunkRef.x &&
                z == chunkRef.z &&
                regionRef.equals(chunkRef.regionRef);
    }

    @Override
    public int hashCode() {
        return Objects.hash(regionRef, x, z);
    }

    @Override
    public String toString() {
        return regionRef + ":" + Integer.toString(x * Constants.CHUNKS_PER_REGION_Z + z, 16);
    }

    public static ChunkRef valueOf(final String stringValue) {
        final String[] parts = stringValue.split(":", PATH_PARTS);
        if(parts.length != PATH_PARTS)
            throw new IllegalArgumentException("Invalid ChunkRef: " + stringValue);
        final RegionRef regionRef = RegionRef.valueOf(Joiner.on(":").join(Arrays.copyOf(parts, RegionRef.PATH_PARTS)));
        final int xz = Integer.parseInt(parts[PATH_PARTS - 1], 16);
        final int x = xz / Constants.CHUNKS_PER_REGION_Z;
        final int z = xz % Constants.CHUNKS_PER_REGION_Z;
        return new ChunkRef(regionRef, x, z);
    }

    @Override
    public void encode(final DataOutput dataOutput) throws IOException {
        regionRef.encode(dataOutput);
        dataOutput.writeByte(x);
        dataOutput.writeByte(z);
    }

    public static ChunkRef decode(final DataInputStream dataInputStream) throws IOException {
        final RegionRef regionRef = RegionRef.decode(dataInputStream);
        final int x = dataInputStream.readUnsignedByte();
        final int z = dataInputStream.readUnsignedByte();
        return new ChunkRef(regionRef, x, z);
    }

    public WorldRef getWorldRef() {
        return regionRef.getWorldRef();
    }

    public ChunkSectionRef section(final int y) {
        return new ChunkSectionRef(this, y);
    }

    public ColumnRef column(final int x, final int z) {
        return new ColumnRef(this, x, z);
    }

    public BlockRef block(final int x, final int y, final int z) {
        return section(y / Constants.CHUNK_SECTIONS_PER_CHUNK)
                .block(x, y % Constants.BLOCKS_PER_CHUNK_SECTION_Y, z);
    }

    @Override
    public boolean isSameCoordinateSystem(final ChunkRef other) {
        return regionRef.isSameCoordinateSystem(other.regionRef);
    }

    @Override
    public ChunkRef add(final Vec2D vec) {
        final Vec2D sum = vec.add(new Vec2D(x, z));
        final Vec2D modulo = sum.modulo(Constants.CHUNKS_PER_REGION_VEC);
        return regionRef.add(sum.divide(Constants.CHUNKS_PER_REGION_VEC))
                .chunk((int) modulo.getX(), (int) modulo.getZ());
    }

    @Override
    public ChunkRef getSelf() {
        return this;
    }

    @Override
    public Vec2D subtract(final ChunkRef other) {
        return regionRef.subtract(other.regionRef)
                .scale(Constants.CHUNKS_PER_REGION_VEC)
                .add(new Vec2D(x - other.x, z - other.z));
    }

    @Override
    public Comparator<ChunkRef> compareByCoordinate(final int coordinate) {
        return COMPARATORS.get(coordinate);
    }

    @Override
    public ChunkRef withCoordinateFrom(final ChunkRef other, final int coordinate) {
        if(coordinate >= coordinateSize())
            return this;
        return regionRef.withCoordinateFrom(other.regionRef, coordinate)
                .chunk(coordinate == COORDINATE_X ? other.x : x, coordinate == COORDINATE_Z ? other.z : z);
    }
}
