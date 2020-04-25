package com.runetide.services.internal.character.common;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.ImmutableSet;
import com.runetide.common.domain.IndexedEnum;
import com.runetide.services.internal.xp.common.XPType;

import java.util.Set;

public enum ClassType implements IndexedEnum {
    RANGER(0,
            ImmutableSet.of(
                    new AttributeModifier(Attribute.DEXTERITY, 1.25),
                    new AttributeModifier(Attribute.STAMINA, 1.25)
            ),
            ImmutableSet.of(
                    new XPModifier(XPType.SKILL_FIGHTING, 1.25),
                    new XPModifier(XPType.SKILL_GATHERING, 1.25),
                    new XPModifier(XPType.SKILL_ACROBATICS, 1.25)
            ),
            ArmorType.MEDIUM,
            ImmutableSet.of(AbilityType.POTION_BREWING)
    ),
    WARRIOR(1,
            ImmutableSet.of(
                    new AttributeModifier(Attribute.STRENGTH, 1.25),
                    new AttributeModifier(Attribute.CONSTITUTION, 1.25)
            ),
            ImmutableSet.of(
                    new XPModifier(XPType.SKILL_FIGHTING, 1.25),
                    new XPModifier(XPType.SKILL_SURVIVAL, 1.25),
                    new XPModifier(XPType.SKILL_ATHLETICS, 1.25)
            ),
            ArmorType.HEAVY,
            ImmutableSet.of(AbilityType.WARCRAFTING)
    ),
    CLERIC(2,
            ImmutableSet.of(
                    new AttributeModifier(Attribute.CONSTITUTION, 1.25),
                    new AttributeModifier(Attribute.MAGICKA, 1.25)
            ),
            ImmutableSet.of(
                    new XPModifier(XPType.SKILL_MEDICINE, 1.50),
                    new XPModifier(XPType.SKILL_MAGIC, 1.25)
            ),
            ArmorType.MEDIUM,
            ImmutableSet.of(AbilityType.DIVINE_MAGIC, AbilityType.DIVINE_RITUALS)
    ),
    ENGINEER(3,
            ImmutableSet.of(
                    new AttributeModifier(Attribute.STAMINA, 1.25),
                    new AttributeModifier(Attribute.MAGICKA, 1.25)
            ),
            ImmutableSet.of(
                    new XPModifier(XPType.SKILL_CRAFTING, 1.25),
                    new XPModifier(XPType.SKILL_MECHANICS, 1.25),
                    new XPModifier(XPType.SKILL_INVESTIGATION, 1.25)
            ),
            ArmorType.LIGHT,
            ImmutableSet.of(AbilityType.LOCK_PICKING, AbilityType.GOLEMANCY)
    ),
    DRUID(4,
            ImmutableSet.of(
                    new AttributeModifier(Attribute.DEXTERITY, 1.25),
                    new AttributeModifier(Attribute.MAGICKA, 1.25)
            ),
            ImmutableSet.of(
                    new XPModifier(XPType.SKILL_ANIMAL_HANDLING, 1.25),
                    new XPModifier(XPType.SKILL_SURVIVAL, 1.25),
                    new XPModifier(XPType.SKILL_PERCEPTION, 1.25)
            ),
            ArmorType.LIGHT,
            ImmutableSet.of(AbilityType.NATURE_MAGIC, AbilityType.ANIMAL_SHAPE)
    ),
    SORCERER(5,
            ImmutableSet.of(
                    new AttributeModifier(Attribute.CHARISMA, 1.25),
                    new AttributeModifier(Attribute.MAGICKA, 1.25)
            ),
            ImmutableSet.of(
                    new XPModifier(XPType.SKILL_MAGIC, 1.75)
            ),
            ArmorType.NONE,
            ImmutableSet.of(AbilityType.ARCANE_MAGIC, AbilityType.SORCEROUS_MAGIC, AbilityType.CASTING_MANIPULATION,
                    AbilityType.FAMILIAR)
    ),
    WARLOCK(6,
            ImmutableSet.of(
                    new AttributeModifier(Attribute.CONSTITUTION, 1.25),
                    new AttributeModifier(Attribute.MAGICKA, 1.25)
            ),
            ImmutableSet.of(
                    new XPModifier(XPType.SKILL_MAGIC, 1.25),
                    new XPModifier(XPType.SKILL_SURVIVAL, 1.25),
                    new XPModifier(XPType.SKILL_INTIMIDATION, 1.25)
            ),
            ArmorType.MEDIUM,
            ImmutableSet.of(AbilityType.ARCANE_MAGIC, AbilityType.ELDRITCH_MAGIC, AbilityType.PATRON_FEATURES,
                    AbilityType.WARLOCK_SWORD, AbilityType.NECROMANCY)
    ),
    WIZARD(7,
            ImmutableSet.of(
                    new AttributeModifier(Attribute.STAMINA, 1.25),
                    new AttributeModifier(Attribute.MAGICKA, 1.25)
            ),
            ImmutableSet.of(
                    new XPModifier(XPType.SKILL_MAGIC, 1.50),
                    new XPModifier(XPType.SKILL_INVESTIGATION, 1.25)
            ),
            ArmorType.NONE,
            ImmutableSet.of(AbilityType.ARCANE_MAGIC, AbilityType.SORCEROUS_MAGIC, AbilityType.WIZARD_MAGIC,
                    AbilityType.RUNE_CRAFTING)
    ),
    PALADIN(8,
            ImmutableSet.of(
                    new AttributeModifier(Attribute.STRENGTH, 1.25),
                    new AttributeModifier(Attribute.CONSTITUTION, 1.25)
            ),
            ImmutableSet.of(
                    new XPModifier(XPType.SKILL_FIGHTING, 1.25),
                    new XPModifier(XPType.SKILL_SURVIVAL, 1.25),
                    new XPModifier(XPType.SKILL_ATHLETICS, 1.25)
            ),
            ArmorType.HEAVY,
            ImmutableSet.of(AbilityType.DIVINE_MAGIC)
    ),
    BARD(9,
            ImmutableSet.of(
                    new AttributeModifier(Attribute.CHARISMA, 1.25),
                    new AttributeModifier(Attribute.DEXTERITY, 1.25)
            ),
            ImmutableSet.of(
                    new XPModifier(XPType.SKILL_INTIMIDATION, 1.25),
                    new XPModifier(XPType.SKILL_DECEPTION, 1.25),
                    new XPModifier(XPType.SKILL_PERSUASION, 1.25),
                    new XPModifier(XPType.SKILL_PERFORMANCE, 1.25)
            ),
            ArmorType.NONE,
            ImmutableSet.of(AbilityType.ARCANE_MAGIC, AbilityType.MUSIC_CRAFTING)
    ),
    MONK(10,
            ImmutableSet.of(
                    new AttributeModifier(Attribute.STRENGTH, 1.25),
                    new AttributeModifier(Attribute.DEXTERITY, 1.25),
                    new AttributeModifier(Attribute.STAMINA, 1.25)
            ),
            ImmutableSet.of(
                    new XPModifier(XPType.SKILL_FIGHTING, 1.25),
                    new XPModifier(XPType.SKILL_SURVIVAL, 1.25),
                    new XPModifier(XPType.SKILL_ACROBATICS, 1.25)
            ),
            ArmorType.NONE,
            ImmutableSet.of()
    ),
    ASSASSIN(11,
            ImmutableSet.of(
                    new AttributeModifier(Attribute.DEXTERITY, 1.25),
                    new AttributeModifier(Attribute.STAMINA, 1.25)
            ),
            ImmutableSet.of(
                    new XPModifier(XPType.SKILL_STEALTH, 1.25),
                    new XPModifier(XPType.SKILL_SLEIGHT_OF_HAND, 1.25),
                    new XPModifier(XPType.SKILL_DECEPTION, 1.25),
                    new XPModifier(XPType.SKILL_INVESTIGATION, 1.25)
            ),
            ArmorType.LIGHT,
            ImmutableSet.of(AbilityType.LOCK_PICKING, AbilityType.TRAPS)
    );

    private final int id;
    private final Set<AttributeModifier> attributeModifiers;
    private final Set<XPModifier> xpModifiers;
    private final ArmorType armorType;
    private final Set<AbilityType> abilities;

    ClassType(int id, Set<AttributeModifier> attributeModifiers, Set<XPModifier> xpModifiers, ArmorType armorType,
              Set<AbilityType> abilities) {
        this.id = id;
        this.attributeModifiers = attributeModifiers;
        this.xpModifiers = xpModifiers;
        this.armorType = armorType;
        this.abilities = abilities;
    }


    @Override
    @JsonValue
    public int toValue() {
        return id;
    }
}
