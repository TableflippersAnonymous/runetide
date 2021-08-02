package com.runetide.services.internal.region.server.services;

import com.runetide.common.Constants;
import com.runetide.common.dto.ChunkDataRef;
import com.runetide.common.dto.RegionRef;
import com.runetide.common.services.blobstore.BlobStore;
import com.runetide.common.util.Compressor;
import com.runetide.services.internal.region.server.dao.RegionDao;
import com.runetide.services.internal.region.server.domain.LoadedChunk;
import com.runetide.services.internal.region.server.domain.LoadedRegion;
import com.runetide.services.internal.region.server.dto.Region;
import com.runetide.services.internal.region.server.dto.RegionChunkJournalEntry;
import com.runetide.services.internal.region.server.dto.RegionData;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

@Singleton
public class RegionLoader {
    private final Compressor compressor;
    private final BlobStore blobStore;
    private final RegionDao daoRegion;
    private final Journaler journaler;

    @Inject
    public RegionLoader(final Compressor compressor, final BlobStore blobStore, final RegionDao daoRegion, final Journaler journaler) {
        this.compressor = compressor;
        this.blobStore = blobStore;
        this.daoRegion = daoRegion;
        this.journaler = journaler;
    }

    public LoadedRegion load(final RegionRef regionRef) throws IOException {
        final Region region = daoRegion.getRegion(regionRef);
        if(region == null) {
            /* FIXME: Call the WorldGenerator service */
            return null;
        }
        RegionData regionData;

        try(final DataInputStream dataInputStream = new DataInputStream(blobStore.get(
                Constants.REGION_BLOBSTORE_NAMESPACE, region.getChunkDataId().toString()))) {
            regionData = RegionData.decode(dataInputStream);
        }
        final LoadedRegion loadedRegion = new LoadedRegion(compressor, region, regionData);
        for(final RegionChunkJournalEntry entry : journaler.replay(regionData.toRef())) {
            final LoadedChunk chunk = loadedRegion.getChunk(entry.getX() / Constants.BLOCKS_PER_CHUNK_SECTION_X,
                    entry.getZ() / Constants.BLOCKS_PER_CHUNK_SECTION_Z);
            chunk.setBlock(
                    entry.getX() % Constants.BLOCKS_PER_CHUNK_SECTION_X,
                    entry.getY(),
                    entry.getZ() % Constants.BLOCKS_PER_CHUNK_SECTION_Z,
                    entry.getBlock()
            );
        }
        return loadedRegion;
    }

    public void save(final LoadedRegion loadedRegion) throws IOException {
        final ChunkDataRef newChunkDataRef = ChunkDataRef.random();
        final ChunkDataRef oldChunkDataRef = loadedRegion.getChunkDataRef();
        journaler.beginReplication(oldChunkDataRef, newChunkDataRef);
        try {
            final byte[][] compressedChunks = loadedRegion.quiesce();
            final RegionData regionData = new RegionData(RegionData.CURRENT_VERSION, newChunkDataRef,
                    loadedRegion.getRegionRef(), new Date().getTime(), compressedChunks);
            try (final DataOutputStream dataOutputStream = new DataOutputStream(blobStore.put(
                    Constants.REGION_BLOBSTORE_NAMESPACE, regionData.getId().toString()))) {
                regionData.encode(dataOutputStream);
            }

            final Region region = daoRegion.getRegion(loadedRegion.getRegionRef());
            if (region == null)
                throw new RuntimeException("Could not find loaded region {}" + loadedRegion.getRegionRef());

            region.setChunkDataId(newChunkDataRef);
            loadedRegion.setChunkDataId(newChunkDataRef);
            daoRegion.save(region);
            journaler.delete(oldChunkDataRef);
        } finally {
            journaler.endReplication(oldChunkDataRef, newChunkDataRef);
        }
    }
}
