CREATE TYPE item_attachment (
    kind int,
    modifiers map<int, int>,
    attributes map<int, text>
);

CREATE TABLE item (
    id uuid,
    kind int,
    parent_inventory uuid,
    child_inventory uuid,
    label text,
    resource_pools map<int, uuid>,
    attachments frozen<list<item_attachment>>,
    PRIMARY KEY ((id))
) WITH COMPACTION = { 'class': 'LeveledCompactionStrategy' };

CREATE MATERIALIZED VIEW item_by_parent_inventory
  AS SELECT parent_inventory, id, kind, child_inventory, label, resource_pools, attachments
  FROM item
  WHERE parent_inventory IS NOT NULL AND id IS NOT NULL
  PRIMARY KEY (parent_inventory, id)
  WITH COMPACTION = { 'class': 'LeveledCompactionStrategy' };
