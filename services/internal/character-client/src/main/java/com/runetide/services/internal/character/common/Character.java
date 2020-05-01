package com.runetide.services.internal.character.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.runetide.common.dto.InstanceRef;
import com.runetide.common.dto.ItemRef;
import com.runetide.services.internal.account.common.AccountRef;
import com.runetide.services.internal.entity.common.dto.EntityRef;
import com.runetide.services.internal.multiverse.common.MultiverseRef;
import com.runetide.services.internal.resourcepool.common.ResourcePool;
import com.runetide.services.internal.resourcepool.common.ResourceType;
import com.runetide.services.internal.xp.common.XP;
import com.runetide.services.internal.xp.common.XPType;

import java.util.Map;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Character {
    @JsonProperty("$")
    private CharacterRef id;
    @JsonProperty("e")
    private EntityRef entity;
    @JsonProperty("a")
    private AccountRef account;
    @JsonProperty("m")
    private MultiverseRef multiverse;
    @JsonProperty("n")
    private String name;
    @JsonProperty("r")
    private RaceType race;
    @JsonProperty("c")
    private ClassType classType;
    @JsonProperty("i")
    private InstanceRef instance;
    @JsonProperty("x")
    private XP xp;
    @JsonProperty("R")
    private Map<ResourceType, ResourcePool> resources;
    @JsonProperty("E")
    private Map<EquipmentSlot, ItemRef> equipment;
    @JsonProperty("s")
    private Map<XPType, XP> skills;
    @JsonProperty("S")
    private Set<SpecialAbilityType> specialAbilities;
    @JsonProperty("A")
    private Map<Attribute, Long> attributes;

    public Character() {
    }

    public Character(CharacterRef id, EntityRef entity, AccountRef account, MultiverseRef multiverse, String name,
                     RaceType race, ClassType classType, InstanceRef instance, XP xp,
                     Map<ResourceType, ResourcePool> resources, Map<EquipmentSlot, ItemRef> equipment,
                     Map<XPType, XP> skills, Set<SpecialAbilityType> specialAbilities,
                     Map<Attribute, Long> attributes) {
        this.id = id;
        this.entity = entity;
        this.account = account;
        this.multiverse = multiverse;
        this.name = name;
        this.race = race;
        this.classType = classType;
        this.instance = instance;
        this.xp = xp;
        this.resources = resources;
        this.equipment = equipment;
        this.skills = skills;
        this.specialAbilities = specialAbilities;
        this.attributes = attributes;
    }

    public CharacterRef getId() {
        return id;
    }

    public void setId(CharacterRef id) {
        this.id = id;
    }

    public EntityRef getEntity() {
        return entity;
    }

    public void setEntity(EntityRef entity) {
        this.entity = entity;
    }

    public AccountRef getAccount() {
        return account;
    }

    public void setAccount(AccountRef account) {
        this.account = account;
    }

    public MultiverseRef getMultiverse() {
        return multiverse;
    }

    public void setMultiverse(MultiverseRef multiverse) {
        this.multiverse = multiverse;
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

    public InstanceRef getInstance() {
        return instance;
    }

    public void setInstance(InstanceRef instance) {
        this.instance = instance;
    }

    public XP getXp() {
        return xp;
    }

    public void setXp(XP xp) {
        this.xp = xp;
    }

    public Map<ResourceType, ResourcePool> getResources() {
        return resources;
    }

    public void setResources(Map<ResourceType, ResourcePool> resources) {
        this.resources = resources;
    }

    public Map<EquipmentSlot, ItemRef> getEquipment() {
        return equipment;
    }

    public void setEquipment(Map<EquipmentSlot, ItemRef> equipment) {
        this.equipment = equipment;
    }

    public Map<XPType, XP> getSkills() {
        return skills;
    }

    public void setSkills(Map<XPType, XP> skills) {
        this.skills = skills;
    }

    public Set<SpecialAbilityType> getSpecialAbilities() {
        return specialAbilities;
    }

    public void setSpecialAbilities(Set<SpecialAbilityType> specialAbilities) {
        this.specialAbilities = specialAbilities;
    }

    public Map<Attribute, Long> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<Attribute, Long> attributes) {
        this.attributes = attributes;
    }
}
