package com.runetide.services.internal.region.server;

import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;
import com.runetide.services.internal.region.server.dao.RegionDao;

@Mapper
public interface RegionMapper {
    @DaoFactory
    RegionDao dao();
}
