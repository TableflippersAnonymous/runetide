package com.runetide.common;

import com.hubspot.dropwizard.guice.GuiceBundle;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public abstract class Service<T extends ServiceConfiguration> extends Application<T> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    protected GuiceBundle<T> guiceBundle;
    protected final String serviceName;

    protected Service(final String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public void initialize(final Bootstrap<T> bootstrap) {
        super.initialize(bootstrap);
        guiceBundle = addGuiceModules(GuiceBundle.<T>newBuilder())
                .enableAutoConfig("com.runetide")
                .setConfigClass(getConfigurationClass())
                .build();
        bootstrap.addBundle(guiceBundle);
    }

    protected GuiceBundle.Builder<T> addGuiceModules(GuiceBundle.Builder<T> builder) {
        return builder.addModule(new GuiceModule<>(this));
    }

    @Override
    public void run(final T configuration, final Environment environment) throws Exception {
        final ServiceRegistry serviceRegistry = guiceBundle.getInjector().getInstance(ServiceRegistry.class);
        serviceRegistry.register(serviceName);
    }
}
