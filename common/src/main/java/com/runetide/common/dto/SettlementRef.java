package com.runetide.common.dto;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class SettlementRef extends UUIDRef<SettlementRef> {
    public SettlementRef(final UUID uuidRef) {
        super(uuidRef);
    }

    public static SettlementRef decode(final DataInputStream dataInputStream) throws IOException {
        return new SettlementRef(decodeInternal(dataInputStream));
    }

    public static SettlementRef valueOf(final String stringValue) {
        return new SettlementRef(UUID.fromString(stringValue));
    }
}
