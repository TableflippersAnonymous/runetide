package com.runetide.services.internal.item.server.dto;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.runetide.common.domain.ItemType;
import com.runetide.services.internal.inventory.common.InventoryRef;
import com.runetide.services.internal.resourcepool.common.ResourcePoolRef;
import com.runetide.services.internal.resourcepool.common.ResourceType;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@CqlName("item_by_parent_inventory")
public class ItemByParentInventory {
    @PartitionKey
    @CqlName("parent_inventory")
    private InventoryRef parentInventory;
    @ClusteringColumn
    @CqlName("id")
    private UUID id;
    @CqlName("kind")
    private ItemType kind;
    @CqlName("child_inventory")
    private InventoryRef childInventory;
    @CqlName("label")
    private String label;
    @CqlName("resource_pools")
    private Map<ResourceType, ResourcePoolRef> resourcePools;
    @CqlName("attachments")
    private List<ItemAttachment> attachments;

    public ItemByParentInventory() {
    }

    public ItemByParentInventory(InventoryRef parentInventory, UUID id, ItemType kind, InventoryRef childInventory,
                                 String label, Map<ResourceType, ResourcePoolRef> resourcePools,
                                 List<ItemAttachment> attachments) {
        this.parentInventory = parentInventory;
        this.id = id;
        this.kind = kind;
        this.childInventory = childInventory;
        this.label = label;
        this.resourcePools = resourcePools;
        this.attachments = attachments;
    }

    public InventoryRef getParentInventory() {
        return parentInventory;
    }

    public void setParentInventory(InventoryRef parentInventory) {
        this.parentInventory = parentInventory;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ItemType getKind() {
        return kind;
    }

    public void setKind(ItemType kind) {
        this.kind = kind;
    }

    public InventoryRef getChildInventory() {
        return childInventory;
    }

    public void setChildInventory(InventoryRef childInventory) {
        this.childInventory = childInventory;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Map<ResourceType, ResourcePoolRef> getResourcePools() {
        return resourcePools;
    }

    public void setResourcePools(Map<ResourceType, ResourcePoolRef> resourcePools) {
        this.resourcePools = resourcePools;
    }

    public List<ItemAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<ItemAttachment> attachments) {
        this.attachments = attachments;
    }
}
