package com.runetide.services.internal.world.server;

import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;
import com.runetide.services.internal.world.server.dao.WorldDao;

@Mapper
public interface WorldMapper {
    @DaoFactory
    WorldDao dao();
}
