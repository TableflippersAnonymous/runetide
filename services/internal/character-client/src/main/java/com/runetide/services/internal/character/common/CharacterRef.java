package com.runetide.services.internal.character.common;

import com.runetide.common.dto.UUIDRef;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class CharacterRef extends UUIDRef<CharacterRef> {
    public CharacterRef(UUID uuidRef) {
        super(uuidRef);
    }

    public static CharacterRef decode(final DataInputStream dataInputStream) throws IOException {
        return new CharacterRef(decodeInternal(dataInputStream));
    }

    public static CharacterRef valueOf(final String stringValue) {
        return new CharacterRef(UUID.fromString(stringValue));
    }

    public UUID getId() {
        return getUuidRef();
    }
}
