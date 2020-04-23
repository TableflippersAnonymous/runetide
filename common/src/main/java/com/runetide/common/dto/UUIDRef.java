package com.runetide.common.dto;

import com.google.common.base.Objects;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public abstract class UUIDRef<T extends UUIDRef<T>> implements Comparable<T> {
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

    public void encode(final DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeLong(uuidRef.getLeastSignificantBits());
        dataOutputStream.writeLong(uuidRef.getMostSignificantBits());
    }

    protected static UUID decodeInternal(final DataInputStream dataInputStream) throws IOException {
        final long lo = dataInputStream.readLong();
        final long hi = dataInputStream.readLong();
        return new UUID(hi, lo);
    }
}
