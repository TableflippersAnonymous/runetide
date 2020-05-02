package com.runetide.common.services.servicediscovery;

import com.google.common.collect.Iterables;
import com.runetide.common.services.servicediscovery.ServiceData;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class ServiceRegistry {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ServiceDiscovery<ServiceData> serviceDiscovery;
    private final Map<String, ServiceInstance<ServiceData>> registeredServices = new ConcurrentHashMap<>();

    @Inject
    public ServiceRegistry(final ServiceDiscovery<ServiceData> serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    public void register(final String name) throws Exception {
        final ServiceInstance<ServiceData> serviceInstance = ServiceInstance.<ServiceData>builder()
                .serviceType(ServiceType.DYNAMIC)
                .payload(new ServiceData(0))
                .name(name)
                .id(UUID.randomUUID().toString())
                .build();
        registeredServices.put(name, serviceInstance);
        serviceDiscovery.registerService(serviceInstance);
    }

    public void unregister(final String name) throws Exception {
        final ServiceInstance<ServiceData> serviceInstance = registeredServices.remove(name);
        if(serviceInstance != null)
            serviceDiscovery.unregisterService(serviceInstance);
    }

    public ServiceInstance<ServiceData> getFirst(final String name) throws Exception {
        final Collection<ServiceInstance<ServiceData>> serviceInstances = serviceDiscovery.queryForInstances(name);
        return Iterables.getFirst(serviceInstances, null);
    }

    public ServiceInstance<ServiceData> getRandom(final String name) throws Exception {
        final Collection<ServiceInstance<ServiceData>> serviceInstances = serviceDiscovery.queryForInstances(name);
        return serviceInstances.stream()
                .skip((int) (serviceInstances.size() * Math.random()))
                .findFirst().orElse(null);
    }

    public URI getRandomUri(final String name) {
        final ServiceInstance<ServiceData> serviceInstance;
        try {
            serviceInstance = getRandom(name);
        } catch (final Exception e) {
            LOG.error("Caught exception getting URI for service discovery.");
            throw new RuntimeException(e);
        }
        if (serviceInstance == null)
            throw new IllegalStateException();
        return URI.create("http://" + serviceInstance.getAddress());
    }

    public void clear() {
        for(final String key : registeredServices.keySet()) {
            try {
                unregister(key);
            } catch (final Exception e) {
                LOG.error("Got exception during clear unregistering {}", key, e);
            }
        }
    }
}
