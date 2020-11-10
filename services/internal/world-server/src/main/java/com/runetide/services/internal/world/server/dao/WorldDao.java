package com.runetide.services.internal.world.server.dao;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Select;
import com.runetide.common.dto.WorldRef;
import com.runetide.services.internal.world.common.World;

import java.util.UUID;

@Dao
public interface WorldDao {
    @Select
    World getWorld(UUID worldId);

    default World getWorld(final WorldRef worldRef) {
        return getWorld(worldRef.getId());
    }
}
