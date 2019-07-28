package com.runetide.services.internal.region.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.runetide.common.domain.BiomeType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Column {
    @JsonProperty("t")
    private int top;
    @JsonProperty("b")
    private BiomeType biome;

    public Column() {
    }

    public Column(final int top, final BiomeType biome) {
        this.top = top;
        this.biome = biome;
    }

    public int getTop() {
        return top;
    }

    public void setTop(final int top) {
        this.top = top;
    }

    public BiomeType getBiome() {
        return biome;
    }

    public void setBiome(final BiomeType biome) {
        this.biome = biome;
    }
}
