package com.runetide.services.internal.world.common;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.runetide.common.dto.WorldRef;
import com.runetide.services.internal.multiverse.common.MultiverseRef;

@Entity
@CqlName("world")
@JsonIgnoreProperties(ignoreUnknown = true)
public class World {
    @PartitionKey
    @CqlName("id")
    @JsonProperty("$")
    private WorldRef id;
    @CqlName("kind")
    @JsonProperty("k")
    private WorldType kind;
    @CqlName("name")
    @JsonProperty("n")
    private String name;
    @CqlName("seed")
    @JsonProperty("s")
    private long seed;
    @CqlName("multiverse")
    @JsonProperty("m")
    private MultiverseRef multiverse;

    public World() {
    }

    public World(WorldRef id, WorldType kind, String name, long seed, MultiverseRef multiverse) {
        this.id = id;
        this.kind = kind;
        this.name = name;
        this.seed = seed;
        this.multiverse = multiverse;
    }

    public WorldRef getId() {
        return id;
    }

    public void setId(WorldRef id) {
        this.id = id;
    }

    public WorldType getKind() {
        return kind;
    }

    public void setKind(WorldType kind) {
        this.kind = kind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public MultiverseRef getMultiverse() {
        return multiverse;
    }

    public void setMultiverse(MultiverseRef multiverse) {
        this.multiverse = multiverse;
    }
}
