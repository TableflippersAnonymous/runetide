package com.runetide.common.dto;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.runetide.common.Constants;
import com.runetide.common.domain.geometry.Vector2D;
import com.runetide.common.domain.geometry.XZCoordinates;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class RegionRef implements OffsetRef<RegionRef, Vector2D, SectorRef, ChunkRef, Vector2D>,
        XZCoordinates<RegionRef> {
    public static final Comparator<RegionRef> COMPARE_BY_X = Comparator.comparing(RegionRef::getWorldRef)
            .thenComparingLong(RegionRef::getX);
    public static final Comparator<RegionRef> COMPARE_BY_Z = Comparator.comparing(RegionRef::getWorldRef)
            .thenComparingLong(RegionRef::getZ);
    public static final List<Comparator<RegionRef>> COMPARATORS = ImmutableList.of(COMPARE_BY_X, COMPARE_BY_Z);
    public static final String PATH_REGEX = WorldRef.PATH_REGEX + ":-?[0-9a-z]+:-?[0-9a-z]+";
    public static final int PATH_PARTS = WorldRef.PATH_PARTS + 2;

    private final WorldRef worldRef;
    private final long x;
    private final long z;

    RegionRef(final WorldRef worldRef, final long x, final long z) {
        this.worldRef = worldRef;
        this.x = x;
        this.z = z;
    }

    public WorldRef getWorldRef() {
        return worldRef;
    }

    public SectorRef getSectorRef() {
        return new SectorRef(worldRef, x / Constants.REGIONS_PER_SECTOR_X, z / Constants.REGIONS_PER_SECTOR_Z);
    }

    public long getX() {
        return x;
    }

    public long getZ() {
        return z;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final RegionRef regionRef = (RegionRef) o;
        return x == regionRef.x &&
                z == regionRef.z &&
                Objects.equal(worldRef, regionRef.worldRef);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(worldRef, x, z);
    }

    @Override
    public String toString() {
        return worldRef + ":" + Long.toString(x, 36) + ":" + Long.toString(z, 36);
    }

    public static RegionRef valueOf(final String stringValue) {
        final String[] parts = stringValue.split(":", PATH_PARTS);
        if(parts.length != PATH_PARTS)
            throw new IllegalArgumentException("Invalid RegionRef: " + stringValue);
        final WorldRef worldRef = WorldRef.valueOf(Joiner.on(":").join(Arrays.copyOf(parts, WorldRef.PATH_PARTS)));
        final long x = Long.parseLong(parts[PATH_PARTS - 2], 36);
        final long z = Long.parseLong(parts[PATH_PARTS - 1], 36);
        return new RegionRef(worldRef, x, z);
    }

    @Override
    public void encode(final DataOutput dataOutput) throws IOException {
        worldRef.encode(dataOutput);
        dataOutput.writeLong(x);
        dataOutput.writeLong(z);
    }

    public static RegionRef decode(final DataInputStream dataInputStream) throws IOException {
        final WorldRef worldRef = WorldRef.decode(dataInputStream);
        final long x = dataInputStream.readLong();
        final long z = dataInputStream.readLong();
        return new RegionRef(worldRef, x, z);
    }

    public ChunkRef chunk(final int x, final int z) {
        return new ChunkRef(this, x, z);
    }

    @Override
    public boolean isSameCoordinateSystem(final RegionRef other) {
        return worldRef.equals(other.worldRef);
    }

    @Override
    public RegionRef add(final Vector2D vec) {
        return new RegionRef(worldRef, x + vec.getX(), z + vec.getZ());
    }

    @Override
    public RegionRef getSelf() {
        return this;
    }

    @Override
    public Vector2D subtract(final RegionRef other) {
        return new Vector2D(x - other.x, z - other.z);
    }

    @Override
    public Comparator<RegionRef> compareByCoordinate(final int coordinate) {
        return COMPARATORS.get(coordinate);
    }

    @Override
    public RegionRef withCoordinateFrom(final RegionRef other, final int coordinate) {
        if(coordinate >= coordinateSize())
            return this;
        return new RegionRef(worldRef, coordinate == COORDINATE_X ? other.x : x,
                coordinate == COORDINATE_Z ? other.z : z);
    }

    @Override
    public ChunkRef getStart() {
        return chunk(0, 0);
    }

    @Override
    public ChunkRef getEnd() {
        return chunk(Constants.CHUNKS_PER_REGION_X - 1, Constants.CHUNKS_PER_REGION_Z - 1);
    }

    @Override
    public Vector2D offsetTo(final OffsetBasis<?> basis) {
        if(basis.equals(this))
            return Vector2D.IDENTITY;
        if(basis instanceof RegionRef)
            return subtract((RegionRef) basis).modulo(Constants.REGIONS_PER_SECTOR_VEC);
        if(OffsetBasis.CONTAINING_COMPARATOR.compare(getClass(), basis.getClass()) < 0)
            return getStart().offsetTo(basis).divide(Constants.CHUNKS_PER_REGION_VEC);
        return getParent().offsetTo(basis).scale(Constants.REGIONS_PER_SECTOR_VEC)
                .add(new Vector2D(x % Constants.REGIONS_PER_SECTOR_X, z % Constants.REGIONS_PER_SECTOR_Z));
    }

    @Override
    public SectorRef getParent() {
        return getSectorRef();
    }
}
