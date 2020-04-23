package com.runetide.common;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfig;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import com.datastax.oss.driver.api.core.context.DriverContext;
import com.datastax.oss.driver.api.core.type.codec.TypeCodec;
import com.datastax.oss.driver.api.core.type.codec.registry.CodecRegistry;
import com.datastax.oss.driver.internal.core.auth.ProgrammaticPlainTextAuthProvider;
import com.datastax.oss.driver.internal.core.loadbalancing.DefaultLoadBalancingPolicy;
import com.datastax.oss.driver.internal.core.specex.ConstantSpeculativeExecutionPolicy;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.runetide.common.services.blobstore.BlobStore;
import com.runetide.common.services.blobstore.FSBlobStore;
import com.runetide.common.services.servicediscovery.ServiceData;
import com.runetide.common.util.Compressor;
import com.runetide.common.util.XZCompressor;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.BoundedExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceType;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class GuiceModule<T extends ServiceConfiguration> extends AbstractModule {
    private final Service<T> service;

    public GuiceModule(final Service<T> service) {
        this.service = service;
    }

    @Override
    protected void configure() {
        bind(TopicManager.class).to(TopicManagerImpl.class);
        bind(LockManager.class).to(LockManagerImpl.class);
        bind(Compressor.class).to(XZCompressor.class);
    }

    @Provides
    public Config getRedissonConfig(final ServiceConfiguration configuration) {
        return configuration.getRedissonConfig();
    }

    @Provides @Singleton
    public RedissonClient getRedisson(final Config config) {
        return Redisson.create(config);
    }

    @Provides
    public CassandraConfig getCassandraConfig(final ServiceConfiguration configuration) {
        return configuration.getCassandraConfig();
    }

    @Provides
    public Service getService() {
        return service;
    }

    @Provides @Singleton
    public CuratorFramework getCurator(final ServiceConfiguration configuration) {
        final CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(configuration.getZookeeperConnectionString(),
                new BoundedExponentialBackoffRetry(50, 1000, 29));
        curatorFramework.start();
        return curatorFramework;
    }

    @Provides @Singleton
    public ServiceInstance<ServiceData> getServiceInstance() throws Exception {
        return ServiceInstance.<ServiceData>builder()
                .serviceType(ServiceType.DYNAMIC)
                .payload(new ServiceData(0))
                .name(service.getName())
                .id(UUID.randomUUID().toString())
                .build();
    }

    @Provides @Singleton
    public ServiceDiscovery<ServiceData> getServiceDiscovery(final CuratorFramework curatorFramework,
                                                             final ServiceInstance<ServiceData> instance) throws Exception {
        final ServiceDiscovery<ServiceData> serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceData.class)
                .basePath(Constants.ZK_SERVICES)
                .client(curatorFramework)
                .thisInstance(instance)
                .serializer(new JsonInstanceSerializer<>(ServiceData.class))
                .watchInstances(true)
                .build();
        serviceDiscovery.start();
        return serviceDiscovery;
    }

    @Provides @Singleton
    public CqlSession getCassandraSession(final CassandraConfig cassandraConfig) {
        return CqlSession.builder() /* Yay, options! */
                .addContactPoints(cassandraConfig.getContactPoints().stream().map(s -> new InetSocketAddress(s, cassandraConfig.getPort())).collect(Collectors.toList()))
                .withAuthProvider(cassandraConfig.isAuthenticated() ? new ProgrammaticPlainTextAuthProvider(cassandraConfig.getUsername(), cassandraConfig.getPassword()) : null)
                .withConfigLoader(DriverConfigLoader.programmaticBuilder()
                        .withString(DefaultDriverOption.LOAD_BALANCING_POLICY_CLASS, "DcInferringLoadBalancingPolicy")
                        .withString(DefaultDriverOption.REQUEST_CONSISTENCY, cassandraConfig.getConsistencyLevel())
                        .withString(DefaultDriverOption.REQUEST_SERIAL_CONSISTENCY, cassandraConfig.getSerialConsistencyLevel())
                        .withInt(DefaultDriverOption.REQUEST_PAGE_SIZE, cassandraConfig.getFetchSize())
                        .withString(DefaultDriverOption.RECONNECTION_POLICY_CLASS, "ExponentialReconnectionPolicy")
                        .withDuration(DefaultDriverOption.RECONNECTION_BASE_DELAY, Duration.ofMillis(cassandraConfig.getReconnectBaseDelayMs()))
                        .withDuration(DefaultDriverOption.RECONNECTION_MAX_DELAY, Duration.ofMillis(cassandraConfig.getReconnectMaxDelayMs()))
                        .withDuration(DefaultDriverOption.CONTROL_CONNECTION_TIMEOUT, Duration.ofMillis(cassandraConfig.getConnectTimeoutMs()))
                        .withBoolean(DefaultDriverOption.SOCKET_KEEP_ALIVE, cassandraConfig.isTcpKeepAlive())
                        .build())
                .withKeyspace(cassandraConfig.getKeyspace())
                .addTypeCodecs(service.getCqlTypeCodecs().toArray(new TypeCodec[] {}))
                .build();
    }

    @Provides @Singleton
    public BlobStore getBlobStore(final ServiceConfiguration configuration) {
        //TODO: FIXME
        return new FSBlobStore(".");
    }

    @Provides @Singleton
    public ScheduledExecutorService getExecutorService() {
        return Executors.newScheduledThreadPool(5);
    }
}

