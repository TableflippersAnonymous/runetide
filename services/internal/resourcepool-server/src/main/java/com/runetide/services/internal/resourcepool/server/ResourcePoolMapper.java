package com.runetide.services.internal.resourcepool.server;

import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;
import com.runetide.services.internal.resourcepool.server.dao.ResourcePoolDao;

@Mapper
public interface ResourcePoolMapper {
    @DaoFactory
    ResourcePoolDao dao();
}
