package com.runetide.services.internal.region.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BulkBlockUpdateEntry {
    @JsonProperty
    private int cx;
    @JsonProperty
    private int cz;
    @JsonProperty
    private int sy;
    @JsonProperty
    private int bx;
    @JsonProperty
    private int by;
    @JsonProperty
    private int bz;
    @JsonProperty("b")
    private Block block;
}
