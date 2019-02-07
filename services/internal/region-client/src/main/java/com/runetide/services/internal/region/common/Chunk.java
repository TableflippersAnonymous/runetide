package com.runetide.services.internal.region.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Chunk {
    @JsonProperty("s")
    private ChunkSection[] chunkSections;
    @JsonProperty("c")
    private Column[][] columns;
}
