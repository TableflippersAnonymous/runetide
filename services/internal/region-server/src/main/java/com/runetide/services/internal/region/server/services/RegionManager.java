package com.runetide.services.internal.region.server.services;

import com.runetide.common.dto.RegionRef;
import com.runetide.services.internal.region.server.domain.LoadedRegion;

import javax.inject.Singleton;
import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutorService;

@Singleton
public class RegionManager {
    private final ExecutorService executorService;
    private final RegionLoader regionLoader;
    private final Map<RegionRef, LoadedRegion> loadedRegions;

    public URI queueLoad(final RegionRef region) {
        executorService.submit(()->load(region));
        return null;
    }

    public Collection<LoadedRegion> getLoadedRegions() {
        return loadedRegions.values();
    }

    public LoadedRegion getLoadedRegion(final RegionRef regionRef) {
        return loadedRegions.get(regionRef);
    }

    private void load(final RegionRef region) {
        final LoadedRegion loadedRegion = regionLoader.load(region);
        loadedRegions.put(region, loadedRegion);
    }
}
