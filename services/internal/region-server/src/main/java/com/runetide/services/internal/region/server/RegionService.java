package com.runetide.services.internal.region.server;

import com.hubspot.dropwizard.guice.GuiceBundle;
import com.runetide.common.Constants;
import com.runetide.common.Service;
import io.dropwizard.setup.Bootstrap;

public class RegionService extends Service<RegionConfiguration> {
    public static void main(final String[] args) throws Exception {
        new RegionService().run(args);
    }

    private RegionService() {
        super(Constants.REGION_LOADING_NAMESPACE);
    }

    @Override
    protected GuiceBundle.Builder<RegionConfiguration> addGuiceModules(GuiceBundle.Builder<RegionConfiguration> builder) {
        return super.addGuiceModules(builder)
                .addModule(new RegionGuiceModule());
    }
}
