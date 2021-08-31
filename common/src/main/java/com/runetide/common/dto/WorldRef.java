package com.runetide.common.dto;

import com.runetide.common.services.cql.UUIDRefCodec;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class WorldRef extends UUIDRef<WorldRef> implements ContainerBase<WorldRef> {
    public static final UUIDRefCodec<WorldRef> CODEC = new UUIDRefCodec<>(WorldRef.class, WorldRef::new);
    public static final String PATH_REGEX = UUIDRef.PATH_REGEX;
    public static final int PATH_PARTS = UUIDRef.PATH_PARTS;

    WorldRef(final UUID uuidRef) {
        super(uuidRef);
    }

    public static WorldRef decode(final DataInputStream dataInputStream) throws IOException {
        return new WorldRef(decodeInternal(dataInputStream));
    }

    public static WorldRef valueOf(final String stringValue) {
        return new WorldRef(UUID.fromString(stringValue));
    }

    public static WorldRef random() {
        return new WorldRef(UUID.randomUUID());
    }

    public SectorRef sector(final long x, final long z) {
        return new SectorRef(this, x, z);
    }

    public RegionRef region(final long x, final long z) {
        return new RegionRef(this, x, z);
    }

    public UUID getId() {
        return getUuidRef();
    }
}
