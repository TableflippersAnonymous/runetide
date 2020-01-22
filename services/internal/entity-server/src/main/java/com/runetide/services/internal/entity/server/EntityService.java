package com.runetide.services.internal.entity.server;

import com.runetide.common.Constants;
import com.runetide.common.Service;

public class EntityService extends Service<EntityConfiguration> {
    public static void main(final String[] args) throws Exception {
        new EntityService().run(args);
    }

    private EntityService() {
        super(Constants.ENTITY_SERVICE_NAME);
    }
}
