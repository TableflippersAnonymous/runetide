package com.runetide.common.dto;

import java.util.UUID;

public class DungeonRef extends UUIDRef<DungeonRef> {
    protected DungeonRef(final UUID uuidRef) {
        super(uuidRef);
    }

    public static DungeonRef valueOf(final String stringValue) {
        return new DungeonRef(UUID.fromString(stringValue));
    }
}
