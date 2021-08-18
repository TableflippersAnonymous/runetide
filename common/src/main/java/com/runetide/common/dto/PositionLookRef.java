package com.runetide.common.dto;

import com.google.common.base.Joiner;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class PositionLookRef implements ContainerBase<PositionLookRef> {
    public static final String PATH_REGEX = PositionRef.PATH_REGEX + ":[0-9a-z]+:[0-9a-z]+";
    public static final int PATH_PARTS = PositionRef.PATH_PARTS + 2;

    private final PositionRef positionRef;
    private final int elevation;
    private final int rotation;

    PositionLookRef(PositionRef positionRef, int elevation, int rotation) {
        this.positionRef = positionRef;
        this.elevation = elevation;
        this.rotation = rotation;
    }

    public PositionRef getPositionRef() {
        return positionRef;
    }

    public int getElevation() {
        return elevation;
    }

    public int getRotation() {
        return rotation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PositionLookRef that = (PositionLookRef) o;
        return elevation == that.elevation &&
                rotation == that.rotation &&
                positionRef.equals(that.positionRef);
    }

    @Override
    public int hashCode() {
        return Objects.hash(positionRef, elevation, rotation);
    }

    @Override
    public String toString() {
        return positionRef + ":" + Integer.toString(elevation, 36) + ":" + Integer.toString(rotation, 36);
    }

    public static PositionLookRef valueOf(final String stringValue) {
        final String[] parts = stringValue.split(":", PATH_PARTS);
        if(parts.length != PATH_PARTS)
            throw new IllegalArgumentException("Invalid PositionLookRef: " + stringValue);
        final PositionRef positionRef = PositionRef.valueOf(Joiner.on(":")
                .join(Arrays.copyOf(parts, PositionRef.PATH_PARTS)));
        final int elevation = Integer.parseInt(parts[PATH_PARTS - 2], 36);
        final int rotation = Integer.parseInt(parts[PATH_PARTS - 1], 36);
        return new PositionLookRef(positionRef, elevation, rotation);
    }

    public void encode(final DataOutput dataOutput) throws IOException {
        positionRef.encode(dataOutput);
        dataOutput.writeInt(elevation);
        dataOutput.writeInt(rotation);
    }

    public static PositionLookRef decode(final DataInputStream dataInputStream) throws IOException {
        final PositionRef positionRef = PositionRef.decode(dataInputStream);
        final int elevation = dataInputStream.readInt();
        final int rotation = dataInputStream.readInt();
        return new PositionLookRef(positionRef, elevation, rotation);
    }
}
