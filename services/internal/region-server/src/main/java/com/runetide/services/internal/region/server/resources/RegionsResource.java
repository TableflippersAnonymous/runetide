package com.runetide.services.internal.region.server.resources;

import com.google.common.collect.ImmutableList;
import com.runetide.common.dto.RegionRef;
import com.runetide.common.dto.WorldRef;
import com.runetide.services.internal.region.common.Block;
import com.runetide.services.internal.region.common.BulkBlockUpdateEntry;
import com.runetide.services.internal.region.common.BulkBlockUpdateRequest;
import com.runetide.services.internal.region.common.Chunk;
import com.runetide.services.internal.region.common.ChunkSection;
import com.runetide.services.internal.region.common.LoadRegionRequest;
import com.runetide.services.internal.region.common.Region;
import com.runetide.services.internal.region.common.RegionChunkData;
import com.runetide.services.internal.region.server.domain.LoadedRegion;
import com.runetide.services.internal.region.server.services.RegionManager;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Path("/regions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RegionsResource {
    private final RegionManager regionManager;

    @Inject
    public RegionsResource(final RegionManager regionManager) {
        this.regionManager = regionManager;
    }

    @POST
    public Response loadRegion(final LoadRegionRequest loadRegionRequest) throws Exception {
        if(regionManager.requestLoad(loadRegionRequest.getRegion()))
            return Response.noContent().build();
        return Response.seeOther(regionManager.getUri(loadRegionRequest.getRegion())).build();
    }

    @GET
    public List<Region> getRegions() {
        return regionManager.getLoadedRegions().stream().map(LoadedRegion::toClientRegion).collect(Collectors.toList());
    }

    @Path("{worldId}/{rx}/{rz}")
    @GET
    public Region getRegion(@PathParam("worldId") final WorldRef worldId, @PathParam("rx") final long rx,
                            @PathParam("rz") final long rz) {
        return regionManager.getLoadedRegion(new RegionRef(worldId, rx, rz)).toClientRegion();
    }

    @Path("{worldId}/{rx}/{rz}")
    @POST
    public RegionChunkData bulkUpdate(@PathParam("worldId") final WorldRef worldId, @PathParam("rx") final long rx,
                                      @PathParam("rz") final long rz,
                                      final BulkBlockUpdateRequest bulkBlockUpdateRequest) {
        final RegionRef regionRef = new RegionRef(worldId, rx, rz);
        regionManager.update(regionRef, bulkBlockUpdateRequest);
        return regionManager.getRegionData(regionRef);
    }

    @Path("{worldId}/{rx}/{rz}")
    @DELETE
    public Response unloadRegion(@PathParam("worldId") final WorldRef worldId, @PathParam("rx") final long rx,
                                 @PathParam("rz") final long rz) {
        final RegionRef regionRef = new RegionRef(worldId, rx, rz);
        if(regionManager.requestUnload(regionRef))
            return Response.noContent().build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @Path("{worldId}/{rx}/{rz}/{cx}/{cz}")
    @GET
    public Chunk getChunk(@PathParam("worldId") final WorldRef worldId, @PathParam("rx") final long rx,
                          @PathParam("rz") final long rz, @PathParam("cx") final int cx, @PathParam("cz") final int cz) {
        final RegionRef regionRef = new RegionRef(worldId, rx, rz);
        return regionManager.getChunk(regionRef, cx, cz);
    }

    @Path("{worldId}/{rx}/{rz}/{cx}/{cz}/{sy}")
    @GET
    public ChunkSection getChunkSection(@PathParam("worldId") final WorldRef worldId, @PathParam("rx") final long rx,
                                        @PathParam("rz") final long rz, @PathParam("cx") final int cx,
                                        @PathParam("cz") final int cz, @PathParam("sy") final int sy) {
        final RegionRef regionRef = new RegionRef(worldId, rx, rz);
        return regionManager.getChunkSection(regionRef, cx, cz, sy);
    }

    @Path("{worldId}/{rx}/{rz}/{cx}/{cz}/{sy}/{bx}/{by}/{bz}")
    @PUT
    public ChunkSection putBlock(@PathParam("worldId") final WorldRef worldId, @PathParam("rx") final long rx,
                                 @PathParam("rz") final long rz, @PathParam("cx") final int cx,
                                 @PathParam("cz") final int cz, @PathParam("sy") final int sy,
                                 @PathParam("bx") final int bx, @PathParam("by") final int by,
                                 @PathParam("bz") final int bz, final Block block) {
        final RegionRef regionRef = new RegionRef(worldId, rx, rz);
        final BulkBlockUpdateRequest bulkBlockUpdateRequest = new BulkBlockUpdateRequest(ImmutableList.of(
                new BulkBlockUpdateEntry(cx, cz, sy, bx, by, bz, block)));
        regionManager.update(regionRef, bulkBlockUpdateRequest);
        return regionManager.getChunkSection(regionRef, cx, cz, sy);
    }
}
