package com.runetide.common.dto;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class ChunkDataRef extends UUIDRef<ChunkDataRef> {

    public ChunkDataRef(final UUID uuidRef) {
        super(uuidRef);
    }

    public static ChunkDataRef decode(final DataInputStream dataInputStream) throws IOException {
        return new ChunkDataRef(decodeInternal(dataInputStream));
    }

    public static ChunkDataRef valueOf(final String stringValue) {
        return new ChunkDataRef(UUID.fromString(stringValue));
    }
}
