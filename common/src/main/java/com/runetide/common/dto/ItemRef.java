package com.runetide.common.dto;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class ItemRef extends UUIDRef<ItemRef> {
    protected ItemRef(final UUID uuidRef) {
        super(uuidRef);
    }

    public static ItemRef decode(final DataInputStream dataInputStream) throws IOException {
        return new ItemRef(decodeInternal(dataInputStream));
    }

    public static ItemRef valueOf(final String stringValue) {
        return new ItemRef(UUID.fromString(stringValue));
    }

    public UUID getId() {
        return getUuidRef();
    }
}
