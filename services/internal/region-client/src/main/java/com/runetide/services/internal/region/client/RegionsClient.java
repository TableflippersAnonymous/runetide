package com.runetide.services.internal.region.client;

import com.runetide.common.*;
import com.runetide.common.dto.*;
import com.runetide.common.services.servicediscovery.ServiceData;
import com.runetide.services.internal.region.common.*;
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
public class RegionsClient extends UniqueLoadingClient<RegionRef> {
    @Inject
    public RegionsClient(final ServiceRegistry serviceRegistry, final TopicManager topicManager) {
        super(serviceRegistry, topicManager, Constants.REGION_LOADING_NAMESPACE, "regions");
    }

    public Response loadRegion(final LoadRegionRequest loadRegionRequest) {
        return getTarget()
                .request(ACCEPT)
                .post(Entity.entity(loadRegionRequest, MediaType.APPLICATION_JSON), Response.class);
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

    public TopicListenerHandle<RegionLoadMessage> listenLoad(final RegionRef regionRef,
                                                             final TopicListener<RegionLoadMessage> listener) {
        return topicManager.addListener(Constants.REGION_TOPIC_PREFIX + regionRef + ":load", listener,
                RegionLoadMessage.class);
    }

    public TopicListenerHandle<BlockUpdateMessage> listenUpdate(final RegionRef regionRef,
                                                                final TopicListener<BlockUpdateMessage> listener) {
        return topicManager.addListener(Constants.REGION_TOPIC_PREFIX + regionRef + ":blockupdate", listener,
                BlockUpdateMessage.class);
    }

    @Override
    protected WebTarget getTarget(final RegionRef regionRef) {
        return super.getTarget(regionRef)
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
