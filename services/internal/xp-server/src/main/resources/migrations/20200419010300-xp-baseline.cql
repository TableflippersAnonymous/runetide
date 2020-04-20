CREATE TABLE xp (
    id uuid,
    parent uuid,
    type int,
    xp bigint,
    PRIMARY KEY ((id))
) WITH COMPACTION = { 'class': 'LeveledCompactionStrategy' };

CREATE MATERIALIZED VIEW xp_by_parent
AS SELECT parent, id, type, xp
   FROM xp
   WHERE parent IS NOT NULL AND id IS NOT NULL
PRIMARY KEY (parent, id)
WITH COMPACTION = { 'class': 'LeveledCompactionStrategy' };