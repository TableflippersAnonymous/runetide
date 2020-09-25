CREATE TABLE world (
    id uuid,
    kind int,
    name text,
    seed bigint,
    multiverse uuid,
    PRIMARY KEY ((id))
) WITH COMPACTION = { 'class': 'LeveledCompactionStrategy' };