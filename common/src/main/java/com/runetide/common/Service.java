package com.runetide.common;

import com.datastax.oss.driver.api.core.type.codec.TypeCodec;
import com.hubspot.dropwizard.guice.GuiceBundle;
import com.runetide.common.domain.*;
import com.runetide.common.dto.*;
import com.runetide.common.services.cql.EnumIndexedCodec;
import com.runetide.common.services.cql.EnumNameCodec;
import com.runetide.common.services.cql.UUIDRefCodec;
import com.runetide.common.util.MigrationManager;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;

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
        guiceBundle.getInjector()
                .getInstance(MigrationManager.class)
                .runMigrations();
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

    protected List<TypeCodec<?>> getCqlTypeCodecs() {
        return Arrays.asList(
                new EnumIndexedCodec<>(BiomeType.class),
                new EnumIndexedCodec<>(BlockType.class),
                new EnumNameCodec<>(CraftingRecipe.class),
                new EnumIndexedCodec<>(DamageType.class),
                new EnumIndexedCodec<>(EntityType.class),
                new EnumIndexedCodec<>(EquipmentType.class),
                new EnumIndexedCodec<>(ItemType.class),
                new EnumIndexedCodec<>(RuneType.class),
                new UUIDRefCodec<>(ChunkDataRef.class, ChunkDataRef::new),
                new UUIDRefCodec<>(DungeonRef.class, DungeonRef::new),
                new UUIDRefCodec<>(InstanceRef.class, InstanceRef::new),
                new UUIDRefCodec<>(ItemRef.class, ItemRef::new),
                new UUIDRefCodec<>(SettlementRef.class, SettlementRef::new),
                new UUIDRefCodec<>(WorldRef.class, WorldRef::new)
        );
    }
}
