package com.runetide.services.internal.region.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Region {
    @JsonProperty("world_id")
    private UUID worldId;
    @JsonProperty
    private long x;
    @JsonProperty
    private long z;
    @JsonProperty("chunk_data_id")
    private UUID chunkDataId;
    @JsonProperty("instance_ids")
    private List<UUID> instanceIds;
    @JsonProperty("new_instance_template")
    private InstanceTemplate newInstanceTemplate;
    @JsonProperty("difficulty_level")
    private int difficultyLevel;
    @JsonProperty("settlement_ids")
    private List<UUID> settlementIds;
    @JsonProperty("dungeon_ids")
    private List<UUID> dungeonIds;
}
