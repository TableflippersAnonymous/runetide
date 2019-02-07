package com.runetide.services.internal.region.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Column {
    @JsonProperty("t")
    private int top;
    @JsonProperty("b")
    private BiomeType biome;
}
