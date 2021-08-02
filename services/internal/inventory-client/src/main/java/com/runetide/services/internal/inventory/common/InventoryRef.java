package com.runetide.services.internal.inventory.common;

import com.runetide.common.dto.UUIDRef;
import com.runetide.common.services.cql.UUIDRefCodec;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class InventoryRef extends UUIDRef<InventoryRef> {
    public static final UUIDRefCodec<InventoryRef> CODEC = new UUIDRefCodec<>(InventoryRef.class, InventoryRef::new);

    InventoryRef(final UUID uuidRef) {
        super(uuidRef);
    }

    public static InventoryRef decode(final DataInputStream dataInputStream) throws IOException {
        return new InventoryRef(decodeInternal(dataInputStream));
    }

    public static InventoryRef valueOf(final String stringValue) {
        return new InventoryRef(UUID.fromString(stringValue));
    }

    public UUID getId() {
        return getUuidRef();
    }
}
