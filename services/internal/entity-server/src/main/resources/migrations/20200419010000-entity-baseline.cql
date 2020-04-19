CREATE TYPE entity_position (
    bx int,
    "by" int,
    bz int,
    ox int,
    oy int,
    oz int
);

CREATE TYPE entity_vector (
    x int,
    y int,
    z int
);

CREATE TYPE entity_direction (
    e int,
    r int
);

CREATE TYPE entity_effect (
    kind int,
    duration int,
    intensity int
);

CREATE TABLE entity (
    id uuid,
    kind int,
    instance uuid,
    inventory uuid,
    position frozen<entity_position>,
    speed frozen<entity_vector>,
    accel frozen<entity_vector>,
    face frozen<entity_direction>,
    look frozen<entity_direction>,
    effects frozen<set<entity_effect>>,
    PRIMARY KEY ((id))
) WITH COMPACTION = { 'class': 'LeveledCompactionStrategy' };

CREATE MATERIALIZED VIEW entities_by_instance
  AS SELECT id, kind, instance, inventory, position, speed, accel, face, look, effects
   FROM entity
   WHERE instance IS NOT NULL AND id IS NOT NULL
  PRIMARY KEY (instance, id)
  WITH COMPACTION = { 'class': 'LeveledCompactionStrategy' };