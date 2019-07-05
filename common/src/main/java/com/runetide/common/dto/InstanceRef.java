package com.runetide.common.dto;

import java.util.UUID;

public class InstanceRef extends UUIDRef<InstanceRef> {
    protected InstanceRef(final UUID uuidRef) {
        super(uuidRef);
    }

    public static InstanceRef valueOf(final String stringValue) {
        return new InstanceRef(UUID.fromString(stringValue));
    }
}
