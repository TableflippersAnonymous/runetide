package com.runetide.services.internal.resourcepool.server;

import com.datastax.oss.driver.api.core.type.codec.TypeCodec;
import com.hubspot.dropwizard.guice.GuiceBundle;
import com.runetide.common.Constants;
import com.runetide.common.Service;
import com.runetide.services.internal.resourcepool.client.ResourcePoolsClient;

import java.util.List;

public class ResourcePoolService extends Service<ResourcePoolConfiguration> {
    public static void main(final String[] args) throws Exception {
        new ResourcePoolService().run(args);
    }

    private ResourcePoolService() {
        super(Constants.RESOURCE_POOL_SERVICE_NAME);
    }

    @Override
    protected GuiceBundle.Builder<ResourcePoolConfiguration> addGuiceModules(GuiceBundle.Builder<ResourcePoolConfiguration> builder) {
        return super.addGuiceModules(builder)
                .addModule(new ResourcePoolGuiceModule());
    }

    @Override
    protected List<TypeCodec<?>> getCqlTypeCodecs() {
        final List<TypeCodec<?>> list = super.getCqlTypeCodecs();
        list.addAll(ResourcePoolsClient.getCqlTypeCodecs());
        return list;
    }
}
