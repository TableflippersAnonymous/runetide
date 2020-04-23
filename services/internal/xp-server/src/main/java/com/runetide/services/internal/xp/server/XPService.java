package com.runetide.services.internal.xp.server;

import com.datastax.oss.driver.api.core.type.codec.TypeCodec;
import com.runetide.common.Constants;
import com.runetide.common.Service;
import com.runetide.services.internal.xp.client.XPClient;

import java.util.List;

public class XPService extends Service<XPConfiguration> {
    public static void main(String[] args) throws Exception {
        new XPService().run(args);
    }

    private XPService() {
        super(Constants.XP_SERVICE_NAME);
    }

    @Override
    protected List<TypeCodec<?>> getCqlTypeCodecs() {
        final List<TypeCodec<?>> list = super.getCqlTypeCodecs();
        list.addAll(XPClient.getCqlTypeCodecs());
        return list;
    }
}
