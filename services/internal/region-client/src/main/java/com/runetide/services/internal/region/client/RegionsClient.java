package com.runetide.services.internal.region.client;

import com.runetide.common.dto.WorldRef;
import com.runetide.services.internal.region.common.Block;
import com.runetide.services.internal.region.common.BulkBlockUpdateRequest;
import com.runetide.services.internal.region.common.Chunk;
import com.runetide.services.internal.region.common.ChunkSection;
import com.runetide.services.internal.region.common.LoadRegionRequest;
import com.runetide.services.internal.region.common.Region;
import com.runetide.services.internal.region.common.RegionChunkData;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public class RegionsClient {
    private static final String ACCEPT = MediaType.APPLICATION_JSON;
    private final WebTarget regions;

    public RegionsClient(final WebTarget regions) {
        this.regions = regions;
    }

    public Response loadRegion(final LoadRegionRequest loadRegionRequest) {
        return regions
                .request(ACCEPT)
                .post(Entity.entity(loadRegionRequest, MediaType.APPLICATION_JSON), Response.class);
    }

    public List<Region> getRegions() {
        return regions
                .request(ACCEPT)
                .get(new GenericType<List<Region>>() {});
    }

    public Region getRegion(final WorldRef worldId, final long rx, final long rz) {
        return regions
                .path(worldId.toString()).path(String.valueOf(rx)).path(String.valueOf(rz))
                .request(ACCEPT)
                .get(Region.class);
    }

    public RegionChunkData bulkUpdate(final WorldRef worldId, final long rx, final long rz,
                                      final BulkBlockUpdateRequest bulkBlockUpdateRequest) {
        return regions
                .path(worldId.toString()).path(String.valueOf(rx)).path(String.valueOf(rz))
                .request(ACCEPT)
                .post(Entity.entity(bulkBlockUpdateRequest, MediaType.APPLICATION_JSON), RegionChunkData.class);
    }

    public Response unloadRegion(final WorldRef worldId, final long rx, final long rz) {
        return regions
                .path(worldId.toString()).path(String.valueOf(rx)).path(String.valueOf(rz))
                .request(ACCEPT)
                .delete(Response.class);
    }

    public Chunk getChunk(final WorldRef worldId, final long rx, final long rz, final int cx, final int cz) {
        return regions
                .path(worldId.toString()).path(String.valueOf(rx)).path(String.valueOf(rz))
                .path(String.valueOf(cx)).path(String.valueOf(cz))
                .request(ACCEPT)
                .get(Chunk.class);
    }

    public ChunkSection getChunkSection(final WorldRef worldId, final long rx, final long rz, final int cx,
                                        final int cz, final int sy) {
        return regions
                .path(worldId.toString()).path(String.valueOf(rx)).path(String.valueOf(rz))
                .path(String.valueOf(cx)).path(String.valueOf(cz)).path(String.valueOf(sy))
                .request(ACCEPT)
                .get(ChunkSection.class);
    }

    public ChunkSection putBlock(final WorldRef worldId, final long rx, final long rz, final int cx, final int cz,
                                 final int sy, final int bx, final int by, final int bz, final Block block) {
        return regions
                .path(worldId.toString()).path(String.valueOf(rx)).path(String.valueOf(rz))
                .path(String.valueOf(cx)).path(String.valueOf(cz)).path(String.valueOf(sy))
                .path(String.valueOf(bx)).path(String.valueOf(by)).path(String.valueOf(bz))
                .request(ACCEPT)
                .put(Entity.entity(block, MediaType.APPLICATION_JSON), ChunkSection.class);
    }
}
