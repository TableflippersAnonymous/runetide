package com.runetide.common.dto;

import com.runetide.common.services.cql.UUIDRefCodec;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class DungeonRef extends UUIDRef<DungeonRef> {
    public static final UUIDRefCodec<DungeonRef> CODEC = new UUIDRefCodec<>(DungeonRef.class, DungeonRef::new);

    DungeonRef(final UUID uuidRef) {
        super(uuidRef);
    }

    public static DungeonRef decode(final DataInputStream dataInputStream) throws IOException {
        return new DungeonRef(decodeInternal(dataInputStream));
    }

    public static DungeonRef valueOf(final String stringValue) {
        return new DungeonRef(UUID.fromString(stringValue));
    }
}
