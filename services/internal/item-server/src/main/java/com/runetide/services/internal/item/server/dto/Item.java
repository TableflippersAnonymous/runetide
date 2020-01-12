package com.runetide.services.internal.item.server.dto;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.UUID;

@Table(name = "item")
public class Item {
    @PartitionKey
    private UUID id;
    @Enumerated
    private
    id uuid,
    kind int,
    inventory uuid,
    capacity int,
    name text,
    attachments frozen<list<item_attachment>>,
}
