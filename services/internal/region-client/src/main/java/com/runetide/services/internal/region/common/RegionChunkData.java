package com.runetide.services.internal.region.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegionChunkData {
    @JsonProperty("v")
    private long version;
    @JsonProperty("i")
    private UUID id;
    @JsonProperty("t")
    private long createdAt;
    @JsonProperty("c")
    private Chunk[][] chunks;
}
