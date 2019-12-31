package com.runetide.services.internal.loader.server.resources;

import com.runetide.common.dto.RegionRef;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/loader")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoaderResource {
    @Path("regions")
    @GET
    public List<RegionRef> getLoadedRegions() {}


}
