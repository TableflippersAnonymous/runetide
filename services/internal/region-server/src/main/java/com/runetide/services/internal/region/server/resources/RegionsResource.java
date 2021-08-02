package com.runetide.services.internal.region.server.resources;

import com.google.common.collect.ImmutableList;
import com.runetide.common.dto.BlockRef;
import com.runetide.common.dto.ChunkRef;
import com.runetide.common.dto.ChunkSectionRef;
import com.runetide.common.dto.RegionRef;
import com.runetide.services.internal.region.common.Block;
import com.runetide.services.internal.region.common.BulkBlockUpdateEntry;
import com.runetide.services.internal.region.common.BulkBlockUpdateRequest;
import com.runetide.services.internal.region.common.Chunk;
import com.runetide.services.internal.region.common.ChunkSection;
import com.runetide.services.internal.region.common.Region;
import com.runetide.services.internal.region.common.RegionChunkData;
import com.runetide.services.internal.region.server.domain.LoadedRegion;
import com.runetide.services.internal.region.server.services.RegionManager;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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

    @GET
    public List<Region> getRegions() {
        return regionManager.getLoadedRegions().stream().map(LoadedRegion::toClientRegion).collect(Collectors.toList());
    }

    @Path("{regionRef: " + RegionRef.PATH_REGEX + "}")
    @GET
    public Region getRegion(@PathParam("regionRef") final RegionRef regionRef) {
        final LoadedRegion region = regionManager.getLoadedRegion(regionRef);
        if(region == null)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        return region.toClientRegion();
    }

    @Path("{regionRef: " + RegionRef.PATH_REGEX + "}")
    @POST
    public RegionChunkData bulkUpdate(@PathParam("regionRef") final RegionRef regionRef,
                                      final BulkBlockUpdateRequest bulkBlockUpdateRequest) {
        if(regionManager.getLoadedRegion(regionRef) == null)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        regionManager.update(regionRef, bulkBlockUpdateRequest);
        return regionManager.getRegionData(regionRef);
    }

    @Path("{chunkRef: " + ChunkRef.PATH_REGEX + "}")
    @GET
    public Chunk getChunk(@PathParam("chunkRef") final ChunkRef chunkRef) {
        final RegionRef regionRef = chunkRef.getRegionRef();
        if(regionManager.getLoadedRegion(regionRef) == null)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        return regionManager.getChunk(chunkRef);
    }

    @Path("{chunkSectionRef: " + ChunkSectionRef.PATH_REGEX + "}")
    @GET
    public ChunkSection getChunkSection(@PathParam("chunkSectionRef") final ChunkSectionRef chunkSectionRef) {
        final RegionRef regionRef = chunkSectionRef.getRegionRef();
        if(regionManager.getLoadedRegion(regionRef) == null)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        return regionManager.getChunkSection(chunkSectionRef);
    }

    @Path("{blockRef: " + BlockRef.PATH_REGEX + "}")
    @PUT
    public ChunkSection putBlock(@PathParam("blockRef") final BlockRef blockRef, final Block block) {
        final RegionRef regionRef = blockRef.getRegionRef();
        if(regionManager.getLoadedRegion(regionRef) == null)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        final BulkBlockUpdateRequest bulkBlockUpdateRequest = new BulkBlockUpdateRequest(ImmutableList.of(
                new BulkBlockUpdateEntry(blockRef, block)));
        regionManager.update(regionRef, bulkBlockUpdateRequest);
        return regionManager.getChunkSection(blockRef.getChunkSectionRef());
    }
}
