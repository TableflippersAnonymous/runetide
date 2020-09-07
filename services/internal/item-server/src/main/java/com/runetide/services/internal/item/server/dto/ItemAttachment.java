package com.runetide.services.internal.item.server.dto;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;

import java.util.Map;

@Entity
@CqlName("item_attachment")
public class ItemAttachment {
    @CqlName("kind")
    private ItemAttachmentType kind;
    @CqlName("modifiers")
    private Map<AttachmentModifier, Integer> modifiers;
    @CqlName("attributes")
    private Map<AttachmentAttribute, String> attributes;

    public ItemAttachment() {
    }

    public ItemAttachment(ItemAttachmentType kind, Map<AttachmentModifier, Integer> modifiers,
                          Map<AttachmentAttribute, String> attributes) {
        this.kind = kind;
        this.modifiers = modifiers;
        this.attributes = attributes;
    }

    public ItemAttachmentType getKind() {
        return kind;
    }

    public void setKind(ItemAttachmentType kind) {
        this.kind = kind;
    }

    public Map<AttachmentModifier, Integer> getModifiers() {
        return modifiers;
    }

    public void setModifiers(Map<AttachmentModifier, Integer> modifiers) {
        this.modifiers = modifiers;
    }

    public Map<AttachmentAttribute, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<AttachmentAttribute, String> attributes) {
        this.attributes = attributes;
    }
}
