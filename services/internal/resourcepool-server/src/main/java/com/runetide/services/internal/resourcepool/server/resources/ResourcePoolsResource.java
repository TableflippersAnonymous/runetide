package com.runetide.services.internal.resourcepool.server.resources;

import com.google.inject.Inject;
import com.runetide.common.dto.ResourcePoolRef;
import com.runetide.services.internal.resourcepool.common.ResourcePool;
import com.runetide.services.internal.resourcepool.common.ResourcePoolEffect;
import com.runetide.services.internal.resourcepool.common.ResourcePoolUpdateRequest;
import com.runetide.services.internal.resourcepool.common.ResourcePoolUpdateResponse;
import com.runetide.services.internal.resourcepool.server.domain.LoadedResourcePool;
import com.runetide.services.internal.resourcepool.server.services.ResourcePoolManager;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/resource-pools")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ResourcePoolsResource {
    private final ResourcePoolManager resourcePoolManager;

    @Inject
    public ResourcePoolsResource(ResourcePoolManager resourcePoolManager) {
        this.resourcePoolManager = resourcePoolManager;
    }

    @GET
    public List<ResourcePool> getResourcePools() {
        return resourcePoolManager.getLoadedResourcePools().stream()
                .map(LoadedResourcePool::getResourcePool)
                .collect(Collectors.toList());
    }

    @POST
    public ResourcePool createResourcePool(final ResourcePool resourcePool) {
        if(resourcePool.getId() != null)
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        resourcePoolManager.createResourcePool(resourcePool);
        return resourcePool;
    }

    @Path("{id}")
    @GET
    public ResourcePool getResourcePool(@PathParam("id") final ResourcePoolRef resourcePoolRef) {
        final LoadedResourcePool loadedResourcePool = resourcePoolManager.getLoadedResourcePool(resourcePoolRef);
        if(loadedResourcePool == null)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        return loadedResourcePool.getResourcePool();
    }

    @Path("{id}")
    @POST
    public ResourcePoolUpdateResponse updateResourcePool(@PathParam("id") final ResourcePoolRef resourcePoolRef,
                                                         final ResourcePoolUpdateRequest resourcePoolUpdateRequest) {
        final LoadedResourcePool loadedResourcePool = resourcePoolManager.getLoadedResourcePool(resourcePoolRef);
        if(loadedResourcePool == null)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        final boolean updated = loadedResourcePool.update(resourcePoolUpdateRequest.getDelta(),
                resourcePoolUpdateRequest.isIgnoreNormalLimits(), resourcePoolUpdateRequest.isTakePartial(),
                resourcePoolUpdateRequest.getOverrideMin(), resourcePoolUpdateRequest.getOverrideMax());
        return new ResourcePoolUpdateResponse(updated, loadedResourcePool.getResourcePool());
    }

    @Path("{id}")
    @PUT
    public ResourcePool updateResourcePool(@PathParam("id") final ResourcePoolRef resourcePoolRef,
                                           final ResourcePool resourcePool) {
        final LoadedResourcePool loadedResourcePool = resourcePoolManager.getLoadedResourcePool(resourcePoolRef);
        if(loadedResourcePool == null)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        if(resourcePool.getId() != null && !resourcePool.getId().equals(loadedResourcePool.getResourcePool().getId()))
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        if(resourcePool.getType() != null && !resourcePool.getType().equals(loadedResourcePool.getResourcePool().getType()))
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        loadedResourcePool.getResourcePool().setValue(resourcePool.getValue());
        loadedResourcePool.getResourcePool().setNormalLimitLower(resourcePool.getNormalLimitLower());
        loadedResourcePool.getResourcePool().setNormalLimitUpper(resourcePool.getNormalLimitUpper());
        loadedResourcePool.getResourcePool().setFinalLimitLower(resourcePool.getFinalLimitLower());
        loadedResourcePool.getResourcePool().setFinalLimitUpper(resourcePool.getFinalLimitUpper());
        if(resourcePool.getEffects() != null)
            loadedResourcePool.getResourcePool().setEffects(resourcePool.getEffects());
        loadedResourcePool.save();
        return loadedResourcePool.getResourcePool();
    }

    @Path("{id}")
    @DELETE
    public Response deleteResourcePool(@PathParam("id") final ResourcePoolRef resourcePoolRef) {
        resourcePoolManager.deleteResourcePool(resourcePoolRef);
        return Response.noContent().build();
    }

    @Path("{id}/{effect}")
    @GET
    public ResourcePoolEffect getResourcePoolEffect(@PathParam("id") final ResourcePoolRef resourcePoolRef,
                                                    @PathParam("effect") final String effectName) {
        final LoadedResourcePool loadedResourcePool = resourcePoolManager.getLoadedResourcePool(resourcePoolRef);
        if(loadedResourcePool == null)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        final ResourcePoolEffect resourcePoolEffect = loadedResourcePool.getResourcePool().getEffects().get(effectName);
        if(resourcePoolEffect == null)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        return resourcePoolEffect;
    }

    @Path("{id}/{effect}")
    @PUT
    public ResourcePool upsertResourcePoolEffect(@PathParam("id") final ResourcePoolRef resourcePoolRef,
                                                 @PathParam("effect") final String effectName,
                                                 final ResourcePoolEffect effect) {
        final LoadedResourcePool loadedResourcePool = resourcePoolManager.getLoadedResourcePool(resourcePoolRef);
        if(loadedResourcePool == null)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        loadedResourcePool.addEffect(effectName, effect);
        return loadedResourcePool.getResourcePool();
    }
}
