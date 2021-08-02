package com.runetide.services.internal.multiverse.common;

import com.runetide.common.dto.UUIDRef;
import com.runetide.common.services.cql.UUIDRefCodec;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class MultiverseRef extends UUIDRef<MultiverseRef> {
    public static final UUIDRefCodec<MultiverseRef> CODEC = new UUIDRefCodec<>(MultiverseRef.class, MultiverseRef::new);

    MultiverseRef(UUID uuidRef) {
        super(uuidRef);
    }

    public static MultiverseRef decode(final DataInputStream dataInputStream) throws IOException {
        return new MultiverseRef(decodeInternal(dataInputStream));
    }

    public static MultiverseRef valueOf(final String stringValue) {
        return new MultiverseRef(UUID.fromString(stringValue));
    }

    public UUID getId() {
        return getUuidRef();
    }
}
