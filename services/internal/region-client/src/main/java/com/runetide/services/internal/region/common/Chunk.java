package com.runetide.services.internal.region.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Chunk {
    @JsonProperty("s")
    private ChunkSection[] chunkSections;
    @JsonProperty("c")
    private Column[][] columns;

    public Chunk() {
    }

    public Chunk(final ChunkSection[] chunkSections, final Column[][] columns) {
        this.chunkSections = chunkSections;
        this.columns = columns;
    }

    public ChunkSection[] getChunkSections() {
        return chunkSections;
    }

    public void setChunkSections(final ChunkSection[] chunkSections) {
        this.chunkSections = chunkSections;
    }

    public Column[][] getColumns() {
        return columns;
    }

    public void setColumns(final Column[][] columns) {
        this.columns = columns;
    }
}
