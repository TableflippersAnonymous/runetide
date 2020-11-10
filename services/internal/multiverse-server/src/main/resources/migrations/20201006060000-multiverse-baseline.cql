CREATE TABLE multiverse (
    id uuid,
    public boolean,
    invite_code text,
    owner uuid,
    name text,
    PRIMARY KEY ((id))
) WITH COMPACTION = { 'class': 'LeveledCompactionStrategy' };