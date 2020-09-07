package com.runetide.services.internal.item.server;

import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;
import com.runetide.services.internal.item.server.dao.ItemDao;

@Mapper
public interface ItemMapper {
    @DaoFactory
    ItemDao dao();
}
