package com.runetide.services.internal.multiverse.server;

import com.runetide.common.Constants;
import com.runetide.common.Service;

public class MultiverseService extends Service<MultiverseConfiguration> {
    public static void main(final String[] args) throws Exception {
        new MultiverseService().run(args);
    }

    private MultiverseService() {
        super(Constants.MULTIVERSE_SERVICE_NAME);
    }
}
