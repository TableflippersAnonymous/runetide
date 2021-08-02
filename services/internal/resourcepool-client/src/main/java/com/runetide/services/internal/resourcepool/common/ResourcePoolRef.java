package com.runetide.services.internal.resourcepool.common;

import com.runetide.common.dto.UUIDRef;
import com.runetide.common.services.cql.UUIDRefCodec;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class ResourcePoolRef extends UUIDRef<ResourcePoolRef> {
    public static final UUIDRefCodec<ResourcePoolRef> CODEC = new UUIDRefCodec<>(ResourcePoolRef.class,
            ResourcePoolRef::new);

    ResourcePoolRef(UUID uuidRef) {
        super(uuidRef);
    }

    public static ResourcePoolRef decode(final DataInputStream dataInputStream) throws IOException {
        return new ResourcePoolRef(decodeInternal(dataInputStream));
    }

    public static ResourcePoolRef valueOf(final String stringValue) {
        return new ResourcePoolRef(UUID.fromString(stringValue));
    }

    public static ResourcePoolRef random() {
        return new ResourcePoolRef(UUID.randomUUID());
    }

    public UUID getId() {
        return getUuidRef();
    }
}
