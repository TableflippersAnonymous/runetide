package com.runetide.common.dto;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

public class ChunkRef {
    private final RegionRef regionRef;
    private final int x;
    private final int z;

    public ChunkRef(RegionRef regionRef, int x, int z) {
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
        return regionRef + ":" + x + ":" + z;
    }

    public static ChunkRef valueOf(final String stringValue) {
        final String[] parts = stringValue.split(":", 5);
        if(parts.length != 5)
            throw new IllegalArgumentException("Invalid ChunkRef: " + stringValue);
        final String encodedRegion = parts[0] + ":" + parts[1] + ":" + parts[2];
        final RegionRef regionRef = RegionRef.valueOf(encodedRegion);
        final int x = Integer.parseInt(parts[3]);
        final int z = Integer.parseInt(parts[4]);
        return new ChunkRef(regionRef, x, z);
    }

    public void encode(final DataOutputStream dataOutputStream) throws IOException {
        regionRef.encode(dataOutputStream);
        dataOutputStream.writeByte(x);
        dataOutputStream.writeByte(z);
    }

    public static ChunkRef decode(final DataInputStream dataInputStream) throws IOException {
        final RegionRef regionRef = RegionRef.decode(dataInputStream);
        final int x = dataInputStream.readUnsignedByte();
        final int z = dataInputStream.readUnsignedByte();
        return new ChunkRef(regionRef, x, z);
    }

    public ChunkSectionRef section(final int y) {
        return new ChunkSectionRef(this, y);
    }
}
