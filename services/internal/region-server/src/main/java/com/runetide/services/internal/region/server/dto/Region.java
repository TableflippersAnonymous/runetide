package com.runetide.services.internal.region.server.dto;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.runetide.common.dto.ChunkDataRef;
import com.runetide.common.dto.DungeonRef;
import com.runetide.common.dto.InstanceRef;
import com.runetide.common.dto.RegionRef;
import com.runetide.common.dto.SettlementRef;
import com.runetide.common.dto.WorldRef;

import java.util.Set;

@Entity
@CqlName("region")
public class Region {
    @PartitionKey(0)
    @CqlName("world_id")
    private WorldRef worldId;
    @PartitionKey(1)
    private long x;
    @PartitionKey(2)
    private long z;
    @CqlName("chunk_data_id")
    private ChunkDataRef chunkDataId;
    @CqlName("instance_ids")
    private Set<InstanceRef> instanceIds;
    @CqlName("new_instance_template")
    private String newInstanceTemplate;
    @CqlName("difficulty_level")
    private int difficultyLevel;
    @CqlName("settlement_ids")
    private Set<SettlementRef> settlementIds;
    @CqlName("dungeon_ids")
    private Set<DungeonRef> dungeonIds;

    public Region() {
    }

    public Region(final WorldRef worldId, final long x, final long z, final ChunkDataRef chunkDataId,
                  final Set<InstanceRef> instanceIds, final String newInstanceTemplate, final int difficultyLevel,
                  final Set<SettlementRef> settlementIds, final Set<DungeonRef> dungeonIds) {
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

    public Set<InstanceRef> getInstanceIds() {
        return instanceIds;
    }

    public void setInstanceIds(final Set<InstanceRef> instanceIds) {
        this.instanceIds = instanceIds;
    }

    public String getNewInstanceTemplate() {
        return newInstanceTemplate;
    }

    public void setNewInstanceTemplate(final String newInstanceTemplate) {
        this.newInstanceTemplate = newInstanceTemplate;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(final int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public Set<SettlementRef> getSettlementIds() {
        return settlementIds;
    }

    public void setSettlementIds(final Set<SettlementRef> settlementIds) {
        this.settlementIds = settlementIds;
    }

    public Set<DungeonRef> getDungeonIds() {
        return dungeonIds;
    }

    public void setDungeonIds(final Set<DungeonRef> dungeonIds) {
        this.dungeonIds = dungeonIds;
    }

    public RegionRef toRef() {
        return worldId.region(x, z);
    }
}
