package com.runetide.services.internal.character.common;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.ImmutableSet;
import com.runetide.common.domain.IndexedEnum;

import java.util.Set;

public enum RaceType implements IndexedEnum {
    HUMAN(0, Size.MEDIUM,
            ImmutableSet.of(Language.COMMON), 2,
            ImmutableSet.of(
                    new AttributeModifier(Attribute.STRENGTH, 1.01),
                    new AttributeModifier(Attribute.DEXTERITY, 1.01),
                    new AttributeModifier(Attribute.CONSTITUTION, 1.01),
                    new AttributeModifier(Attribute.STAMINA, 1.01),
                    new AttributeModifier(Attribute.MAGICKA, 1.01),
                    new AttributeModifier(Attribute.CHARISMA, 1.01)
            ),
            ImmutableSet.of()
    ),
    ELF(1, Size.MEDIUM,
            ImmutableSet.of(Language.COMMON, Language.ELVISH), 1,
            ImmutableSet.of(
                    new AttributeModifier(Attribute.DEXTERITY, 1.01),
                    new AttributeModifier(Attribute.MAGICKA, 1.01)
            ),
            ImmutableSet.of(Trait.MAGIC_SIGHT, Trait.NATURAL_DIPLOMAT)
    ),
    DWARF(2, Size.SMALL,
            ImmutableSet.of(Language.COMMON, Language.DWARVISH), 1,
            ImmutableSet.of(
                    new AttributeModifier(Attribute.STRENGTH, 1.01),
                    new AttributeModifier(Attribute.CONSTITUTION, 1.01)
            ),
            ImmutableSet.of(Trait.NATURAL_MINER, Trait.COLD_RESIST)
    ),
    GNOME(3, Size.SMALL,
            ImmutableSet.of(Language.COMMON, Language.GNOMISH), 1,
            ImmutableSet.of(
                    new AttributeModifier(Attribute.DEXTERITY, 1.01),
                    new AttributeModifier(Attribute.CHARISMA, 1.01)
            ),
            ImmutableSet.of(Trait.LUCKY, Trait.NATURAL_CARTOGRAPHER, Trait.EASILY_SATED)
    ),
    KOBOLD(4, Size.SMALL,
            ImmutableSet.of(Language.COMMON, Language.DRACONIC), 1,
            ImmutableSet.of(
                    new AttributeModifier(Attribute.DEXTERITY, 1.01),
                    new AttributeModifier(Attribute.STAMINA, 1.01)
            ),
            ImmutableSet.of(Trait.BREATH_BOOST, Trait.NIGHT_VISION, Trait.LIGHT_SENSITIVITY, Trait.MONSTROUS_HIDING)
    ),
    GOBLIN(5, Size.SMALL,
            ImmutableSet.of(Language.GOBLINOID), 2,
            ImmutableSet.of(
                    new AttributeModifier(Attribute.STRENGTH, 1.01),
                    new AttributeModifier(Attribute.DEXTERITY, 1.01)
            ),
            ImmutableSet.of(Trait.LOOTING, Trait.HEAT_RESIST)
    ),
    ORC(6, Size.MEDIUM,
            ImmutableSet.of(Language.ORCISH), 2,
            ImmutableSet.of(
                    new AttributeModifier(Attribute.STRENGTH, 1.01),
                    new AttributeModifier(Attribute.STAMINA, 1.01)
            ),
            ImmutableSet.of(Trait.GREATER_OMNIVORE)
    ),
    DROW(7, Size.MEDIUM,
            ImmutableSet.of(Language.COMMON, Language.ELVISH), 1,
            ImmutableSet.of(
                    new AttributeModifier(Attribute.CHARISMA, 1.01),
                    new AttributeModifier(Attribute.DEXTERITY, 1.01)
            ),
            ImmutableSet.of(Trait.MAGIC_SIGHT, Trait.NIGHT_VISION, Trait.LIGHT_SENSITIVITY, Trait.NATURAL_REGEN)
    ),
    CONSTRUCT(8, Size.MEDIUM,
            ImmutableSet.of(), 3,
            ImmutableSet.of(
                    new AttributeModifier(Attribute.STRENGTH, 1.01),
                    new AttributeModifier(Attribute.STAMINA, 1.02),
                    new AttributeModifier(Attribute.CHARISMA, 0.99)
            ),
            ImmutableSet.of(Trait.UNBREATHING, Trait.UNDRINKING, Trait.UNEATING, Trait.WATER_VULNERABILITY)
    ),
    FELINIAN(9, Size.MEDIUM,
            ImmutableSet.of(Language.COMMON, Language.FELINE), 1,
            ImmutableSet.of(
                    new AttributeModifier(Attribute.DEXTERITY, 1.01),
                    new AttributeModifier(Attribute.CONSTITUTION, 1.01)
            ),
            ImmutableSet.of(Trait.NIGHT_VISION, Trait.LUCKY, Trait.FEATHER_FALLING, Trait.COLD_RESIST)
    );

    private final int id;
    private final Size size;
    private final Set<Language> languages;
    private final int freeLanguages;
    private final Set<AttributeModifier> attributeModifiers;
    private final Set<Trait> traits;

    RaceType(int id, Size size, Set<Language> languages, int freeLanguages, Set<AttributeModifier> attributeModifiers,
             Set<Trait> traits) {
        this.id = id;
        this.size = size;
        this.languages = languages;
        this.freeLanguages = freeLanguages;
        this.attributeModifiers = attributeModifiers;
        this.traits = traits;
    }


    @Override
    @JsonValue
    public int toValue() {
        return id;
    }
}
