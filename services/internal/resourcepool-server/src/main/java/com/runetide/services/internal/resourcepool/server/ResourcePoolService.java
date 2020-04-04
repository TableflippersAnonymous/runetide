package com.runetide.services.internal.resourcepool.server;

import com.runetide.common.Constants;
import com.runetide.common.Service;

public class ResourcePoolService extends Service<ResourcePoolConfiguration> {
    public static void main(final String[] args) throws Exception {
        new ResourcePoolService().run(args);
    }

    private ResourcePoolService() {
        super(Constants.RESOURCE_POOL_SERVICE_NAME);
    }
}
