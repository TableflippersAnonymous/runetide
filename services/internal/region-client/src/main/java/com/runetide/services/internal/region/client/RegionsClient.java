package com.runetide.services.internal.region.client;

import com.runetide.common.dto.*;
import com.runetide.common.services.servicediscovery.ServiceData;
import com.runetide.services.internal.region.common.Block;
import com.runetide.services.internal.region.common.BulkBlockUpdateRequest;
import com.runetide.services.internal.region.common.Chunk;
import com.runetide.services.internal.region.common.ChunkSection;
import com.runetide.services.internal.region.common.LoadRegionRequest;
import com.runetide.services.internal.region.common.Region;
import com.runetide.services.internal.region.common.RegionChunkData;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceProvider;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.invoke.MethodHandles;
import java.util.List;

@Singleton
public class RegionsClient {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String ACCEPT = MediaType.APPLICATION_JSON;
    private static final String URI_FORMAT = "http://%s";

    private final ServiceProvider<ServiceData> regionsService;
    private final Client client;

    @Inject
    public RegionsClient(final ServiceDiscovery<ServiceData> serviceDiscovery) throws Exception {
        regionsService = serviceDiscovery.serviceProviderBuilder()
                .providerStrategy(instanceProvider -> instanceProvider.getInstances().stream()
                        .min((o1, o2) -> o2.getPayload().getLoad() - o1.getPayload().getLoad()).orElse(null))
                .serviceName("RegionService")
                .build();
        regionsService.start();
        client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);
    }

    public Response loadRegion(final LoadRegionRequest loadRegionRequest) {
        return getTarget()
                .request(ACCEPT)
                .post(Entity.entity(loadRegionRequest, MediaType.APPLICATION_JSON), Response.class);
    }

    public List<Region> getRegions() {
        return getTarget()
                .request(ACCEPT)
                .get(new GenericType<List<Region>>() {});
    }

    public Region getRegion(final RegionRef regionRef) {
        return getTarget(regionRef)
                .request(ACCEPT)
                .get(Region.class);
    }

    public RegionChunkData bulkUpdate(final RegionRef regionRef, final BulkBlockUpdateRequest bulkBlockUpdateRequest) {
        return getTarget(regionRef)
                .request(ACCEPT)
                .post(Entity.entity(bulkBlockUpdateRequest, MediaType.APPLICATION_JSON), RegionChunkData.class);
    }

    public Response unloadRegion(final RegionRef regionRef) {
        return getTarget(regionRef)
                .request(ACCEPT)
                .delete(Response.class);
    }

    public Chunk getChunk(final ChunkRef chunkRef) {
        return getTarget(chunkRef)
                .request(ACCEPT)
                .get(Chunk.class);
    }

    public ChunkSection getChunkSection(final ChunkSectionRef chunkSectionRef) {
        return getTarget(chunkSectionRef)
                .request(ACCEPT)
                .get(ChunkSection.class);
    }

    public ChunkSection putBlock(final BlockRef blockRef, final Block block) {
        return getTarget(blockRef)
                .request(ACCEPT)
                .put(Entity.entity(block, MediaType.APPLICATION_JSON), ChunkSection.class);
    }

    private WebTarget getTarget(final String address) {
        final WebTarget webTarget = client.target(String.format(URI_FORMAT, address));
        return webTarget.path("regions");
    }

    private WebTarget getTarget() {
        try {
            final ServiceInstance<ServiceData> instance = regionsService.getInstance();
            return getTarget(instance.getAddress());
        } catch (final Exception e) {
            LOG.error("Unexpected exception getting RegionService instance.", e);
            throw new IllegalStateException(e);
        }
    }

    private WebTarget getTarget(final RegionRef regionRef) {
        final String address = null; //FIXME
        return getTarget(address)
                .path(regionRef.getWorldRef().toString())
                .path(String.valueOf(regionRef.getX())).path(String.valueOf(regionRef.getZ()));
    }

    private WebTarget getTarget(final ChunkRef chunkRef) {
        return getTarget(chunkRef.getRegionRef())
                .path(String.valueOf(chunkRef.getX())).path(String.valueOf(chunkRef.getZ()));
    }

    private WebTarget getTarget(final ChunkSectionRef chunkSectionRef) {
        return getTarget(chunkSectionRef.getChunkRef())
                .path(String.valueOf(chunkSectionRef.getY()));
    }

    private WebTarget getTarget(final BlockRef blockRef) {
        return getTarget(blockRef.getChunkSectionRef())
                .path(String.valueOf(blockRef.getX()))
                .path(String.valueOf(blockRef.getY()))
                .path(String.valueOf(blockRef.getZ()));
    }
}
