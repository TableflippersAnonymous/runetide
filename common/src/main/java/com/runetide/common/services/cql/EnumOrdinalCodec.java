package com.runetide.common.services.cql;

import com.datastax.oss.driver.api.core.type.codec.TypeCodecs;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import net.jcip.annotations.Immutable;

import java.util.Objects;

@Immutable
public class EnumOrdinalCodec<EnumT extends Enum<EnumT>> extends MappingCodec<Integer, EnumT> {

    private final EnumT[] enumConstants;

    public EnumOrdinalCodec(@NonNull Class<EnumT> enumClass) {
        super(
                TypeCodecs.INT,
                GenericType.of(Objects.requireNonNull(enumClass, "enumClass must not be null")));
        this.enumConstants = enumClass.getEnumConstants();
    }

    @Nullable
    @Override
    protected EnumT innerToOuter(@Nullable Integer value) {
        return value == null ? null : enumConstants[value];
    }

    @Nullable
    @Override
    protected Integer outerToInner(@Nullable EnumT value) {
        return value == null ? null : value.ordinal();
    }
}
