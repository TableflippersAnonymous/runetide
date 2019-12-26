package com.runetide.common;

import com.datastax.driver.core.AuthProvider;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.CodecRegistry;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.PerHostPercentileTracker;
import com.datastax.driver.core.PlainTextAuthProvider;
import com.datastax.driver.core.QueryOptions;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SimpleStatement;
import com.datastax.driver.core.SocketOptions;
import com.datastax.driver.core.policies.DCAwareRoundRobinPolicy;
import com.datastax.driver.core.policies.ExponentialReconnectionPolicy;
import com.datastax.driver.core.policies.LatencyAwarePolicy;
import com.datastax.driver.core.policies.PercentileSpeculativeExecutionPolicy;
import com.datastax.driver.core.policies.TokenAwarePolicy;
import com.datastax.driver.mapping.MappingManager;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.runetide.common.services.servicediscovery.ServiceData;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.BoundedExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceProvider;
import org.apache.curator.x.discovery.ServiceType;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class GuiceModule<T extends ServiceConfiguration> extends AbstractModule {
    private final Service<T> service;

    public GuiceModule(final Service<T> service) {
        this.service = service;
    }

    @Override
    protected void configure() {
        bind(TopicManager.class).to(TopicManagerImpl.class);
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
                .basePath("/discovery")
                .client(curatorFramework)
                .thisInstance(instance)
                .serializer(new JsonInstanceSerializer<>(ServiceData.class))
                .watchInstances(true)
                .build();
        serviceDiscovery.start();
        return serviceDiscovery;
    }

    @Provides @Singleton
    @Named("region")
    public ServiceProvider<ServiceData> getRegionServiceProvider(final ServiceDiscovery<ServiceData> serviceDiscovery) throws Exception {
        final ServiceProvider<ServiceData> serviceProvider = serviceDiscovery.serviceProviderBuilder()
                .providerStrategy(instanceProvider -> instanceProvider.getInstances().stream()
                        .min((o1, o2) -> o2.getPayload().getLoad() - o1.getPayload().getLoad()).get())
                .serviceName("region")
                .build();
        serviceProvider.start();
    }

    @Provides @Singleton
    public Cluster getCassandraCluster(final CassandraConfig cassandraConfig) {
        return Cluster.builder() /* Yay, options! */
                .withClusterName(cassandraConfig.getClusterName())
                .addContactPoints(cassandraConfig.getContactPoints().toArray(new String[] {}))
                .withPort(cassandraConfig.getPort())
                .withAuthProvider(cassandraConfig.isAuthenticated() ? new PlainTextAuthProvider(cassandraConfig.getUsername(), cassandraConfig.getPassword()) : AuthProvider.NONE)
                .withLoadBalancingPolicy(new TokenAwarePolicy(
                        LatencyAwarePolicy.builder(
                                DCAwareRoundRobinPolicy.builder()
                                        .build()
                        )
                                .withExclusionThreshold(cassandraConfig.getExclusionThreshold())
                                .withScale(cassandraConfig.getScaleMs(), TimeUnit.MILLISECONDS)
                                .withRetryPeriod(cassandraConfig.getRetryMs(), TimeUnit.MILLISECONDS)
                                .withUpdateRate(cassandraConfig.getUpdateMs(), TimeUnit.MILLISECONDS)
                                .withMininumMeasurements(cassandraConfig.getMinimumMeasurements())
                                .build()
                ))
                .withQueryOptions(new QueryOptions()
                        .setConsistencyLevel(ConsistencyLevel.valueOf(cassandraConfig.getConsistencyLevel()))
                        .setSerialConsistencyLevel(ConsistencyLevel.valueOf(cassandraConfig.getSerialConsistencyLevel()))
                        .setFetchSize(cassandraConfig.getFetchSize())
                )
                .withReconnectionPolicy(new ExponentialReconnectionPolicy(cassandraConfig.getReconnectBaseDelayMs(), cassandraConfig.getReconnectMaxDelayMs()))
                .withSocketOptions(new SocketOptions()
                        .setConnectTimeoutMillis(cassandraConfig.getConnectTimeoutMs())
                        .setKeepAlive(cassandraConfig.isTcpKeepAlive())
                )
                .withSpeculativeExecutionPolicy(new PercentileSpeculativeExecutionPolicy(
                        PerHostPercentileTracker.builder(cassandraConfig.getHighestTrackableLatencyMs()).build(),
                        cassandraConfig.getSpeculativeRetryPercentile(),
                        cassandraConfig.getSpeculativeMaxRetries()
                ))
                .withCodecRegistry(new CodecRegistry()

                )
                .withoutMetrics() // FIXME: Needed for dependency issues with Dropwizard.
                .build();
    }

    @Provides @Singleton
    public Session getCassandraSession(final CassandraConfig cassandraConfig, final Cluster cassandraCluster) {
        final Session session = cassandraCluster.connect();
        session.execute(new SimpleStatement("USE " + cassandraConfig.getKeyspace()));
        return session;
    }

    @Provides @Singleton
    public MappingManager getMappingManager(final Session session) {
        return new MappingManager(session);
    }
}

