package com.runetide.services.internal.region.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.runetide.common.dto.ChunkDataRef;
import com.runetide.common.dto.RegionRef;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegionChunkData {
    @JsonProperty("v")
    private long version;
    @JsonProperty("i")
    private ChunkDataRef id;
    @JsonProperty("r")
    private RegionRef regionId;
    @JsonProperty("t")
    private long createdAt;
    @JsonProperty("c")
    private Chunk[][] chunks;
}
