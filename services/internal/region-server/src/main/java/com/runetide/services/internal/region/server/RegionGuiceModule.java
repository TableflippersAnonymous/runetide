package com.runetide.services.internal.region.server;

import com.datastax.oss.driver.api.core.CqlSession;
import com.google.inject.Binder;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.runetide.services.internal.region.server.dao.RegionDao;

public class RegionGuiceModule implements com.google.inject.Module {
    @Override
    public void configure(Binder binder) {

    }

    @Provides @Singleton
    public RegionMapper getRegionMapper(final CqlSession cqlSession) {
        return new RegionMapperBuilder(cqlSession).build();
    }

    @Provides @Singleton
    public RegionDao getRegionDao(final RegionMapper regionMapper) {
        return regionMapper.dao();
    }
}
