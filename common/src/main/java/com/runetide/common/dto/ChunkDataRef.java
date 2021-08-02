package com.runetide.common.dto;

import com.runetide.common.services.cql.UUIDRefCodec;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class ChunkDataRef extends UUIDRef<ChunkDataRef> {
    public static final UUIDRefCodec<ChunkDataRef> CODEC = new UUIDRefCodec<>(ChunkDataRef.class, ChunkDataRef::new);

    ChunkDataRef(final UUID uuidRef) {
        super(uuidRef);
    }

    public static ChunkDataRef decode(final DataInputStream dataInputStream) throws IOException {
        return new ChunkDataRef(decodeInternal(dataInputStream));
    }

    public static ChunkDataRef valueOf(final String stringValue) {
        return new ChunkDataRef(UUID.fromString(stringValue));
    }

    public static ChunkDataRef random() {
        return new ChunkDataRef(UUID.randomUUID());
    }
}
