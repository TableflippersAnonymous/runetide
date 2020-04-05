package com.runetide.common.dto;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class ResourcePoolRef extends UUIDRef<ResourcePoolRef> {
    public ResourcePoolRef(UUID uuidRef) {
        super(uuidRef);
    }

    public static ResourcePoolRef decode(final DataInputStream dataInputStream) throws IOException {
        return new ResourcePoolRef(decodeInternal(dataInputStream));
    }

    public static ResourcePoolRef valueOf(final String stringValue) {
        return new ResourcePoolRef(UUID.fromString(stringValue));
    }

    public UUID getId() {
        return getUuidRef();
    }
}
