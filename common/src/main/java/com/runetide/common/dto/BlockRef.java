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

public class BlockRef implements ContainerRef<BlockRef, Vector3L, ChunkSectionRef, PositionRef, Vector3L>,
        XYZCoordinates<BlockRef> {
    public static final Comparator<BlockRef> COMPARE_BY_X = Comparator
            .comparing(BlockRef::getChunkSectionRef, ChunkSectionRef.COMPARE_BY_X)
            .thenComparingInt(BlockRef::getX);
    public static final Comparator<BlockRef> COMPARE_BY_Y = Comparator
            .comparing(BlockRef::getChunkSectionRef, ChunkSectionRef.COMPARE_BY_Y)
            .thenComparingInt(BlockRef::getY);
    public static final Comparator<BlockRef> COMPARE_BY_Z = Comparator
            .comparing(BlockRef::getChunkSectionRef, ChunkSectionRef.COMPARE_BY_Z)
            .thenComparingInt(BlockRef::getZ);
    public static final List<Comparator<BlockRef>> COMPARATORS = ImmutableList.of(COMPARE_BY_X, COMPARE_BY_Z,
            COMPARE_BY_Y);
    public static final String PATH_REGEX = ChunkSectionRef.PATH_REGEX + ":[0-9a-f]+";
    public static final int PATH_PARTS = ChunkSectionRef.PATH_PARTS + 1;

    private final ChunkSectionRef chunkSectionRef;
    private final int x;
    private final int y;
    private final int z;

    BlockRef(ChunkSectionRef chunkSectionRef, int x, int y, int z) {
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
        return chunkSectionRef + ":" + Integer.toString((x * Constants.BLOCKS_PER_CHUNK_SECTION_Y + y)
                * Constants.BLOCKS_PER_CHUNK_SECTION_Z + z, 16);
    }

    public static BlockRef valueOf(final String stringValue) {
        final String[] parts = stringValue.split(":", PATH_PARTS);
        if(parts.length != PATH_PARTS)
            throw new IllegalArgumentException("Invalid BlockRef: " + stringValue);
        final ChunkSectionRef chunkSectionRef = ChunkSectionRef.valueOf(Joiner.on(":")
                .join(Arrays.copyOf(parts, ChunkSectionRef.PATH_PARTS)));
        final int xyz = Integer.parseInt(parts[PATH_PARTS - 1], 16);
        final int xy = xyz / Constants.BLOCKS_PER_CHUNK_SECTION_Z;
        final int x = xy / Constants.BLOCKS_PER_CHUNK_SECTION_Y;
        final int y = xy % Constants.BLOCKS_PER_CHUNK_SECTION_Y;
        final int z = xyz % Constants.BLOCKS_PER_CHUNK_SECTION_Z;
        return new BlockRef(chunkSectionRef, x, y, z);
    }

    @Override
    public void encode(final DataOutput dataOutput) throws IOException {
        chunkSectionRef.encode(dataOutput);
        dataOutput.writeByte(x);
        dataOutput.writeByte(y << 4 | z);
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

    @Override
    public BlockRef add(final Vector3L vec) {
        final Vector3L sum = vec.add(new Vector3L(x, y, z));
        final Vector3L modulo = sum.modulo(Constants.BLOCKS_PER_CHUNK_SECTION_VEC);
        return chunkSectionRef.add(sum.divide(Constants.BLOCKS_PER_CHUNK_SECTION_VEC))
                .block(modulo.getX().intValue(), modulo.getY().intValue(), modulo.getZ().intValue());
    }

    @Override
    public BlockRef getSelf() {
        return this;
    }

    @Override
    public boolean isSameCoordinateSystem(final BlockRef other) {
        return chunkSectionRef.isSameCoordinateSystem(other.chunkSectionRef);
    }

    public WorldRef getWorldRef() {
        return chunkSectionRef.getWorldRef();
    }

    public ChunkRef getChunkRef() {
        return chunkSectionRef.getChunkRef();
    }

    public RegionRef getRegionRef() {
        return chunkSectionRef.getRegionRef();
    }

    @Override
    public Vector3L subtract(final BlockRef other) {
        return chunkSectionRef.subtract(other.chunkSectionRef)
                .scale(Constants.BLOCKS_PER_CHUNK_SECTION_VEC)
                .add(new Vector3L(x - other.x, y - other.y, z - other.z));
    }

    @Override
    public Comparator<BlockRef> compareByCoordinate(final int coordinate) {
        return COMPARATORS.get(coordinate);
    }

    @Override
    public BlockRef withCoordinateFrom(final BlockRef other, final int coordinate) {
        if(coordinate >= coordinateSize())
            return this;
        return chunkSectionRef.withCoordinateFrom(other.chunkSectionRef, coordinate)
                .block(coordinate == COORDINATE_X ? other.x : x, coordinate == COORDINATE_Y ? other.y : y,
                        coordinate == COORDINATE_Z ? other.z : z);
    }

    public PositionRef position(final int x, final int y, final int z) {
        return new PositionRef(this, x, y, z);
    }

    @Override
    public PositionRef getStart() {
        return position(0, 0, 0);
    }

    @Override
    public PositionRef getEnd() {
        return position(Constants.OFFSETS_PER_BLOCK_X - 1, Constants.OFFSETS_PER_BLOCK_Y - 1,
                Constants.OFFSETS_PER_BLOCK_Z - 1);
    }

    @Override
    public Vector3L offsetTo(final ContainerBase<?> basis) {
        if(basis.equals(this))
            return Vector3L.IDENTITY;
        if(basis instanceof BlockRef)
            return subtract((BlockRef) basis);
        if(basis instanceof ColumnRef)
            return column().offsetTo(basis)
                    .toVec3D((long) chunkSectionRef.getY() * Constants.CHUNK_SECTIONS_PER_CHUNK + y);
        if(ContainerBase.CONTAINING_COMPARATOR.compare(getClass(), basis.getClass()) < 0)
            return getStart().offsetTo(basis).divide(Constants.OFFSETS_PER_BLOCK_VEC);
        return getParent().offsetTo(basis).scale(Constants.BLOCKS_PER_CHUNK_SECTION_VEC)
                .add(new Vector3L(x, y, z));
    }

    @Override
    public ChunkSectionRef getParent() {
        return chunkSectionRef;
    }
}
