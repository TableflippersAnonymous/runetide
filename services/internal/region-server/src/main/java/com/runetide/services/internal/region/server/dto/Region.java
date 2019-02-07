package com.runetide.services.internal.region.server.dto;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.List;
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
    private List<UUID> instanceIds;
    @Column(name = "new_instance_template")
    private String newInstanceTemplate;
    @Column(name = "difficulty_level")
    private int difficultyLevel;
    @Column(name = "settlement_ids")
    private List<UUID> settlementIds;
    @Column(name = "dungeon_ids")
    private List<UUID> dungeonIds;

    public Region() {
    }

    public Region(final UUID worldId, final long x, final long z, final UUID chunkDataId, final List<UUID> instanceIds,
                  final String newInstanceTemplate, final int difficultyLevel, final List<UUID> settlementIds,
                  final List<UUID> dungeonIds) {
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

    public List<UUID> getInstanceIds() {
        return instanceIds;
    }

    public void setInstanceIds(final List<UUID> instanceIds) {
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

    public List<UUID> getSettlementIds() {
        return settlementIds;
    }

    public void setSettlementIds(final List<UUID> settlementIds) {
        this.settlementIds = settlementIds;
    }

    public List<UUID> getDungeonIds() {
        return dungeonIds;
    }

    public void setDungeonIds(final List<UUID> dungeonIds) {
        this.dungeonIds = dungeonIds;
    }
}
