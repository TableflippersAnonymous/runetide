package com.runetide.services.internal.xp.common;

import com.runetide.common.dto.UUIDRef;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class XPRef extends UUIDRef<XPRef> {
    public XPRef(UUID uuidRef) {
        super(uuidRef);
    }

    public static XPRef decode(final DataInputStream dataInputStream) throws IOException {
        return new XPRef(decodeInternal(dataInputStream));
    }

    public static XPRef valueOf(final String stringValue) {
        return new XPRef(UUID.fromString(stringValue));
    }

    public UUID getId() {
        return getUuidRef();
    }
}
