package com.runetide.common.services.cql;

import com.datastax.oss.driver.api.core.type.codec.TypeCodecs;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;
import com.runetide.common.domain.IndexedEnum;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import net.jcip.annotations.Immutable;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Immutable
public class EnumIndexedCodec<EnumT extends Enum<EnumT> & IndexedEnum> extends MappingCodec<Integer, EnumT> {

    private final Map<Integer, EnumT> enumConstants;

    public EnumIndexedCodec(@NonNull Class<EnumT> enumClass) {
        super(
                TypeCodecs.INT,
                GenericType.of(Objects.requireNonNull(enumClass, "enumClass must not be null")));
        enumConstants = Arrays.stream(enumClass.getEnumConstants())
                .collect(Collectors.toMap(IndexedEnum::toValue, Function.identity()));
    }

    @Nullable
    @Override
    protected EnumT innerToOuter(@Nullable Integer value) {
        return value == null ? null : enumConstants.get(value);
    }

    @Nullable
    @Override
    protected Integer outerToInner(@Nullable EnumT value) {
        return value == null ? null : value.toValue();
    }
}
