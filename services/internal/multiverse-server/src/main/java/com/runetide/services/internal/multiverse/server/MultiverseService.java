package com.runetide.services.internal.multiverse.server;

import com.datastax.oss.driver.api.core.type.codec.TypeCodec;
import com.runetide.common.Constants;
import com.runetide.common.Service;
import com.runetide.services.internal.multiverse.client.MultiversesClient;

import java.util.List;

public class MultiverseService extends Service<MultiverseConfiguration> {
    public static void main(final String[] args) throws Exception {
        new MultiverseService().run(args);
    }

    private MultiverseService() {
        super(Constants.MULTIVERSE_SERVICE_NAME);
    }

    @Override
    protected List<TypeCodec<?>> getCqlTypeCodecs() {
        final List<TypeCodec<?>> list = super.getCqlTypeCodecs();
        list.addAll(MultiversesClient.getCqlTypeCodecs());
        return list;
    }
}
