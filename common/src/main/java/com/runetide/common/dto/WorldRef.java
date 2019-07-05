package com.runetide.common.dto;

import java.util.UUID;

public class WorldRef extends UUIDRef<WorldRef> {
    protected WorldRef(final UUID uuidRef) {
        super(uuidRef);
    }

    public static WorldRef valueOf(final String stringValue) {
        return new WorldRef(UUID.fromString(stringValue));
    }
}
