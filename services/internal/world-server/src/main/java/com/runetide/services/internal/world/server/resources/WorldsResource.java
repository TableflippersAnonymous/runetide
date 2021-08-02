package com.runetide.services.internal.world.server.resources;

import com.runetide.common.dto.WorldRef;
import com.runetide.services.internal.world.common.World;
import com.runetide.services.internal.world.server.services.WorldManager;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/worlds")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WorldsResource {
    private final WorldManager manager;

    @Inject
    public WorldsResource(final WorldManager manager) {
        this.manager = manager;
    }

    @GET
    @Path("/{worldId: " + WorldRef.PATH_REGEX + "}")
    public World getWorld(final WorldRef worldRef) {
        return manager.getWorld(worldRef);
    }
}
