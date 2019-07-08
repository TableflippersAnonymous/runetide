package com.runetide.services.internal.region.server.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.runetide.services.internal.region.common.Block;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegionChunkJournalEntry {
    @JsonProperty
    private int x;
    @JsonProperty
    private int y;
    @JsonProperty
    private int z;
    @JsonProperty("b")
    private Block block;

    public RegionChunkJournalEntry() {
    }

    public RegionChunkJournalEntry(final int x, final int y, final int z, final Block block) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.block = block;
    }

    public int getX() {
        return x;
    }

    public void setX(final int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(final int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(final int z) {
        this.z = z;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(final Block block) {
        this.block = block;
    }
}
