package com.runetide.services.internal.entity.common.dto;

import com.runetide.common.dto.UUIDRef;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class EntityRef extends UUIDRef<EntityRef> {
    public EntityRef(final UUID uuidRef) {
        super(uuidRef);
    }

    public static EntityRef decode(final DataInputStream dataInputStream) throws IOException {
        return new EntityRef(decodeInternal(dataInputStream));
    }

    public static EntityRef valueOf(final String stringValue) {
        return new EntityRef(UUID.fromString(stringValue));
    }

    public UUID getId() {
        return getUuidRef();
    }
}
