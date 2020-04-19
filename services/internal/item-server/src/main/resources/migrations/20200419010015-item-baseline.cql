CREATE TYPE item_attachment (
    kind int,
    modifiers map<int, int>,
    attributes map<int, text>
);

CREATE TABLE item (
    id uuid,
    kind int,
    inventory uuid,
    capacity int,
    name text,
    attachments frozen<list<item_attachment>>,
    PRIMARY KEY ((id))
) WITH COMPACTION = { 'class': 'LeveledCompactionStrategy' };

CREATE MATERIALIZED VIEW item_by_inventory
  AS SELECT inventory, id, kind, capacity, name, attachments
  FROM item
  WHERE inventory IS NOT NULL AND id IS NOT NULL
  PRIMARY KEY (inventory, id)
  WITH COMPACTION = { 'class': 'LeveledCompactionStrategy' };
