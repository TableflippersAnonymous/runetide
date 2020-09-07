package com.runetide.services.internal.item.server.dao;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Select;
import com.runetide.common.dto.ItemRef;
import com.runetide.common.services.cql.BaseDao;
import com.runetide.services.internal.inventory.common.InventoryRef;
import com.runetide.services.internal.item.common.Item;
import com.runetide.services.internal.item.server.dto.ItemByParentInventory;

@Dao
public interface ItemDao extends BaseDao<Item> {
    @Select
    Item getItem(ItemRef itemRef);

    @Select
    PagingIterable<ItemByParentInventory> getItems(InventoryRef inventoryRef);
}
