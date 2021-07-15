package com.runetide.common.dto;

import com.runetide.common.Constants;
import com.runetide.common.domain.Vec3D;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Comparator;
import java.util.Objects;

public class PositionRef implements Ref<PositionRef>, XYZCoordinates<PositionRef> {
    public static final Comparator<PositionRef> COMPARE_BY_X = Comparator
            .comparing(PositionRef::getBlockRef, BlockRef.COMPARE_BY_X)
            .thenComparingInt(PositionRef::getX);
    public static final Comparator<PositionRef> COMPARE_BY_Y = Comparator
            .comparing(PositionRef::getBlockRef, BlockRef.COMPARE_BY_Y)
            .thenComparingInt(PositionRef::getY);
    public static final Comparator<PositionRef> COMPARE_BY_Z = Comparator
            .comparing(PositionRef::getBlockRef, BlockRef.COMPARE_BY_Z)
            .thenComparingInt(PositionRef::getZ);

    private final BlockRef blockRef;
    private final int x;
    private final int y;
    private final int z;

    public PositionRef(BlockRef blockRef, int x, int y, int z) {
        if(x < 0 || x >= Constants.OFFSETS_PER_BLOCK_X
                || y < 0 || y >= Constants.OFFSETS_PER_BLOCK_Y
                || z < 0 || z >= Constants.OFFSETS_PER_BLOCK_Z)
            throw new IndexOutOfBoundsException("x/y/z out of range");
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

    public void encode(final DataOutput dataOutput) throws IOException {
        blockRef.encode(dataOutput);
        dataOutput.writeByte(x);
        dataOutput.writeByte(y);
        dataOutput.writeByte(z);
    }

    public static PositionRef decode(final DataInputStream dataInputStream) throws IOException {
        final BlockRef blockRef = BlockRef.decode(dataInputStream);
        final int x = dataInputStream.readUnsignedByte();
        final int y = dataInputStream.readUnsignedByte();
        final int z = dataInputStream.readUnsignedByte();
        return new PositionRef(blockRef, x, y, z);
    }

    @Override
    public PositionRef withXFrom(final PositionRef other) {
        return blockRef.withXFrom(other.blockRef)
                .position(other.x, y, z);
    }

    @Override
    public PositionRef withYFrom(final PositionRef other) {
        return blockRef.withYFrom(other.blockRef)
                .position(x, other.y, z);
    }

    @Override
    public PositionRef withZFrom(final PositionRef other) {
        return blockRef.withZFrom(other.blockRef)
                .position(x, y, other.z);
    }

    @Override
    public Comparator<PositionRef> getXComparator() {
        return COMPARE_BY_X;
    }

    @Override
    public Comparator<PositionRef> getYComparator() {
        return COMPARE_BY_Y;
    }

    @Override
    public Comparator<PositionRef> getZComparator() {
        return COMPARE_BY_Z;
    }

    @Override
    public PositionRef getSelf() {
        return this;
    }

    @Override
    public boolean isSameCoordinateSystem(final PositionRef other) {
        return blockRef.isSameCoordinateSystem(other.blockRef);
    }

    @Override
    public PositionRef add(final Vec3D other) {
        final Vec3D sum = other.add(new Vec3D(x, y, z));
        final Vec3D modulo = sum.modulo(Constants.OFFSETS_PER_BLOCK_VEC);
        return blockRef.add(sum.divide(Constants.OFFSETS_PER_BLOCK_VEC))
                .position((int) modulo.getX(), (int) modulo.getY(), (int) modulo.getZ());
    }

    @Override
    public Vec3D subtract(final PositionRef other) {
        return blockRef.subtract(other.blockRef)
                .scale(Constants.OFFSETS_PER_BLOCK_VEC)
                .add(new Vec3D(x - other.x, y - other.y, z - other.z));
    }
}
