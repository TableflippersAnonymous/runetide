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

public class ChunkSectionRef implements ContainerRef<ChunkSectionRef, Vector3L, ChunkRef, BlockRef, Vector3L>,
        XYZCoordinates<ChunkSectionRef> {
    public static final Comparator<ChunkSectionRef> COMPARE_BY_X = Comparator
            .comparing(ChunkSectionRef::getChunkRef, ChunkRef.COMPARE_BY_X);
    public static final Comparator<ChunkSectionRef> COMPARE_BY_Y = Comparator
            .comparing(ChunkSectionRef::getWorldRef)
            .thenComparingInt(ChunkSectionRef::getY);
    public static final Comparator<ChunkSectionRef> COMPARE_BY_Z = Comparator
            .comparing(ChunkSectionRef::getChunkRef, ChunkRef.COMPARE_BY_Z);
    public static final List<Comparator<ChunkSectionRef>> COMPARATORS = ImmutableList.of(COMPARE_BY_X, COMPARE_BY_Z,
            COMPARE_BY_Y);
    public static final String PATH_REGEX = ChunkRef.PATH_REGEX + ":[0-9a-f]+";
    public static final int PATH_PARTS = ChunkRef.PATH_PARTS + 1;

    private final ChunkRef chunkRef;
    private final int y;

    ChunkSectionRef(final ChunkRef chunkRef, final int y) {
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
        return chunkRef + ":" + Integer.toString(y, 16);
    }

    public static ChunkSectionRef valueOf(final String stringValue) {
        final String[] parts = stringValue.split(":", PATH_PARTS);
        if(parts.length != PATH_PARTS)
            throw new IllegalArgumentException("Invalid ChunkSectionRef: " + stringValue);
        final ChunkRef chunkRef = ChunkRef.valueOf(Joiner.on(":").join(Arrays.copyOf(parts, ChunkRef.PATH_PARTS)));
        final int y = Integer.parseInt(parts[PATH_PARTS - 1], 16);
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
    public ChunkSectionRef add(final Vector3L vec) {
        final Vector3L sum = vec.add(new Vector3L(0, y, 0));
        final Vector3L modulo = sum.modulo(Constants.CHUNK_SECTIONS_PER_CHUNK_VEC);
        return chunkRef.add(sum.divide(Constants.CHUNK_SECTIONS_PER_CHUNK_VEC).toVec2D())
                .section(modulo.getY().intValue());
    }

    @Override
    public ChunkSectionRef getSelf() {
        return this;
    }

    @Override
    public Vector3L subtract(final ChunkSectionRef other) {
        return chunkRef.subtract(other.chunkRef)
                .scale(Constants.CHUNK_SECTIONS_PER_CHUNK_VEC)
                .add(new Vector3L(0, y - other.y, 0));
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

    @Override
    public BlockRef getStart() {
        return block(0, 0, 0);
    }

    @Override
    public BlockRef getEnd() {
        return block(Constants.BLOCKS_PER_CHUNK_SECTION_X - 1, Constants.BLOCKS_PER_CHUNK_SECTION_Y - 1,
                Constants.BLOCKS_PER_CHUNK_SECTION_Z - 1);
    }

    @Override
    public Vector3L offsetTo(final ContainerBase<?> basis) {
        if(basis.equals(this))
            return Vector3L.IDENTITY;
        if(basis instanceof ChunkSectionRef)
            return subtract((ChunkSectionRef) basis);
        if(ContainerBase.CONTAINING_COMPARATOR.compare(getClass(), basis.getClass()) < 0)
            return getStart().offsetTo(basis).divide(Constants.BLOCKS_PER_CHUNK_SECTION_VEC);
        return getParent().offsetTo(basis).scale(Constants.CHUNK_SECTIONS_PER_CHUNK_VEC)
                .toVec3D(y);
    }

    @Override
    public ChunkRef getParent() {
        return chunkRef;
    }
}
