package com.runetide.services.internal.region.server.services;

import com.runetide.common.Constants;
import com.runetide.common.dto.RegionRef;
import com.runetide.common.services.blobstore.BlobStore;
import com.runetide.common.util.Compressor;
import com.runetide.services.internal.region.server.dao.DAORegion;
import com.runetide.services.internal.region.server.domain.LoadedRegion;
import com.runetide.services.internal.region.server.dto.Region;
import com.runetide.services.internal.region.server.dto.RegionData;

import java.io.DataInputStream;
import java.io.IOException;

public class RegionLoader {
    private final Compressor compressor;
    private final BlobStore blobStore;
    private final DAORegion daoRegion;

    public LoadedRegion load(final RegionRef regionRef) throws IOException {
        final Region region = daoRegion.getRegion(regionRef);
        if(region == null) {
            /* FIXME: Call the WorldGenerator service */
            return null;
        }
        LoadedRegion loadedRegion;
        try(final DataInputStream dataInputStream = new DataInputStream(blobStore.get(
                Constants.REGION_BLOBSTORE_NAMESPACE, region.getChunkDataId().toString()))) {
            final RegionData regionData = RegionData.decode(dataInputStream);
            loadedRegion = new LoadedRegion(compressor, region, regionData);
        }
        /* FIXME: Apply journal */
        return loadedRegion;
    }
}
