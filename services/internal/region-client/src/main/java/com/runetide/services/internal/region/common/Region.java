package com.runetide.services.internal.region.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.runetide.common.dto.ChunkDataRef;
import com.runetide.common.dto.DungeonRef;
import com.runetide.common.dto.InstanceRef;
import com.runetide.common.dto.SettlementRef;
import com.runetide.common.dto.WorldRef;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Region {
    @JsonProperty("world_id")
    private WorldRef worldId;
    @JsonProperty
    private long x;
    @JsonProperty
    private long z;
    @JsonProperty("chunk_data_id")
    private ChunkDataRef chunkDataId;
    @JsonProperty("instance_ids")
    private List<InstanceRef> instanceIds;
    @JsonProperty("new_instance_template")
    private InstanceTemplate newInstanceTemplate;
    @JsonProperty("difficulty_level")
    private int difficultyLevel;
    @JsonProperty("settlement_ids")
    private List<SettlementRef> settlementIds;
    @JsonProperty("dungeon_ids")
    private List<DungeonRef> dungeonIds;

    public Region() {
    }

    public Region(final WorldRef worldId, final long x, final long z, final ChunkDataRef chunkDataId,
                  final List<InstanceRef> instanceIds, final InstanceTemplate newInstanceTemplate,
                  final int difficultyLevel, final List<SettlementRef> settlementIds,
                  final List<DungeonRef> dungeonIds) {
        this.worldId = worldId;
        this.x = x;
        this.z = z;
        this.chunkDataId = chunkDataId;
        this.instanceIds = instanceIds;
        this.newInstanceTemplate = newInstanceTemplate;
        this.difficultyLevel = difficultyLevel;
        this.settlementIds = settlementIds;
        this.dungeonIds = dungeonIds;
    }

    public WorldRef getWorldId() {
        return worldId;
    }

    public void setWorldId(final WorldRef worldId) {
        this.worldId = worldId;
    }

    public long getX() {
        return x;
    }

    public void setX(final long x) {
        this.x = x;
    }

    public long getZ() {
        return z;
    }

    public void setZ(final long z) {
        this.z = z;
    }

    public ChunkDataRef getChunkDataId() {
        return chunkDataId;
    }

    public void setChunkDataId(final ChunkDataRef chunkDataId) {
        this.chunkDataId = chunkDataId;
    }

    public List<InstanceRef> getInstanceIds() {
        return instanceIds;
    }

    public void setInstanceIds(final List<InstanceRef> instanceIds) {
        this.instanceIds = instanceIds;
    }

    public InstanceTemplate getNewInstanceTemplate() {
        return newInstanceTemplate;
    }

    public void setNewInstanceTemplate(final InstanceTemplate newInstanceTemplate) {
        this.newInstanceTemplate = newInstanceTemplate;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(final int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public List<SettlementRef> getSettlementIds() {
        return settlementIds;
    }

    public void setSettlementIds(final List<SettlementRef> settlementIds) {
        this.settlementIds = settlementIds;
    }

    public List<DungeonRef> getDungeonIds() {
        return dungeonIds;
    }

    public void setDungeonIds(final List<DungeonRef> dungeonIds) {
        this.dungeonIds = dungeonIds;
    }
}
