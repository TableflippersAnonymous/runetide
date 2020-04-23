package com.runetide.common.services.cql;

import com.datastax.oss.driver.api.core.type.codec.TypeCodecs;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import net.jcip.annotations.Immutable;

import java.util.Objects;

@Immutable
public class EnumNameCodec<EnumT extends Enum<EnumT>> extends MappingCodec<String, EnumT> {

    private final Class<EnumT> enumClass;

    public EnumNameCodec(@NonNull Class<EnumT> enumClass) {
        super(
                TypeCodecs.TEXT,
                GenericType.of(Objects.requireNonNull(enumClass, "enumClass must not be null")));
        this.enumClass = enumClass;
    }

    @Nullable
    @Override
    protected EnumT innerToOuter(@Nullable String value) {
        return value == null || value.isEmpty() ? null : Enum.valueOf(enumClass, value);
    }

    @Nullable
    @Override
    protected String outerToInner(@Nullable EnumT value) {
        return value == null ? null : value.name();
    }
}
