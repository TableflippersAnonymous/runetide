package com.runetide.services.internal.worldgen.server.domain;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.common.primitives.Longs;
import com.runetide.common.dto.Ref;
import com.runetide.common.dto.SectorRef;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("UnstableApiUsage")
public abstract class BaseSeed {
    private static final byte[] SUBSEED = "subseed".getBytes(StandardCharsets.UTF_8);
    private static final LoadingCache<byte[], HashFunction> HASH_CACHE = CacheBuilder.newBuilder()
            .expireAfterAccess(30, TimeUnit.SECONDS)
            .build(CacheLoader.from(Hashing::hmacSha512));

    private final HashFunction hash;

    protected BaseSeed(final byte[] seed) {
        this.hash = HASH_CACHE.getUnchecked(seed);
    }

    protected BaseSeed(final long seed) {
        this(Longs.toByteArray(seed));
    }

    protected byte[] seedFor(final SeedPurpose seedPurpose) {
        return hash.hashBytes(seedPurpose.getBytes()).asBytes();
    }

    protected byte[] subSeed(final byte[]... parts) {
        final ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();
        dataOutput.write(SUBSEED);
        for(final byte[] bytes : parts)
            dataOutput.write(bytes);
        return hash.hashBytes(dataOutput.toByteArray()).asBytes();
    }

    protected long asLong(final byte[] seed) {
        return Longs.fromByteArray(seed);
    }

    protected byte[] ref(final byte[] prefix, final Ref<?> ref) {
        try {
            final ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();
            dataOutput.write(SUBSEED);
            dataOutput.write(prefix);
            ref.encode(dataOutput);
            return hash.hashBytes(dataOutput.toByteArray()).asBytes();
        } catch (final IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    public long getSeed(final SeedPurpose purpose) {
        return asLong(seedFor(purpose));
    }
}
