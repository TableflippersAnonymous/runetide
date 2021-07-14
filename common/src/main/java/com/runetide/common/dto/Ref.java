package com.runetide.common.dto;

import java.io.DataOutput;
import java.io.IOException;

public interface Ref<T extends Ref<T>> {
    void encode(final DataOutput dataOutput) throws IOException;
}
