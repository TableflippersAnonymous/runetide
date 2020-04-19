CREATE TABLE xp (
    id uuid,
    parent uuid,
    type int,
    xp bigint,
    PRIMARY KEY ((id))
) WITH COMPACTION = { 'class': 'LeveledCompactionStrategy' };