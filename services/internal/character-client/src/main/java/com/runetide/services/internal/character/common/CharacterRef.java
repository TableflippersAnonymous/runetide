package com.runetide.services.internal.character.common;

import com.runetide.common.dto.UUIDRef;
import com.runetide.common.services.cql.UUIDRefCodec;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class CharacterRef extends UUIDRef<CharacterRef> {
    public static final UUIDRefCodec<CharacterRef> CODEC = new UUIDRefCodec<>(CharacterRef.class, CharacterRef::new);

    CharacterRef(final UUID uuidRef) {
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
