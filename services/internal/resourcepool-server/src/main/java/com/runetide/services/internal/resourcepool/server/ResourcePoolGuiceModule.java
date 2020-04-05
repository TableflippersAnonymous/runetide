package com.runetide.services.internal.resourcepool.server;

import com.datastax.oss.driver.api.core.CqlSession;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.runetide.services.internal.resourcepool.server.dao.ResourcePoolDao;

public class ResourcePoolGuiceModule implements Module {
    @Override
    public void configure(Binder binder) {

    }

    @Provides @Singleton
    public ResourcePoolMapper getResourcePoolMapper(final CqlSession cqlSession) {
        return new ResourcePoolMapperBuilder(cqlSession).build();
    }

    @Provides @Singleton
    public ResourcePoolDao getResourcePoolDao(final ResourcePoolMapper resourcePoolMapper) {
        return resourcePoolMapper.dao();
    }
}
