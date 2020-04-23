package com.runetide.services.internal.character.server.dto;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.runetide.common.dto.EntityRef;
import com.runetide.common.dto.InstanceRef;
import com.runetide.common.dto.ItemRef;
import com.runetide.services.internal.character.common.CharacterRef;
import com.runetide.services.internal.character.common.EquipmentSlot;
import com.runetide.services.internal.resourcepool.common.ResourcePoolRef;
import com.runetide.services.internal.resourcepool.common.ResourceType;
import com.runetide.services.internal.xp.common.XPRef;
import com.runetide.services.internal.xp.common.XPType;

import java.util.Map;

@Entity
@CqlName("character")
public class Character {
    private CharacterRef id;
    private EntityRef entityId;
    private AccountRef accountId;
    private MultiverseRef multiverseId;
    @CqlName("name")
    private String name;
    private RaceType race;
    private ClassType classType;
    private InstanceRef spawnInstance;
    private XPRef xp;
    private Map<ResourceType, ResourcePoolRef> resources;
    private Map<EquipmentSlot, ItemRef> equipment;
    private Map<XPType, XPRef> skills;
    private Set<SpecialAbilityType> specialAbilities;
}
