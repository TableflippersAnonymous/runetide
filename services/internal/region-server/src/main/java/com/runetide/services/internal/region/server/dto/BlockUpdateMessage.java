package com.runetide.services.internal.region.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.runetide.common.TopicMessage;
import com.runetide.common.dto.WorldRef;
import com.runetide.services.internal.region.common.Block;

public class BlockUpdateMessage extends TopicMessage {
    @JsonProperty("w")
    private WorldRef worldId;
    @JsonProperty
    private long rx;
    @JsonProperty
    private long rz;
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

    public BlockUpdateMessage() {
    }

    public BlockUpdateMessage(final WorldRef worldId, final long rx, final long rz, final int cx, final int cz,
                              final int sy, final int bx, final int by, final int bz, final Block block) {
        this.worldId = worldId;
        this.rx = rx;
        this.rz = rz;
        this.cx = cx;
        this.cz = cz;
        this.sy = sy;
        this.bx = bx;
        this.by = by;
        this.bz = bz;
        this.block = block;
    }

    public WorldRef getWorldId() {
        return worldId;
    }

    public void setWorldId(final WorldRef worldId) {
        this.worldId = worldId;
    }

    public long getRx() {
        return rx;
    }

    public void setRx(final long rx) {
        this.rx = rx;
    }

    public long getRz() {
        return rz;
    }

    public void setRz(final long rz) {
        this.rz = rz;
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