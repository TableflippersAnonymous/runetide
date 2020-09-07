package com.runetide.services.internal.item.server.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import com.runetide.common.domain.IndexedEnum;

public enum AttachmentAttribute implements IndexedEnum {
    ;

    private final int id;

    AttachmentAttribute(int id) {
        this.id = id;
    }

    @Override
    @JsonValue
    public int toValue() {
        return id;
    }
}
