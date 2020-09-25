package com.runetide.services.internal.worldgen.server;

import com.hubspot.dropwizard.guice.GuiceBundle;
import com.runetide.common.Constants;
import com.runetide.common.Service;

public class WorldGenService extends Service<WorldGenConfiguration> {
    public static void main(final String[] args) throws Exception {
        new WorldGenService().run(args);
    }

    private WorldGenService() {
        super(Constants.WORLDGEN_SERVICE_NAME);
    }

    @Override
    protected GuiceBundle.Builder<WorldGenConfiguration> addGuiceModules(GuiceBundle.Builder<WorldGenConfiguration> builder) {
        return super.addGuiceModules(builder)
                .addModule(new WorldGenGuiceModule());
    }
}
