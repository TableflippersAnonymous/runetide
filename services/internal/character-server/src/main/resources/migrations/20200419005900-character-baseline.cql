CREATE TYPE character_resource_pool (
    current_value int,
    max_value int,
    temp_max int
);

CREATE TYPE character_resource_pools (
    health character_resource_pool,
    stamina character_resource_pool,
    mana character_resource_pool,
    hunger character_resource_pool,
    thirst character_resource_pool,
    breath character_resource_pool,
    pain character_resource_pool,
);

CREATE TYPE character_inventory_slots (
    head uuid,
    chest uuid,
    legs uuid,
    feet uuid,
    hands uuid,
    finger_0 uuid,
    finger_1 uuid,
    finger_2 uuid,
    finger_3 uuid,
    finger_4 uuid,
    finger_5 uuid,
    finger_6 uuid,
    finger_7 uuid,
    amulet uuid,
    trinket_0 uuid,
    trinket_1 uuid,
    neck uuid,
    wrist uuid,
    waist uuid,
    shoulders uuid,
    backpack uuid,
    main_hand uuid,
    off_hand uuid,
    hotbar_0 uuid,
    hotbar_1 uuid,
    hotbar_2 uuid,
    hotbar_3 uuid,
    hotbar_4 uuid,
    hotbar_5 uuid,
    hotbar_6 uuid,
    hotbar_7 uuid,
    hotbar_8 uuid,
    hotbar_9 uuid
);

CREATE TYPE character_attribute (

);

CREATE TYPE character_attributes (
    strength character_attribute,
    dexterity character_attribute,
    constitution charcter_attribute,
    stamina character_attribute,
    magicka character_attribute,
    charisma character_attribute
);

CREATE TYPE character_skill (
    xp int,
    level int
);

CREATE TYPE character_skills (
    fighting character_skill,
    survival character_skill,
    magic character_skill,
    gathering character_skill,
    mining character_skill,
    intimidation character_skill,
    deception character_skill,
    persuasion character_skill,
    stealth character_skill,
    sleight_of_hand character_skill,
    performance character_skill,
    perception character_skill,
    medicine character_skill,
    investigation character_skill,
    animal_handling character_skill,
    athletics character_skill,
    acrobatics character_skill,
    crafting charcter_skill,
    mechanics character_skill,
    leadership character_skill,
    cooking character_skill,
    fishing character_skill,
    lock_picking character_skill
);

CREATE TABLE character (
    id uuid,
    entity_id uuid,
    account_id uuid,
    multiverse_id uuid,

    name text,
    race int,
    class int,

    spawn_instance uuid,

    resources frozen<character_resource_pools>,
    equipment frozen<character_inventory_slots>,
    attributes frozen<character_attributes>,
    skills frozen<character_skills>,
    special_abilities set<int>,
);