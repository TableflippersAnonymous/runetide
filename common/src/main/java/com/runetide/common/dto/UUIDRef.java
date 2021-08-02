package com.runetide.common.dto;

import com.google.common.base.Objects;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.util.UUID;

public abstract class UUIDRef<T extends UUIDRef<T>> implements Comparable<T>, Ref<T> {
    protected static final String PATH_REGEX = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
    protected static final int PATH_PARTS = 1;

    private final UUID uuidRef;

    protected UUIDRef(final UUID uuidRef) {
        this.uuidRef = uuidRef;
    }

    @Override
    public String toString() {
        return uuidRef.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final UUIDRef uuidRef1 = (UUIDRef) o;
        return Objects.equal(uuidRef, uuidRef1.uuidRef);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuidRef);
    }

    public UUID getUuidRef() {
        return uuidRef;
    }

    @Override
    public int compareTo(final T o) {
        return uuidRef.compareTo(o.getUuidRef());
    }

    public void encode(final DataOutput dataOutput) throws IOException {
        dataOutput.writeLong(uuidRef.getLeastSignificantBits());
        dataOutput.writeLong(uuidRef.getMostSignificantBits());
    }

    protected static UUID decodeInternal(final DataInputStream dataInputStream) throws IOException {
        final long lo = dataInputStream.readLong();
        final long hi = dataInputStream.readLong();
        return new UUID(hi, lo);
    }
}
