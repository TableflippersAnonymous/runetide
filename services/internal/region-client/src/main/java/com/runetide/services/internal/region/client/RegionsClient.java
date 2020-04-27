package com.runetide.services.internal.region.client;

import com.runetide.common.*;
import com.runetide.common.dto.*;
import com.runetide.services.internal.region.common.*;
import org.apache.curator.framework.CuratorFramework;
import org.redisson.api.RedissonClient;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

@Singleton
public class RegionsClient extends UniqueLoadingClient<RegionRef> {
    @Inject
    public RegionsClient(final ServiceRegistry serviceRegistry, final TopicManager topicManager,
                         final CuratorFramework curatorFramework, final RedissonClient redissonClient) {
        super(serviceRegistry, topicManager, Constants.REGION_LOADING_NAMESPACE, "regions", curatorFramework,
                redissonClient);
    }

    public Region getRegion(final LoadingToken<RegionRef> loadingToken) {
        return getTarget(loadingToken)
                .request(ACCEPT)
                .get(Region.class);
    }

    public RegionChunkData bulkUpdate(final LoadingToken<RegionRef> loadingToken,
                                      final BulkBlockUpdateRequest bulkBlockUpdateRequest) {
        return getTarget(loadingToken)
                .request(ACCEPT)
                .post(Entity.entity(bulkBlockUpdateRequest, MediaType.APPLICATION_JSON), RegionChunkData.class);
    }

    public Chunk getChunk(final LoadingToken<RegionRef> loadingToken) {
        return getTarget(loadingToken)
                .request(ACCEPT)
                .get(Chunk.class);
    }

    public ChunkSection getChunkSection(final LoadingToken<RegionRef> loadingToken,
                                        final ChunkSectionRef chunkSectionRef) {
        return getTarget(loadingToken, chunkSectionRef)
                .request(ACCEPT)
                .get(ChunkSection.class);
    }

    public ChunkSection putBlock(final LoadingToken<RegionRef> loadingToken, final BlockRef blockRef,
                                 final Block block) {
        return getTarget(loadingToken, blockRef)
                .request(ACCEPT)
                .put(Entity.entity(block, MediaType.APPLICATION_JSON), ChunkSection.class);
    }

    public TopicListenerHandle<BlockUpdateMessage> listenUpdate(final RegionRef regionRef,
                                                                final TopicListener<BlockUpdateMessage> listener) {
        return topicManager.addListener(Constants.REGION_TOPIC_PREFIX + regionRef + ":blockupdate", listener,
                BlockUpdateMessage.class);
    }

    @Override
    protected WebTarget getTarget(final LoadingToken<RegionRef> regionRef) {
        return super.getTarget(regionRef)
                .path(regionRef.getKey().getWorldRef().toString())
                .path(String.valueOf(regionRef.getKey().getX())).path(String.valueOf(regionRef.getKey().getZ()));
    }

    private WebTarget getTarget(final LoadingToken<RegionRef> loadingToken, final ChunkRef chunkRef) {
        if(!loadingToken.getKey().equals(chunkRef.getRegionRef()))
            throw new IllegalArgumentException("Invalid loading token");
        return getTarget(loadingToken)
                .path(String.valueOf(chunkRef.getX())).path(String.valueOf(chunkRef.getZ()));
    }

    private WebTarget getTarget(final LoadingToken<RegionRef> loadingToken, final ChunkSectionRef chunkSectionRef) {
        return getTarget(loadingToken, chunkSectionRef.getChunkRef())
                .path(String.valueOf(chunkSectionRef.getY()));
    }

    private WebTarget getTarget(final LoadingToken<RegionRef> loadingToken, final BlockRef blockRef) {
        return getTarget(loadingToken, blockRef.getChunkSectionRef())
                .path(String.valueOf(blockRef.getX()))
                .path(String.valueOf(blockRef.getY()))
                .path(String.valueOf(blockRef.getZ()));
    }
}
