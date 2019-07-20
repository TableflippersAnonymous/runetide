package com.runetide.common;

import com.runetide.common.services.servicediscovery.ServiceData;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceType;

import javax.inject.Singleton;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class ServiceRegistry {
    private final CuratorFramework curatorFramework;
    private final ServiceDiscovery<ServiceData> serviceDiscovery;
    private final Map<String, ServiceInstance<ServiceData>> registeredServices = new ConcurrentHashMap<>();

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
}
