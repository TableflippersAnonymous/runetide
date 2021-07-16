package com.runetide.common.dto;

import org.jetbrains.annotations.Contract;

import java.io.DataOutput;
import java.io.IOException;

public interface Ref<T extends Ref<T>> {
    @Contract(pure = true)
    void encode(final DataOutput dataOutput) throws IOException;
}
