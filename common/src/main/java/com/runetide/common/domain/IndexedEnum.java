package com.runetide.common.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public interface IndexedEnum {
    @JsonValue
    int toValue();
}
