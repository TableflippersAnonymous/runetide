package com.runetide.common.services.cql;

import com.datastax.oss.driver.api.core.type.codec.TypeCodecs;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;
import com.runetide.common.dto.UUIDRef;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

public class UUIDRefCodec<T extends UUIDRef<T>> extends MappingCodec<UUID, T> {
    private final Function<UUID, T> constructor;

    public UUIDRefCodec(@NonNull Class<T> uuidRefClass, @NonNull Function<UUID, T> constructor) {
        super(
                TypeCodecs.UUID,
                GenericType.of(Objects.requireNonNull(uuidRefClass, "uuidRefClass must not be null"))
        );
        this.constructor = constructor;
    }

    @Nullable
    @Override
    protected T innerToOuter(@Nullable UUID value) {
        return value == null ? null : constructor.apply(value);
    }

    @Nullable
    @Override
    protected UUID outerToInner(@Nullable T value) {
        return value == null ? null : value.getUuidRef();
    }
}
