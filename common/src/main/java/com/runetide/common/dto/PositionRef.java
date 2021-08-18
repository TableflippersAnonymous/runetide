package com.runetide.common.dto;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.runetide.common.Constants;
import com.runetide.common.domain.geometry.Vector3L;
import com.runetide.common.domain.geometry.XYZCoordinates;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class PositionRef implements ContainerRef<PositionRef, Vector3L, BlockRef, PositionRef, Vector3L>,
        XYZCoordinates<PositionRef> {
    public static final Comparator<PositionRef> COMPARE_BY_X = Comparator
            .comparing(PositionRef::getBlockRef, BlockRef.COMPARE_BY_X)
            .thenComparingInt(PositionRef::getX);
    public static final Comparator<PositionRef> COMPARE_BY_Y = Comparator
            .comparing(PositionRef::getBlockRef, BlockRef.COMPARE_BY_Y)
            .thenComparingInt(PositionRef::getY);
    public static final Comparator<PositionRef> COMPARE_BY_Z = Comparator
            .comparing(PositionRef::getBlockRef, BlockRef.COMPARE_BY_Z)
            .thenComparingInt(PositionRef::getZ);
    public static final List<Comparator<PositionRef>> COMPARATORS = ImmutableList.of(COMPARE_BY_X, COMPARE_BY_Z,
            COMPARE_BY_Y);
    public static final String PATH_REGEX = BlockRef.PATH_REGEX + ":[0-9a-f]+";
    public static final int PATH_PARTS = BlockRef.PATH_PARTS + 1;

    private final BlockRef blockRef;
    private final int x;
    private final int y;
    private final int z;

    PositionRef(BlockRef blockRef, int x, int y, int z) {
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
        return blockRef + ":" + Integer.toString((x * Constants.OFFSETS_PER_BLOCK_Y + y)
                * Constants.OFFSETS_PER_BLOCK_Z + z, 16);
    }

    public static PositionRef valueOf(final String stringValue) {
        final String[] parts = stringValue.split(":", PATH_PARTS);
        if(parts.length != PATH_PARTS)
            throw new IllegalArgumentException("Invalid PositionRef: " + stringValue);
        final BlockRef blockRef = BlockRef.valueOf(Joiner.on(":").join(Arrays.copyOf(parts, BlockRef.PATH_PARTS)));
        final int xyz = Integer.parseInt(parts[PATH_PARTS - 1], 16);
        final int xy = xyz / Constants.OFFSETS_PER_BLOCK_Z;
        final int x = xy / Constants.OFFSETS_PER_BLOCK_Y;
        final int y = xy % Constants.OFFSETS_PER_BLOCK_Y;
        final int z = xyz % Constants.OFFSETS_PER_BLOCK_X;
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
    public PositionRef getSelf() {
        return this;
    }

    @Override
    public boolean isSameCoordinateSystem(final PositionRef other) {
        return blockRef.isSameCoordinateSystem(other.blockRef);
    }

    @Override
    public PositionRef add(final Vector3L other) {
        final Vector3L sum = other.add(new Vector3L(x, y, z));
        final Vector3L modulo = sum.modulo(Constants.OFFSETS_PER_BLOCK_VEC);
        return blockRef.add(sum.divide(Constants.OFFSETS_PER_BLOCK_VEC))
                .position(modulo.getX().intValue(), modulo.getY().intValue(), modulo.getZ().intValue());
    }

    @Override
    public Vector3L subtract(final PositionRef other) {
        return blockRef.subtract(other.blockRef)
                .scale(Constants.OFFSETS_PER_BLOCK_VEC)
                .add(new Vector3L(x - other.x, y - other.y, z - other.z));
    }

    @Override
    public Comparator<PositionRef> compareByCoordinate(final int coordinate) {
        return COMPARATORS.get(coordinate);
    }

    @Override
    public PositionRef withCoordinateFrom(final PositionRef other, final int coordinate) {
        if(coordinate >= coordinateSize())
            return this;
        return blockRef.withCoordinateFrom(other.blockRef, coordinate)
                .position(coordinate == COORDINATE_X ? other.x : x, coordinate == COORDINATE_Y ? other.y : y,
                        coordinate == COORDINATE_Z ? other.z : z);
    }

    @Override
    public PositionRef getStart() {
        return this;
    }

    @Override
    public PositionRef getEnd() {
        return this;
    }

    @Override
    public Vector3L offsetTo(final ContainerBase<?> basis) {
        if(basis.equals(this))
            return Vector3L.IDENTITY;
        if(basis instanceof PositionRef)
            return subtract((PositionRef) basis);
        if(basis instanceof PositionLookRef)
            return subtract(((PositionLookRef) basis).getPositionRef());
        if(ContainerBase.CONTAINING_COMPARATOR.compare(getClass(), basis.getClass()) < 0)
            throw new IllegalArgumentException("Bad OffsetBasis: " + basis);
        return getParent().offsetTo(basis).scale(Constants.OFFSETS_PER_BLOCK_VEC)
                .add(new Vector3L(x, y, z));
    }

    @Override
    public BlockRef getParent() {
        return null;
    }
}
