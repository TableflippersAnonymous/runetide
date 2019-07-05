package com.runetide.services.internal.region.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.runetide.common.dto.ChunkDataRef;
import com.runetide.common.dto.DungeonRef;
import com.runetide.common.dto.InstanceRef;
import com.runetide.common.dto.SettlementRef;
import com.runetide.common.dto.WorldRef;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Region {
    @JsonProperty("world_id")
    private WorldRef worldId;
    @JsonProperty
    private long x;
    @JsonProperty
    private long z;
    @JsonProperty("chunk_data_id")
    private ChunkDataRef chunkDataId;
    @JsonProperty("instance_ids")
    private List<InstanceRef> instanceIds;
    /*@JsonProperty("new_instance_template")
    private InstanceTemplate newInstanceTemplate;*/
    @JsonProperty("difficulty_level")
    private int difficultyLevel;
    @JsonProperty("settlement_ids")
    private List<SettlementRef> settlementIds;
    @JsonProperty("dungeon_ids")
    private List<DungeonRef> dungeonIds;
}
