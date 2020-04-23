package com.runetide.common.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ItemType implements IndexedEnum {
/*    TALC(0, 5, BlockType.TALC, null, null),
    GYPSUM(1, 5, BlockType.GYPSUM, null, null),
    CALCITE(2, 5, BlockType.CALCITE, null, null),
    LIMESTONE(3, 5, BlockType.LIMESTONE, null, null),
    SANDSTONE(4, 10, BlockType.SANDSTONE, null, null),
    MARBLE(5, 10, BlockType.MARBLE, null, null),
    APATITE(6, 10, BlockType.APATITE, null, null),
    BASALT(7, 10, BlockType.BASALT, null, null),
    OBSIDIAN(8, 15, BlockType.OBSIDIAN, null, null),
    FELDSPAR(9, 15, BlockType.FELDSPAR, null, null),
    PUMICE(10, 15, BlockType.PUMICE, null, null),
    GRANITE(11, 15, BlockType.GRANITE, null, null),
    RHYOLITE(12, 20, BlockType.RHYOLITE, null, null),
    DIORITE(13, 20, BlockType.DIORITE, null, null),
    ANDESITE(14, 50, BlockType.ANDESITE, null, null),*/

    BROKEN_TALC(15, 5, BlockType.BROKEN_TALC, null, null),
    BROKEN_GYPSUM(16, 5, BlockType.BROKEN_GYPSUM, null, null),
    BROKEN_CALCITE(17, 5, BlockType.BROKEN_CALCITE, null, null),
    BROKEN_LIMESTONE(18, 5, BlockType.BROKEN_LIMESTONE, null, null),
    BROKEN_SANDSTONE(19, 10, BlockType.BROKEN_SANDSTONE, null, null),
    BROKEN_MARBLE(20, 10, BlockType.BROKEN_MARBLE, null, null),
    BROKEN_APATITE(21, 10, BlockType.BROKEN_APATITE, null, null),
    BROKEN_BASALT(22, 10, BlockType.BROKEN_BASALT, null, null),
    BROKEN_OBSIDIAN(23, 15, BlockType.BROKEN_OBSIDIAN, null, null),
    BROKEN_FELDSPAR(24, 15, BlockType.BROKEN_FELDSPAR, null, null),
    BROKEN_PUMICE(25, 15, BlockType.BROKEN_PUMICE, null, null),
    BROKEN_GRANITE(26, 15, BlockType.BROKEN_GRANITE, null, null),
    BROKEN_RHYOLITE(27, 20, BlockType.BROKEN_RHYOLITE, null, null),
    BROKEN_DIORITE(28, 20, BlockType.BROKEN_DIORITE, null, null),
    BROKEN_ANDESITE(29, 50, BlockType.BROKEN_ANDESITE, null, null),
/*
    DIRT(30, 5, BlockType.DIRT, null, null),
    GRAVEL(31, 5, BlockType.GRAVEL, null, null),
    SAND(32, 5, BlockType.SAND, null, null),
    CLAY(33, 5, null, null, null),
    OAK_LOG(92, 5, BlockType.OAK_LOG, null, null),

    ANTHITE(34, 5, BlockType.ANTHITE, null, null),
    BARITE(35, 5, BlockType.BARITE, null, null),
    BAUXITE(36, 5, BlockType.BAUXITE, null, null),
    BERYL(37, 5, BlockType.BERYL, null, null),
    BORNITE(38, 5, BlockType.BORNITE, null, null),
    CASSITERITE(39, 5, BlockType.CASSITERITE, null, null),
    CHALCOCITE(40, 5, BlockType.CHALCOCITE, null, null),
    CHALCOPYRITE(41, 5, BlockType.CHALCOPYRITE, null, null),
    CHROMITE(42, 5, BlockType.CHROMITE, null, null),
    CINNABAR(43, 5, BlockType.CINNABAR, null, null),
    COAL_ORE(44, 5, BlockType.COAL_ORE, null, null),
    COBALTITE(45, 5, BlockType.COBALTITE, null, null),
    COLTAN(46, 5, BlockType.COLTAN, null, null),
    DOLOMITE(47, 5, BlockType.DOLOMITE, null, null),
    GALENA(48, 5, BlockType.GALENA, null, null),
    GOLD_ORE(49, 5, BlockType.GOLD_ORE, null, null),
    HEMATITE(50, 5, BlockType.HEMATITE, null, null),
    ILMENITE(51, 5, BlockType.ILMENITE, null, null),
    IRIDIUM_ORE(52, 5, BlockType.IRIDIUM_ORE, null, null),
    MAGNETITE(53, 5, BlockType.MAGNETITE, null, null),
    MALACHITE(54, 5, BlockType.MALACHITE, null, null),
    MOLYBDENITE(55, 5, BlockType.MOLYBDENITE, null, null),
    PENTLANDITE(56, 5, BlockType.PENTLANDITE, null, null),
    PYROLUSITE(57, 5, BlockType.PYROLUSITE, null, null),
    SCHEELITE(58, 5, BlockType.SCHEELITE, null, null),
    SPERRYLITE(59, 5, BlockType.SPERRYLITE, null, null),
    SPHALERITE(60, 5, BlockType.SPHALERITE, null, null),
    URANINITE(61, 5, BlockType.URANINITE, null, null),
    WOLFRAMITE(62, 5, BlockType.WOLFRAMITE, null, null),

    BROKEN_ANTHITE(63, 5, BlockType.BROKEN_ANTHITE, null, null),
    BROKEN_BARITE(64, 5, BlockType.BROKEN_BARITE, null, null),
    BROKEN_BAUXITE(65, 5, BlockType.BROKEN_BAUXITE, null, null),
    BROKEN_BERYL(66, 5, BlockType.BROKEN_BERYL, null, null),
    BROKEN_BORNITE(67, 5, BlockType.BROKEN_BORNITE, null, null),
    BROKEN_CASSITERITE(68, 5, BlockType.BROKEN_CASSITERITE, null, null),
    BROKEN_CHALCOCITE(69, 5, BlockType.BROKEN_CHALCOCITE, null, null),
    BROKEN_CHALCOPYRITE(70, 5, BlockType.BROKEN_CHALCOPYRITE, null, null),
    BROKEN_CHROMITE(71, 5, BlockType.BROKEN_CHROMITE, null, null),
    BROKEN_CINNABAR(72, 5, BlockType.BROKEN_CINNABAR, null, null),
    BROKEN_COAL_ORE(73, 5, BlockType.BROKEN_COAL_ORE, null, null),
    BROKEN_COBALTITE(74, 5, BlockType.BROKEN_COBALTITE, null, null),
    BROKEN_COLTAN(75, 5, BlockType.BROKEN_COLTAN, null, null),
    BROKEN_DOLOMITE(76, 5, BlockType.BROKEN_DOLOMITE, null, null),
    BROKEN_GALENA(77, 5, BlockType.BROKEN_GALENA, null, null),
    BROKEN_GOLD_ORE(78, 5, BlockType.BROKEN_GOLD_ORE, null, null),
    BROKEN_HEMATITE(79, 5, BlockType.BROKEN_HEMATITE, null, null),
    BROKEN_ILMENITE(80, 5, BlockType.BROKEN_ILMENITE, null, null),
    BROKEN_IRIDIUM_ORE(81, 5, BlockType.BROKEN_IRIDIUM_ORE, null, null),
    BROKEN_MAGNETITE(82, 5, BlockType.BROKEN_MAGNETITE, null, null),
    BROKEN_MALACHITE(83, 5, BlockType.BROKEN_MALACHITE, null, null),
    BROKEN_MOLYBDENITE(84, 5, BlockType.BROKEN_MOLYBDENITE, null, null),
    BROKEN_PENTLANDITE(85, 5, BlockType.BROKEN_PENTLANDITE, null, null),
    BROKEN_PYROLUSITE(86, 5, BlockType.BROKEN_PYROLUSITE, null, null),
    BROKEN_SCHEELITE(87, 5, BlockType.BROKEN_SCHEELITE, null, null),
    BROKEN_SPERRYLITE(88, 5, BlockType.BROKEN_SPERRYLITE, null, null),
    BROKEN_SPHALERITE(89, 5, BlockType.BROKEN_SPHALERITE, null, null),
    BROKEN_URANINITE(90, 5, BlockType.BROKEN_URANINITE, null, null),
    BROKEN_WOLFRAMITE(91, 5, BlockType.BROKEN_WOLFRAMITE, null, null),

    SILVER_BLOCK(93, 50, BlockType.SILVER_BLOCK, null, null),
    SULFUR_BLOCK(94, 50, BlockType.SULFUR_BLOCK, null, null),
    BARIUM_BLOCK(95, 50, BlockType.BARIUM_BLOCK, null, null),
    ALUMINUM_BLOCK(96, 50, BlockType.ALUMINUM_BLOCK, null, null),
    BERYLLIUM_BLOCK(97, 50, BlockType.BERYLLIUM_BLOCK, null, null),
    SILICON_BLOCK(98, 50, BlockType.SILICON_BLOCK, null, null),
    COPPER_BLOCK(99, 50, BlockType.COPPER_BLOCK, null, null),
    IRON_BLOCK(100, 50, BlockType.IRON_BLOCK, null, null),
    TIN_BLOCK(101, 50, BlockType.TIN_BLOCK, null, null),
    CHROMIUM_BLOCK(102, 50, BlockType.CHROMIUM_BLOCK, null, null),
    MAGNESIUM_BLOCK(103, 50, BlockType.MAGNESIUM_BLOCK, null, null),
    COBALT_BLOCK(104, 50, BlockType.COBALT_BLOCK, null, null),
    ARSENIC_BLOCK(105, 50, BlockType.ARSENIC_BLOCK, null, null),
    MANGANESE_BLOCK(106, 50, BlockType.MANGANESE_BLOCK, null, null),
    NIOBIUM_BLOCK(107, 50, BlockType.NIOBIUM_BLOCK, null, null),
    TANTALUM_BLOCK(108, 50, BlockType.TANTALUM_BLOCK, null, null),
    LEAD_BLOCK(109, 50, BlockType.LEAD_BLOCK, null, null),
    GOLD_BLOCK(110, 50, BlockType.GOLD_BLOCK, null, null),
    TITANIUM_BLOCK(111, 50, BlockType.TITANIUM_BLOCK, null, null),
    IRIDIUM_BLOCK(112, 50, BlockType.IRIDIUM_BLOCK, null, null),
    MOLYBDENUM_BLOCK(113, 50, BlockType.MOLYBDENUM_BLOCK, null, null),
    NICKEL_BLOCK(114, 50, BlockType.NICKEL_BLOCK, null, null),
    CALCIUM_BLOCK(115, 50, BlockType.CALCIUM_BLOCK, null, null),
    TUNGSTEN_BLOCK(116, 50, BlockType.TUNGSTEN_BLOCK, null, null),
    PLATINUM_BLOCK(117, 50, BlockType.PLATINUM_BLOCK, null, null),
    ZINC_BLOCK(118, 50, BlockType.ZINC_BLOCK, null, null),
    URANIUM_BLOCK(119, 50, BlockType.URANIUM_BLOCK, null, null),
    COAL_BLOCK(120, 50, BlockType.COAL_BLOCK, null, null),
    
    ALEXANDRITE_ORE(121, 5, BlockType.ALEXANDRITE_ORE, null, null),
    AMETHYST_ORE(122, 5, BlockType.AMETHYST_ORE, null, null),
    AQUAMARINE_ORE(123, 5, BlockType.AQUAMARINE_ORE, null, null),
    CITRINE_ORE(124, 5, BlockType.CITRINE_ORE, null, null),
    DIAMOND_ORE(125, 5, BlockType.DIAMOND_ORE, null, null),
    EMERALD_ORE(126, 5, BlockType.EMERALD_ORE, null, null),
    GARNET_ORE(127, 5, BlockType.GARNET_ORE, null, null),
    JADE_ORE(128, 5, BlockType.JADE_ORE, null, null),
    LAPIS_LAZULI_ORE(129, 5, BlockType.LAPIS_LAZULI_ORE, null, null),
    MOONSTONE_ORE(130, 5, BlockType.MOONSTONE_ORE, null, null),
    MORGANITE_ORE(131, 5, BlockType.MORGANITE_ORE, null, null),
    ONYX_ORE(132, 5, BlockType.ONYX_ORE, null, null),
    OPAL_ORE(133, 5, BlockType.OPAL_ORE, null, null),
    PEARL_ORE(134, 5, BlockType.PEARL_ORE, null, null),
    PERIDOT_ORE(135, 5, BlockType.PERIDOT_ORE, null, null),
    RUBELLITE_ORE(136, 5, BlockType.RUBELLITE_ORE, null, null),
    RUBY_ORE(137, 5, BlockType.RUBY_ORE, null, null),
    SAPPHIRE_ORE(138, 5, BlockType.SAPPHIRE_ORE, null, null),
    SPINEL_ORE(139, 5, BlockType.SPINEL_ORE, null, null),
    TANZANITE_ORE(140, 5, BlockType.TANZANITE_ORE, null, null),
    TOPAZ_ORE(141, 5, BlockType.TOPAZ_ORE, null, null),
    TOURMALINE_ORE(142, 5, BlockType.TOURMALINE_ORE, null, null),
    TURQUOISE_ORE(143, 5, BlockType.TURQUOISE_ORE, null, null),
    QUARTZ_ORE(144, 5, BlockType.QUARTZ_ORE, null, null),
    ZIRCON_ORE(145, 5, BlockType.ZIRCON_ORE, null, null),

    ALEXANDRITE_GEM(146, 1, null, null, null),
    AMETHYST_GEM(147, 1, null, null, null),
    AQUAMARINE_GEM(148, 1, null, null, null),
    CITRINE_GEM(149, 1, null, null, null),
    DIAMOND_GEM(150, 1, null, null, null),
    EMERALD_GEM(151, 1, null, null, null),
    GARNET_GEM(152, 1, null, null, null),
    JADE_GEM(153, 1, null, null, null),
    LAPIS_LAZULI_GEM(154, 1, null, null, null),
    MOONSTONE_GEM(155, 1, null, null, null),
    MORGANITE_GEM(156, 1, null, null, null),
    ONYX_GEM(157, 1, null, null, null),
    OPAL_GEM(158, 1, null, null, null),
    PEARL_GEM(159, 1, null, null, null),
    PERIDOT_GEM(160, 1, null, null, null),
    RUBELLITE_GEM(161, 1, null, null, null),
    RUBY_GEM(162, 1, null, null, null),
    SAPPHIRE_GEM(163, 1, null, null, null),
    SPINEL_GEM(164, 1, null, null, null),
    TANZANITE_GEM(165, 1, null, null, null),
    TOPAZ_GEM(166, 1, null, null, null),
    TOURMALINE_GEM(167, 1, null, null, null),
    TURQUOISE_GEM(168, 1, null, null, null),
    QUARTZ_GEM(169, 1, null, null, null),
    ZIRCON_GEM(170, 1, null, null, null),

    ALEXANDRITE_BLOCK(171, 50, BlockType.ALEXANDRITE_BLOCK, null, null),
    AMETHYST_BLOCK(172, 50, BlockType.AMETHYST_BLOCK, null, null),
    AQUAMARINE_BLOCK(173, 50, BlockType.AQUAMARINE_BLOCK, null, null),
    CITRINE_BLOCK(174, 50, BlockType.CITRINE_BLOCK, null, null),
    DIAMOND_BLOCK(175, 50, BlockType.DIAMOND_BLOCK, null, null),
    EMERALD_BLOCK(176, 50, BlockType.EMERALD_BLOCK, null, null),
    GARNET_BLOCK(177, 50, BlockType.GARNET_BLOCK, null, null),
    JADE_BLOCK(178, 50, BlockType.JADE_BLOCK, null, null),
    LAPIS_LAZULI_BLOCK(179, 50, BlockType.LAPIS_LAZULI_BLOCK, null, null),
    MOONSTONE_BLOCK(180, 50, BlockType.MOONSTONE_BLOCK, null, null),
    MORGANITE_BLOCK(181, 50, BlockType.MORGANITE_BLOCK, null, null),
    ONYX_BLOCK(182, 50, BlockType.ONYX_BLOCK, null, null),
    OPAL_BLOCK(183, 50, BlockType.OPAL_BLOCK, null, null),
    PEARL_BLOCK(184, 50, BlockType.PEARL_BLOCK, null, null),
    PERIDOT_BLOCK(185, 50, BlockType.PERIDOT_BLOCK, null, null),
    RUBELLITE_BLOCK(186, 50, BlockType.RUBELLITE_BLOCK, null, null),
    RUBY_BLOCK(187, 50, BlockType.RUBY_BLOCK, null, null),
    SAPPHIRE_BLOCK(188, 50, BlockType.SAPPHIRE_BLOCK, null, null),
    SPINEL_BLOCK(189, 50, BlockType.SPINEL_BLOCK, null, null),
    TANZANITE_BLOCK(190, 50, BlockType.TANZANITE_BLOCK, null, null),
    TOPAZ_BLOCK(191, 50, BlockType.TOPAZ_BLOCK, null, null),
    TOURMALINE_BLOCK(192, 50, BlockType.TOURMALINE_BLOCK, null, null),
    TURQUOISE_BLOCK(193, 50, BlockType.TURQUOISE_BLOCK, null, null),
    QUARTZ_BLOCK(194, 50, BlockType.QUARTZ_BLOCK, null, null),
    ZIRCON_BLOCK(195, 50, BlockType.ZIRCON_BLOCK, null, null),

    SILVER_INGOT(196, 5, null, null, null),
    SULFUR_INGOT(197, 5, null, null, null),
    BARIUM_INGOT(198, 5, null, null, null),
    ALUMINUM_INGOT(199, 5, null, null, null),
    BERYLLIUM_INGOT(200, 5, null, null, null),
    SILICON_INGOT(201, 5, null, null, null),
    COPPER_INGOT(202, 5, null, null, null),
    IRON_INGOT(203, 5, null, null, null),
    TIN_INGOT(204, 5, null, null, null),
    CHROMIUM_INGOT(205, 5, null, null, null),
    MAGNESIUM_INGOT(206, 5, null, null, null),
    MERCURY_INGOT(207, 5, null, null, null),
    COBALT_INGOT(208, 5, null, null, null),
    ARSENIC_INGOT(209, 5, null, null, null),
    NIOBIUM_INGOT(210, 5, null, null, null),
    TANTALUM_INGOT(211, 5, null, null, null),
    CALCIUM_INGOT(212, 5, null, null, null),
    LEAD_INGOT(213, 5, null, null, null),
    GOLD_INGOT(214, 5, null, null, null),
    TITANIUM_INGOT(215, 5, null, null, null),
    IRIDIUM_INGOT(216, 5, null, null, null),
    MOLYBDENUM_INGOT(217, 5, null, null, null),
    NICKEL_INGOT(218, 5, null, null, null),
    MANGANESE_INGOT(219, 5, null, null, null),
    TUNGSTEN_INGOT(220, 5, null, null, null),
    PLATINUM_INGOT(221, 5, null, null, null),
    ZINC_INGOT(222, 5, null, null, null),
    URANIUM_INGOT(223, 5, null, null, null),

    WOODEN_PICKAXE(224, 1, null, EquipmentType.MAIN_HAND, DamageType.PIERCING),
    STONE_PICKAXE(225, 2, null, EquipmentType.MAIN_HAND, DamageType.PIERCING),
    IRON_PICKAXE(226, 2, null, EquipmentType.MAIN_HAND, DamageType.PIERCING),
    QUARTZ_PICKAXE(227, 3, null, EquipmentType.MAIN_HAND, DamageType.PIERCING),
    JADE_PICKAXE(228, 3, null, EquipmentType.MAIN_HAND, DamageType.PIERCING),
    DIAMOND_PICKAXE(229, 4, null, EquipmentType.MAIN_HAND, DamageType.PIERCING),
    TUNGSTEN_PICKAXE(230, 5, null, EquipmentType.MAIN_HAND, DamageType.PIERCING),
*/
    ;
    public static final ItemType[] NONE = new ItemType[] {};
    public static final ItemType[] ANY = null;
/*    public static final ItemType[] WOODEN_PICKAXES = new ItemType[] { WOODEN_PICKAXE, STONE_PICKAXE, IRON_PICKAXE,
            QUARTZ_PICKAXE, JADE_PICKAXE, DIAMOND_PICKAXE, TUNGSTEN_PICKAXE };
    public static final ItemType[] STONE_PICKAXES = new ItemType[] { STONE_PICKAXE, IRON_PICKAXE, QUARTZ_PICKAXE,
            JADE_PICKAXE, DIAMOND_PICKAXE, TUNGSTEN_PICKAXE };
    public static final ItemType[] IRON_PICKAXES = new ItemType[] { IRON_PICKAXE, QUARTZ_PICKAXE, JADE_PICKAXE,
            DIAMOND_PICKAXE, TUNGSTEN_PICKAXE };
    public static final ItemType[] QUARTZ_PICKAXES = new ItemType[] { QUARTZ_PICKAXE, JADE_PICKAXE, DIAMOND_PICKAXE,
            TUNGSTEN_PICKAXE };
    public static final ItemType[] JADE_PICKAXES = new ItemType[] { JADE_PICKAXE, DIAMOND_PICKAXE, TUNGSTEN_PICKAXE };
    public static final ItemType[] DIAMOND_PICKAXES = new ItemType[] { DIAMOND_PICKAXE, TUNGSTEN_PICKAXE };
    public static final ItemType[] TUNGSTEN_PICKAXES = new ItemType[] { TUNGSTEN_PICKAXE };*/

    private final int id;
    private final int weight;
    private final BlockType placeableBlock;
    private final EquipmentType equipmentType;
    private final DamageType damageType;

    ItemType(final int id, final int weight, final BlockType placeableBlock, final EquipmentType equipmentType,
             final DamageType damageType) {
        this.id = id;
        this.weight = weight;
        this.placeableBlock = placeableBlock;
        this.equipmentType = equipmentType;
        this.damageType = damageType;
    }



    public int getId() {
        return id;
    }

    public int getWeight() {
        return weight;
    }

    public BlockType getPlaceableBlock() {
        return placeableBlock;
    }

    public EquipmentType getEquipmentType() {
        return equipmentType;
    }

    public DamageType getDamageType() {
        return damageType;
    }

    @Override
    public int toValue() {
        return id;
    }
}
