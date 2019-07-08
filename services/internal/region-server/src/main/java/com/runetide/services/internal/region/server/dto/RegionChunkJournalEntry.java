package com.runetide.services.internal.region.server.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.runetide.services.internal.region.common.Block;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegionChunkJournalEntry {
    @JsonProperty
    private short x;
    @JsonProperty
    private short y;
    @JsonProperty
    private short z;
    @JsonProperty("b")
    private Block block;

    public RegionChunkJournalEntry() {
    }

    public RegionChunkJournalEntry(final short x, final short y, final short z, final Block block) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.block = block;
    }

    public short getX() {
        return x;
    }

    public void setX(final short x) {
        this.x = x;
    }

    public short getY() {
        return y;
    }

    public void setY(final short y) {
        this.y = y;
    }

    public short getZ() {
        return z;
    }

    public void setZ(final short z) {
        this.z = z;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(final Block block) {
        this.block = block;
    }
}
