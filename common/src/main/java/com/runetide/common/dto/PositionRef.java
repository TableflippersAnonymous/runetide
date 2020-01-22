package com.runetide.common.dto;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

public class PositionRef {
    private final BlockRef blockRef;
    private final int x;
    private final int y;
    private final int z;

    public PositionRef(BlockRef blockRef, int x, int y, int z) {
        this.blockRef = blockRef;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BlockRef getBlockRef() {
        return blockRef;
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
        PositionRef that = (PositionRef) o;
        return x == that.x &&
                y == that.y &&
                z == that.z &&
                blockRef.equals(that.blockRef);
    }

    @Override
    public int hashCode() {
        return Objects.hash(blockRef, x, y, z);
    }

    @Override
    public String toString() {
        return blockRef + "," + x + ":" + y + ":" + z;
    }

    public static PositionRef valueOf(final String stringValue) {
        final String[] parts = stringValue.split(",", 2);
        if(parts.length != 2)
            throw new IllegalArgumentException("Invalid PositionRef: " + stringValue);
        final BlockRef blockRef = BlockRef.valueOf(parts[0]);
        final String[] coordinates = parts[1].split(":", 3);
        if(coordinates.length != 3)
            throw new IllegalArgumentException("Invalid PositionRef: " + stringValue);
        final int x = Integer.parseInt(coordinates[0]);
        final int y = Integer.parseInt(coordinates[1]);
        final int z = Integer.parseInt(coordinates[2]);
        return new PositionRef(blockRef, x, y, z);
    }

    public void encode(final DataOutputStream dataOutputStream) throws IOException {
        blockRef.encode(dataOutputStream);
        dataOutputStream.writeByte(x);
        dataOutputStream.writeByte(y);
        dataOutputStream.writeByte(z);
    }

    public static PositionRef decode(final DataInputStream dataInputStream) throws IOException {
        final BlockRef blockRef = BlockRef.decode(dataInputStream);
        final int x = dataInputStream.readUnsignedByte();
        final int y = dataInputStream.readUnsignedByte();
        final int z = dataInputStream.readUnsignedByte();
        return new PositionRef(blockRef, x, y, z);
    }
}
