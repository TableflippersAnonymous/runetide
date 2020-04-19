CREATE TYPE resource_pool_effect (
    interval bigint,
    repetitions bigint,
    delta bigint,
    override_min bigint,
    override_max bigint,
    ignore_normal_limits boolean,
    take_partial boolean,
    persistent boolean
);

CREATE TABLE resource_pool (
    id uuid,
    type int,
    value bigint,
    normal_limit_lower bigint,
    normal_limit_upper bigint,
    final_limit_lower bigint,
    final_limit_upper bigint,
    effects frozen<map<text, resource_pool_effect>>,
    PRIMARY KEY ((id))
) WITH COMPACTION = { 'class': 'LeveledCompactionStrategy' };