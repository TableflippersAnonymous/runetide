package com.runetide.services.internal.loader.server;

import com.runetide.common.Constants;
import com.runetide.common.Service;
import com.runetide.services.internal.loader.server.services.LoaderCheckService;
import io.dropwizard.setup.Environment;


public class LoaderService extends Service<LoaderConfiguration> {
    public static void main(final String[] args) throws Exception {
        new LoaderService().run(args);
    }

    private LoaderService() {
        super(Constants.LOADER_SERVICE_NAME);
    }

    @Override
    public void run(final LoaderConfiguration configuration, final Environment environment) throws Exception {
        super.run(configuration, environment);
        guiceBundle.getInjector().getInstance(LoaderCheckService.class).run();
    }
}
