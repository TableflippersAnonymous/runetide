package com.runetide.services.internal.region.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.runetide.common.dto.ChunkDataRef;
import com.runetide.common.dto.RegionRef;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegionChunkData {
    @JsonProperty("v")
    private long version;
    @JsonProperty("i")
    private ChunkDataRef id;
    @JsonProperty("r")
    private RegionRef regionId;
    @JsonProperty("t")
    private long createdAt;
    @JsonProperty("c")
    private Chunk[][] chunks;

    public RegionChunkData() {
    }

    public RegionChunkData(final long version, final ChunkDataRef id, final RegionRef regionId, final long createdAt,
                           final Chunk[][] chunks) {
        this.version = version;
        this.id = id;
        this.regionId = regionId;
        this.createdAt = createdAt;
        this.chunks = chunks;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(final long version) {
        this.version = version;
    }

    public ChunkDataRef getId() {
        return id;
    }

    public void setId(final ChunkDataRef id) {
        this.id = id;
    }

    public RegionRef getRegionId() {
        return regionId;
    }

    public void setRegionId(final RegionRef regionId) {
        this.regionId = regionId;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final long createdAt) {
        this.createdAt = createdAt;
    }

    public Chunk[][] getChunks() {
        return chunks;
    }

    public void setChunks(final Chunk[][] chunks) {
        this.chunks = chunks;
    }
}
