CREATE TABLE IF NOT EXISTS region (
    world_id uuid,
    x bigint,
    z bigint,
    chunk_data_id uuid,
    instance_ids set<uuid>,
    new_instance_template text,
    difficulty_level int,
    settlement_ids set<uuid>,
    dungeon_ids set<uuid>,
    PRIMARY KEY ((world_id, x, z))
) WITH COMPACTION = { 'class': 'LeveledCompactionStrategy' };