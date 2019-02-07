package com.runetide.services.internal.region.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Block {
    @JsonProperty("t")
    private BlockType type;
    @JsonProperty("d")
    private Map<String, String> data;
    @JsonProperty("n")
    private int naturalLight;
    @JsonProperty("a")
    private int artificialLight;

    public Block(final BlockType type, final Map<String, String> data, final int naturalLight, final int artificialLight) {
        this.type = type;
        this.data = data;
        this.naturalLight = naturalLight;
        this.artificialLight = artificialLight;
    }

    public BlockType getType() {
        return type;
    }

    public void setType(final BlockType type) {
        this.type = type;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(final Map<String, String> data) {
        this.data = data;
    }

    public int getNaturalLight() {
        return naturalLight;
    }

    public void setNaturalLight(final int naturalLight) {
        this.naturalLight = naturalLight;
    }

    public int getArtificialLight() {
        return artificialLight;
    }

    public void setArtificialLight(final int artificialLight) {
        this.artificialLight = artificialLight;
    }
}
