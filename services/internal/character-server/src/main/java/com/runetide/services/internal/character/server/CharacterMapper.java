package com.runetide.services.internal.character.server;

import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;
import com.runetide.services.internal.character.server.dao.CharacterDao;

@Mapper
public interface CharacterMapper {
    @DaoFactory
    CharacterDao dao();
}
