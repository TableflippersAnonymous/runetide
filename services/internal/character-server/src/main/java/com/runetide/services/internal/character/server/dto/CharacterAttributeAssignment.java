package com.runetide.services.internal.character.server.dto;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.runetide.services.internal.character.common.Attribute;
import com.runetide.services.internal.character.common.CharacterRef;

@Entity
@CqlName("character_attribute_assignment")
public class CharacterAttributeAssignment {
    @PartitionKey
    @CqlName("character_id")
    private CharacterRef characterId;
    @ClusteringColumn(0)
    @CqlName("level")
    private int level;
    @ClusteringColumn(1)
    @CqlName("idx")
    private int idx;
    @CqlName("attribute")
    private Attribute attribute;

    public CharacterAttributeAssignment() {
    }

    public CharacterAttributeAssignment(CharacterRef characterId, int level, int idx, Attribute attribute) {
        this.characterId = characterId;
        this.level = level;
        this.idx = idx;
        this.attribute = attribute;
    }

    public CharacterRef getCharacterId() {
        return characterId;
    }

    public void setCharacterId(CharacterRef characterId) {
        this.characterId = characterId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }
}
