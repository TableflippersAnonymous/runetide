package com.runetide.common.dto;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class InstanceRef extends UUIDRef<InstanceRef> {
    public InstanceRef(final UUID uuidRef) {
        super(uuidRef);
    }

    public static InstanceRef decode(final DataInputStream dataInputStream) throws IOException {
        return new InstanceRef(decodeInternal(dataInputStream));
    }

    public static InstanceRef valueOf(final String stringValue) {
        return new InstanceRef(UUID.fromString(stringValue));
    }
}
