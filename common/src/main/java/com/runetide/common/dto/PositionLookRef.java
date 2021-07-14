package com.runetide.common.dto;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

public class PositionLookRef implements Ref<PositionLookRef> {
    final PositionRef positionRef;
    final int elevation;
    final int rotation;

    public PositionLookRef(PositionRef positionRef, int elevation, int rotation) {
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
        return positionRef + "," + elevation + ":" + rotation;
    }

    public static PositionLookRef valueOf(final String stringValue) {
        final String[] parts = stringValue.split(",", 3);
        if(parts.length != 3)
            throw new IllegalArgumentException("Invalid PositionLookRef: " + stringValue);
        final PositionRef positionRef = PositionRef.valueOf(parts[0] + "," + parts[1]);
        final String[] coordinates = parts[2].split(":", 2);
        if(coordinates.length != 2)
            throw new IllegalArgumentException("Invalid PositionLookRef: " + stringValue);
        final int elevation = Integer.parseInt(coordinates[0]);
        final int rotation = Integer.parseInt(coordinates[1]);
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
