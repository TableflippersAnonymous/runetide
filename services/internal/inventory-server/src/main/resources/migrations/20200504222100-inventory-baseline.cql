CREATE TABLE inventory (
    id uuid,
    max_slots int,
    max_items int,
    max_weight int,
    PRIMARY KEY ((id))
) WITH COMPACTION = { 'class': 'LeveledCompactionStrategy' };