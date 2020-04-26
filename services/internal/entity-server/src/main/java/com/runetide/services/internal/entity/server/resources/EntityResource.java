package com.runetide.services.internal.entity.server.resources;

import com.runetide.services.internal.entity.common.dto.EntityRef;
import com.runetide.common.dto.PositionLookRef;
import com.runetide.services.internal.entity.common.dto.Entity;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/entities")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EntityResource {
    @POST
    public Entity loadEntity(final EntityRef entityRef) {

    }

    @GET
    public List<Entity> listEntities() {

    }

    @Path("/{id}")
    @PUT
    public Entity updateEntity(@PathParam("id") final EntityRef entityRef, final Entity entity) {

    }

    @Path("/{id}/move")
    @POST
    public Entity moveEntity(@PathParam("id") final EntityRef entityRef, final PositionLookRef position) {

    }

    @Path("/{id}/kill")
    @POST
    public Entity killEntity(@PathParam("id") final EntityRef entityRef) {

    }

    @Path("/{id}")
    @DELETE
    public Response unloadEntity(@PathParam("id") final EntityRef entityRef) {

    }
}
