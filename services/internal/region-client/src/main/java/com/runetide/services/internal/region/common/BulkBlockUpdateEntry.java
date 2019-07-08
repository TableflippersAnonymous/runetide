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

    public BulkBlockUpdateEntry() {
    }

    public BulkBlockUpdateEntry(final int cx, final int cz, final int sy, final int bx, final int by, final int bz,
                                final Block block) {
        this.cx = cx;
        this.cz = cz;
        this.sy = sy;
        this.bx = bx;
        this.by = by;
        this.bz = bz;
        this.block = block;
    }

    public int getCx() {
        return cx;
    }

    public void setCx(final int cx) {
        this.cx = cx;
    }

    public int getCz() {
        return cz;
    }

    public void setCz(final int cz) {
        this.cz = cz;
    }

    public int getSy() {
        return sy;
    }

    public void setSy(final int sy) {
        this.sy = sy;
    }

    public int getBx() {
        return bx;
    }

    public void setBx(final int bx) {
        this.bx = bx;
    }

    public int getBy() {
        return by;
    }

    public void setBy(final int by) {
        this.by = by;
    }

    public int getBz() {
        return bz;
    }

    public void setBz(final int bz) {
        this.bz = bz;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(final Block block) {
        this.block = block;
    }
}
