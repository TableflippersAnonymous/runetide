package com.runetide.services.internal.xp.server;

import com.runetide.common.Constants;
import com.runetide.common.Service;

public class XPService extends Service<XPConfiguration> {
    public static void main(String[] args) throws Exception {
        new XPService().run(args);
    }

    private XPService() {
        super(Constants.XP_SERVICE_NAME);
    }
}
