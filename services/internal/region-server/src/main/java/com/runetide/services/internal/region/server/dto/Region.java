package com.runetide.services.internal.region.server.dto;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.runetide.common.dto.RegionRef;
import com.runetide.common.dto.WorldRef;

import java.util.Set;
import java.util.UUID;

@Table(name = "region")
public class Region {
    @PartitionKey(0)
    @Column(name = "world_id")
    private UUID worldId;
    @PartitionKey(1)
    private long x;
    @PartitionKey(2)
    private long z;
    @Column(name = "chunk_data_id")
    private UUID chunkDataId;
    @Column(name = "instance_ids")
    private Set<UUID> instanceIds;
    @Column(name = "new_instance_template")
    private String newInstanceTemplate;
    @Column(name = "difficulty_level")
    private int difficultyLevel;
    @Column(name = "settlement_ids")
    private Set<UUID> settlementIds;
    @Column(name = "dungeon_ids")
    private Set<UUID> dungeonIds;

    public Region() {
    }

    public Region(final UUID worldId, final long x, final long z, final UUID chunkDataId, final Set<UUID> instanceIds,
                  final String newInstanceTemplate, final int difficultyLevel, final Set<UUID> settlementIds,
                  final Set<UUID> dungeonIds) {
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

    public UUID getWorldId() {
        return worldId;
    }

    public void setWorldId(final UUID worldId) {
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
        return new RegionRef(new WorldRef(worldId), x, z);
    }
}
