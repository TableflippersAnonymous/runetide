package com.runetide.services.internal.item.server;

import com.runetide.common.Constants;
import com.runetide.common.Service;

public class ItemService extends Service<ItemConfiguration> {
    public static void main(final String[] args) throws Exception {
        new ItemService().run(args);
    }

    private ItemService() {
        super(Constants.ITEM_SERVICE_NAME);
    }
}
