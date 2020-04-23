package com.runetide.services.internal.region.server.dto;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.runetide.common.dto.RegionRef;
import com.runetide.common.dto.WorldRef;

import java.util.Set;
import java.util.UUID;

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
    private UUID chunkDataId;
    @CqlName("instance_ids")
    private Set<UUID> instanceIds;
    @CqlName("new_instance_template")
    private String newInstanceTemplate;
    @CqlName("difficulty_level")
    private int difficultyLevel;
    @CqlName("settlement_ids")
    private Set<UUID> settlementIds;
    @CqlName("dungeon_ids")
    private Set<UUID> dungeonIds;

    public Region() {
    }

    public Region(final WorldRef worldId, final long x, final long z, final UUID chunkDataId,
                  final Set<UUID> instanceIds, final String newInstanceTemplate, final int difficultyLevel,
                  final Set<UUID> settlementIds, final Set<UUID> dungeonIds) {
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

    public UUID getChunkDataId() {
        return chunkDataId;
    }

    public void setChunkDataId(final UUID chunkDataId) {
        this.chunkDataId = chunkDataId;
    }

    public Set<UUID> getInstanceIds() {
        return instanceIds;
    }

    public void setInstanceIds(final Set<UUID> instanceIds) {
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

    public Set<UUID> getSettlementIds() {
        return settlementIds;
    }

    public void setSettlementIds(final Set<UUID> settlementIds) {
        this.settlementIds = settlementIds;
    }

    public Set<UUID> getDungeonIds() {
        return dungeonIds;
    }

    public void setDungeonIds(final Set<UUID> dungeonIds) {
        this.dungeonIds = dungeonIds;
    }

    public RegionRef toRef() {
        return new RegionRef(worldId, x, z);
    }
}
