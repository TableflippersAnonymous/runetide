package com.runetide.services.internal.time.server;

import com.runetide.common.Service;
import com.runetide.services.internal.time.server.services.ClockTickService;
import io.dropwizard.setup.Environment;

public class TimeService extends Service<TimeConfiguration> {
    public static void main(final String[] args) throws Exception {
        new TimeService().run(args);
    }

    @Override
    public void run(TimeConfiguration configuration, Environment environment) throws Exception {
        super.run(configuration, environment);
        guiceBundle.getInjector().getInstance(ClockTickService.class).start();
    }
}
