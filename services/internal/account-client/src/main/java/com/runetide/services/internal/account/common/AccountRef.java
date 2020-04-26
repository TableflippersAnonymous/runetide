package com.runetide.services.internal.account.common;

import com.runetide.common.dto.UUIDRef;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class AccountRef extends UUIDRef<AccountRef> {
    public AccountRef(UUID uuidRef) {
        super(uuidRef);
    }

    public static AccountRef decode(final DataInputStream dataInputStream) throws IOException {
        return new AccountRef(decodeInternal(dataInputStream));
    }

    public static AccountRef valueOf(final String stringValue) {
        return new AccountRef(UUID.fromString(stringValue));
    }

    public UUID getId() {
        return getUuidRef();
    }
}
