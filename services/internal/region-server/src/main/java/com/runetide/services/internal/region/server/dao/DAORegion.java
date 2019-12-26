package com.runetide.services.internal.region.server.dao;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.runetide.common.dto.RegionRef;
import com.runetide.services.internal.region.server.dto.Region;

public class DAORegion {
    private final Mapper<Region> mapper;

    public DAORegion(final MappingManager mappingManager) {
        mapper = mappingManager.mapper(Region.class);
    }

    public Region getRegion(final RegionRef regionRef) {
        return mapper.get(regionRef.getWorldRef().getId(), regionRef.getX(), regionRef.getZ());
    }

    public void save(final Region region) {
        mapper.save(region);
    }
}
