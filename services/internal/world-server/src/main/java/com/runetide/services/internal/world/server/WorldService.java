package com.runetide.services.internal.world.server;

import com.hubspot.dropwizard.guice.GuiceBundle;
import com.runetide.common.Constants;
import com.runetide.common.Service;

public class WorldService extends Service<WorldConfiguration> {
    public static void main(final String[] args) throws Exception {
        new WorldService().run(args);
    }

    private WorldService() {
        super(Constants.WORLD_SERVICE_NAME);
    }

    @Override
    protected GuiceBundle.Builder<WorldConfiguration> addGuiceModules(GuiceBundle.Builder<WorldConfiguration> builder) {
        return super.addGuiceModules(builder)
                .addModule(new WorldGuiceModule());
    }
}
