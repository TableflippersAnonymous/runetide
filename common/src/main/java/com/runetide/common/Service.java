package com.runetide.common;

import com.hubspot.dropwizard.guice.GuiceBundle;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public abstract class Service<T extends Configuration> extends Application<T> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    protected GuiceBundle<T> guiceBundle;

    @Override
    public void initialize(final Bootstrap<T> bootstrap) {
        super.initialize(bootstrap);
        guiceBundle = GuiceBundle.<ApiConfiguration>newBuilder()
                .addModule(new ApiGuiceModule(module))
                .enableAutoConfig(getClass().getPackage().getName())
                .setConfigClass(ApiConfiguration.class)
                .build();
        bootstrap.addBundle(guiceBundle);
    }

    @Override
    public void run(final T t, final Environment environment) throws Exception {

    }
}
