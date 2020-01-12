package com.runetide.services.internal.item.server.resources;

import com.runetide.common.dto.ItemRef;
import com.runetide.services.internal.item.common.Item;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemsResource {
    @POST
    public Item createItem(final Item item) {

    }

    @Path("/{id}")
    @GET
    public Item getItem(@PathParam("id") final ItemRef itemRef) {

    }

    @Path("/{id}")
    @PUT
    public Item updateItem(@PathParam("id") final ItemRef itemRef) {

    }

    @Path("/{id}")
    @DELETE
    public Response deleteItem(@PathParam("id") final ItemRef itemRef) {

    }
}
