CREATE TABLE character (
    id uuid,
    entity_id uuid,
    account_id uuid,
    multiverse_id uuid,

    name text,
    race int,
    class int,

    spawn_instance uuid,

    xp uuid,
    resources map<int, uuid>,
    equipment map<int, uuid>,
    skills map<int, uuid>,
    special_abilities set<int>,

    /* Character customization:
     * - Base model on race
     * - Color selectors for:
     *   - Eyes
     *   - Skin
     *   - Hair
     *   - Secondary (markings, etc)
     * - Select one of each:
     *   - Gender
     *   - Head
     *   - Face
     *   - Eyes
     *   - Nose
     *   - Mouth
     *   - Ears
     *   - Horns
     *   - Markings??
     * - Slider for:
     *   - Height
     */

    PRIMARY KEY (id)
) WITH COMPACTION = { 'class': 'LeveledCompactionStrategy' };

CREATE MATERIALIZED VIEW characters_by_account
AS SELECT id, account_id, multiverse_id, name, race, class
   FROM character
   WHERE account_id IS NOT NULL AND id IS NOT NULL
PRIMARY KEY (account_id, id)
WITH COMPACTION = { 'class': 'LeveledCompactionStrategy' };

CREATE TABLE character_attribute_assignment (
    character_id uuid,
    level int,
    idx int,
    attribute int,

    PRIMARY KEY ((character_id), level, idx)
) WITH COMPACTION = { 'class': 'LeveledCompactionStrategy' };