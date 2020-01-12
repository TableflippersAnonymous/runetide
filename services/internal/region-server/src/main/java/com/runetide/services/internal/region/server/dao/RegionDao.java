package com.runetide.services.internal.region.server.dao;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Select;
import com.runetide.common.BaseDao;
import com.runetide.common.dto.RegionRef;
import com.runetide.services.internal.region.server.dto.Region;

import java.util.UUID;

@Dao
public interface RegionDao extends BaseDao<Region> {
    @Select
    Region getRegion(UUID worldId, long x, long z);

    default Region getRegion(final RegionRef regionRef) {
        return getRegion(regionRef.getWorldRef().getId(), regionRef.getX(), regionRef.getZ());
    }
}
