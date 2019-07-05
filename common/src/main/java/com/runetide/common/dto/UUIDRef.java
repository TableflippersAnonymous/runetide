package com.runetide.common.dto;

import com.google.common.base.Objects;

import java.util.UUID;

public abstract class UUIDRef<T extends UUIDRef> implements Comparable<T> {
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

    protected UUID getUuidRef() {
        return uuidRef;
    }

    @Override
    public int compareTo(final T o) {
        return uuidRef.compareTo(o.getUuidRef());
    }
}
