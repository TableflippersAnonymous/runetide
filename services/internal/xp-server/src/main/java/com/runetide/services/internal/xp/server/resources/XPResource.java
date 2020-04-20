package com.runetide.services.internal.xp.server.resources;

import com.google.inject.Inject;
import com.runetide.services.internal.xp.common.XP;
import com.runetide.services.internal.xp.common.XPRef;
import com.runetide.services.internal.xp.common.XPTransactRequest;
import com.runetide.services.internal.xp.common.XPTransactResponse;
import com.runetide.services.internal.xp.server.domain.LoadedXP;
import com.runetide.services.internal.xp.server.services.XPManager;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/xp")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class XPResource {
    private final XPManager xpManager;

    @Inject
    public XPResource(XPManager xpManager) {
        this.xpManager = xpManager;
    }

    @GET
    public Collection<XP> listLoadedXp() {
        return xpManager.getLoadedXPs().stream()
                .map(LoadedXP::getXp)
                .collect(Collectors.toList());
    }

    @POST
    public XPRef createXP(XP xp) {
        if(xp.getId() != null)
            throw new BadRequestException();
        if(xp.getParent() != null && !xpManager.isValid(xp.getParent()))
            throw new NotFoundException();
        return xpManager.create(xp);
    }

    @Path("{id}")
    @GET
    public XP getXp(@PathParam("id") XPRef xpRef) {
        final XP xp = Optional.ofNullable(xpManager.getLoadedXP(xpRef))
                .map(LoadedXP::getXp)
                .orElse(null);
        if(xp == null)
            throw new NotFoundException();
        return xp;
    }

    @Path("{id}")
    @POST
    public XPTransactResponse transact(@PathParam("id") XPRef xpRef, XPTransactRequest request) {
        final LoadedXP loadedXP = xpManager.getLoadedXP(xpRef);
        if(loadedXP == null)
            throw new NotFoundException();
        final long oldValue = loadedXP.getXp().getXp();
        final int oldLevel = loadedXP.getXp().getLevel();
        final boolean success = loadedXP.transact(request.getDelta());
        return new XPTransactResponse(
                success,
                loadedXP.getXp().getId(),
                oldValue,
                loadedXP.getXp().getXp(),
                oldLevel,
                loadedXP.getXp().getLevel()
        );
    }

    @Path("{id}")
    @DELETE
    public Response delete(@PathParam("id") XPRef xpRef) {
        xpManager.delete(xpRef);
        return Response.noContent().build();
    }
}
