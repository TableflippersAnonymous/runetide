package com.runetide.services.internal.character.server.dto;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.runetide.common.dto.InstanceRef;
import com.runetide.common.dto.ItemRef;
import com.runetide.services.internal.account.common.AccountRef;
import com.runetide.services.internal.character.common.*;
import com.runetide.services.internal.entity.common.dto.EntityRef;
import com.runetide.services.internal.multiverse.common.MultiverseRef;
import com.runetide.services.internal.resourcepool.common.ResourcePoolRef;
import com.runetide.services.internal.resourcepool.common.ResourceType;
import com.runetide.services.internal.xp.common.XPRef;
import com.runetide.services.internal.xp.common.XPType;

import java.util.Map;
import java.util.Set;

@Entity
@CqlName("character")
public class Character {
    @PartitionKey
    @CqlName("id")
    private CharacterRef id;
    @CqlName("entity_id")
    private EntityRef entityId;
    @CqlName("account_id")
    private AccountRef accountId;
    @CqlName("multiverse_id")
    private MultiverseRef multiverseId;
    @CqlName("name")
    private String name;
    @CqlName("race")
    private RaceType race;
    @CqlName("class")
    private ClassType classType;
    @CqlName("spawn_instance")
    private InstanceRef spawnInstance;
    @CqlName("xp")
    private XPRef xp;
    @CqlName("resources")
    private Map<ResourceType, ResourcePoolRef> resources;
    @CqlName("equipment")
    private Map<EquipmentSlot, ItemRef> equipment;
    @CqlName("skills")
    private Map<XPType, XPRef> skills;
    @CqlName("special_abilities")
    private Set<SpecialAbilityType> specialAbilities;

    public Character() {
    }

    public Character(CharacterRef id, EntityRef entityId, AccountRef accountId, MultiverseRef multiverseId,
                     String name, RaceType race, ClassType classType, InstanceRef spawnInstance, XPRef xp,
                     Map<ResourceType, ResourcePoolRef> resources, Map<EquipmentSlot, ItemRef> equipment,
                     Map<XPType, XPRef> skills, Set<SpecialAbilityType> specialAbilities) {
        this.id = id;
        this.entityId = entityId;
        this.accountId = accountId;
        this.multiverseId = multiverseId;
        this.name = name;
        this.race = race;
        this.classType = classType;
        this.spawnInstance = spawnInstance;
        this.xp = xp;
        this.resources = resources;
        this.equipment = equipment;
        this.skills = skills;
        this.specialAbilities = specialAbilities;
    }

    public CharacterRef getId() {
        return id;
    }

    public void setId(CharacterRef id) {
        this.id = id;
    }

    public EntityRef getEntityId() {
        return entityId;
    }

    public void setEntityId(EntityRef entityId) {
        this.entityId = entityId;
    }

    public AccountRef getAccountId() {
        return accountId;
    }

    public void setAccountId(AccountRef accountId) {
        this.accountId = accountId;
    }

    public MultiverseRef getMultiverseId() {
        return multiverseId;
    }

    public void setMultiverseId(MultiverseRef multiverseId) {
        this.multiverseId = multiverseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RaceType getRace() {
        return race;
    }

    public void setRace(RaceType race) {
        this.race = race;
    }

    public ClassType getClassType() {
        return classType;
    }

    public void setClassType(ClassType classType) {
        this.classType = classType;
    }

    public InstanceRef getSpawnInstance() {
        return spawnInstance;
    }

    public void setSpawnInstance(InstanceRef spawnInstance) {
        this.spawnInstance = spawnInstance;
    }

    public XPRef getXp() {
        return xp;
    }

    public void setXp(XPRef xp) {
        this.xp = xp;
    }

    public Map<ResourceType, ResourcePoolRef> getResources() {
        return resources;
    }

    public void setResources(Map<ResourceType, ResourcePoolRef> resources) {
        this.resources = resources;
    }

    public Map<EquipmentSlot, ItemRef> getEquipment() {
        return equipment;
    }

    public void setEquipment(Map<EquipmentSlot, ItemRef> equipment) {
        this.equipment = equipment;
    }

    public Map<XPType, XPRef> getSkills() {
        return skills;
    }

    public void setSkills(Map<XPType, XPRef> skills) {
        this.skills = skills;
    }

    public Set<SpecialAbilityType> getSpecialAbilities() {
        return specialAbilities;
    }

    public void setSpecialAbilities(Set<SpecialAbilityType> specialAbilities) {
        this.specialAbilities = specialAbilities;
    }
}
