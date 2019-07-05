package com.runetide.common.dto;

import java.util.UUID;

public class ChunkDataRef extends UUIDRef<ChunkDataRef> {

    protected ChunkDataRef(final UUID uuidRef) {
        super(uuidRef);
    }

    public static ChunkDataRef valueOf(final String stringValue) {
        return new ChunkDataRef(UUID.fromString(stringValue));
    }
}
