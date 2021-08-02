package com.runetide.services.internal.entity.server;

import com.datastax.oss.driver.api.core.type.codec.TypeCodec;
import com.runetide.common.Constants;
import com.runetide.common.Service;
import com.runetide.services.internal.entity.client.EntitiesClient;

import java.util.List;

public class EntityService extends Service<EntityConfiguration> {
    public static void main(final String[] args) throws Exception {
        new EntityService().run(args);
    }

    private EntityService() {
        super(Constants.ENTITY_SERVICE_NAME);
    }

    @Override
    protected List<TypeCodec<?>> getCqlTypeCodecs() {
        final List<TypeCodec<?>> list = super.getCqlTypeCodecs();
        list.addAll(EntitiesClient.getCqlTypeCodecs());
        return list;
    }
}
