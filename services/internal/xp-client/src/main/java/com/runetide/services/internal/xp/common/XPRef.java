package com.runetide.services.internal.xp.common;

import com.runetide.common.dto.UUIDRef;
import com.runetide.common.services.cql.UUIDRefCodec;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class XPRef extends UUIDRef<XPRef> {
    public static final UUIDRefCodec<XPRef> CODEC = new UUIDRefCodec<>(XPRef.class, XPRef::new);

    XPRef(UUID uuidRef) {
        super(uuidRef);
    }

    public static XPRef decode(final DataInputStream dataInputStream) throws IOException {
        return new XPRef(decodeInternal(dataInputStream));
    }

    public static XPRef valueOf(final String stringValue) {
        return new XPRef(UUID.fromString(stringValue));
    }

    public static XPRef random() {
        return new XPRef(UUID.randomUUID());
    }

    public UUID getId() {
        return getUuidRef();
    }
}
