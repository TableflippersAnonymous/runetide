package com.runetide.common.dto;

import java.util.UUID;

public class SettlementRef extends UUIDRef<SettlementRef> {
    protected SettlementRef(final UUID uuidRef) {
        super(uuidRef);
    }

    public static SettlementRef valueOf(final String stringValue) {
        return new SettlementRef(UUID.fromString(stringValue));
    }
}
