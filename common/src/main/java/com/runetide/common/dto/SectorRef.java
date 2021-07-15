package com.runetide.common.dto;

import com.google.common.base.Objects;
import com.runetide.common.Constants;
import com.runetide.common.domain.Vec2D;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Comparator;

public class SectorRef implements Ref<SectorRef>, XZCoordinates<SectorRef> {
    public static final Comparator<SectorRef> COMPARE_BY_X = Comparator.comparing(SectorRef::getWorldRef)
            .thenComparingLong(SectorRef::getX);
    public static final Comparator<SectorRef> COMPARE_BY_Z = Comparator.comparing(SectorRef::getWorldRef)
            .thenComparingLong(SectorRef::getZ);

    private final WorldRef worldRef;
    private final long x;
    private final long z;

    public SectorRef(final WorldRef worldRef, final long x, final long z) {
        this.worldRef = worldRef;
        this.x = x;
        this.z = z;
    }

    public WorldRef getWorldRef() {
        return worldRef;
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
        final SectorRef sectorRef = (SectorRef) o;
        return x == sectorRef.x &&
                z == sectorRef.z &&
                Objects.equal(worldRef, sectorRef.worldRef);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(worldRef, x, z);
    }

    @Override
    public String toString() {
        return worldRef + ":" + x + ":" + z;
    }

    public static SectorRef valueOf(final String stringValue) {
        final String[] parts = stringValue.split(":", 3);
        if(parts.length != 3)
            throw new IllegalArgumentException("Invalid SectorRef: " + stringValue);
        final WorldRef worldRef = WorldRef.valueOf(parts[0]);
        final long x = Long.parseLong(parts[1]);
        final long z = Long.parseLong(parts[2]);
        return new SectorRef(worldRef, x, z);
    }

    @Override
    public void encode(final DataOutput dataOutput) throws IOException {
        worldRef.encode(dataOutput);
        dataOutput.writeLong(x);
        dataOutput.writeLong(z);
    }

    public static SectorRef decode(final DataInputStream dataInputStream) throws IOException {
        final WorldRef worldRef = WorldRef.decode(dataInputStream);
        final long x = dataInputStream.readLong();
        final long z = dataInputStream.readLong();
        return new SectorRef(worldRef, x, z);
    }

    public RegionRef region(final int x, final int z) {
        return new RegionRef(worldRef, this.x * Constants.REGIONS_PER_SECTOR_X + x,
                this.z * Constants.REGIONS_PER_SECTOR_Z + z);
    }

    @Override
    public boolean isSameCoordinateSystem(final SectorRef other) {
        return worldRef.equals(other.worldRef);
    }

    @Override
    public SectorRef add(final Vec2D vec) {
        return new SectorRef(worldRef, x + vec.getX(), z + vec.getZ());
    }

    @Override
    public Vec2D subtract(final SectorRef other) {
        return new Vec2D(x - other.x, z - other.z);
    }

    @Override
    public SectorRef withXFrom(final SectorRef other) {
        return new SectorRef(worldRef, other.x, z);
    }

    @Override
    public SectorRef withZFrom(final SectorRef other) {
        return new SectorRef(worldRef, x, other.z);
    }

    @Override
    public Comparator<SectorRef> getXComparator() {
        return COMPARE_BY_X;
    }

    @Override
    public Comparator<SectorRef> getZComparator() {
        return COMPARE_BY_Z;
    }

    @Override
    public SectorRef getSelf() {
        return this;
    }
}
