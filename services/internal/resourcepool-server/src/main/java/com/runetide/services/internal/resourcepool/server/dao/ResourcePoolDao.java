package com.runetide.services.internal.resourcepool.server.dao;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Select;
import com.runetide.common.services.cql.BaseDao;
import com.runetide.services.internal.resourcepool.common.ResourcePoolRef;
import com.runetide.services.internal.resourcepool.common.ResourcePool;

import java.util.UUID;

@Dao
public interface ResourcePoolDao extends BaseDao<ResourcePool> {
    @Select
    ResourcePool get(UUID id);

    default ResourcePool get(ResourcePoolRef ref) {
        return get(ref.getId());
    }
}
