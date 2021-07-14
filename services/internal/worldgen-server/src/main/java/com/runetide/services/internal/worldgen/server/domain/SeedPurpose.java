package com.runetide.services.internal.worldgen.server.domain;

import java.nio.charset.StandardCharsets;

public enum SeedPurpose {
    HEIGHT("heightmap");

    private final byte[] bytes;

    SeedPurpose(final String s) {
        bytes = s.getBytes(StandardCharsets.UTF_8);
    }

    byte[] getBytes() {
        return bytes;
    }
}
