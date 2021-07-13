package com.runetide.common.dto;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class WorldRef extends UUIDRef<WorldRef> {
    public WorldRef(final UUID uuidRef) {
        super(uuidRef);
    }

    public static WorldRef decode(final DataInputStream dataInputStream) throws IOException {
        return new WorldRef(decodeInternal(dataInputStream));
    }

    public static WorldRef valueOf(final String stringValue) {
        return new WorldRef(UUID.fromString(stringValue));
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
