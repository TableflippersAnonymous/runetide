package com.runetide.services.internal.region.server;

import com.runetide.common.Service;

public class RegionService extends Service<RegionConfiguration> {
    public static void main(final String[] args) throws Exception {
        new RegionService().run(args);
    }
}
