package com.runetide.common.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum BlockType {
    AIR(0, 0, 0, true, null, null),

    // Rocks
    TALC(1, 20, 0, false, ItemType.BROKEN_TALC, ItemType.WOODEN_PICKAXES),
    GYPSUM(2, 80, 0, false, ItemType.BROKEN_GYPSUM, ItemType.STONE_PICKAXES),
    CALCITE(3, 100, 0, false, ItemType.BROKEN_CALCITE, ItemType.STONE_PICKAXES),
    LIMESTONE(4, 140, 0, false, ItemType.BROKEN_LIMESTONE, ItemType.STONE_PICKAXES),
    SANDSTONE(5, 180, 0, false, ItemType.BROKEN_SANDSTONE, ItemType.IRON_PICKAXES),
    MARBLE(6, 200, 0, false, ItemType.BROKEN_MARBLE, ItemType.IRON_PICKAXES),
    APATITE(7, 240, 0, false, ItemType.BROKEN_APATITE, ItemType.IRON_PICKAXES),
    BASALT(8, 280, 0, false, ItemType.BROKEN_BASALT, ItemType.QUARTZ_PICKAXES),
    OBSIDIAN(9, 300, 0, false, ItemType.BROKEN_OBSIDIAN, ItemType.QUARTZ_PICKAXES),
    FELDSPAR(10, 350, 0, false, ItemType.BROKEN_FELDSPAR, ItemType.QUARTZ_PICKAXES),
    PUMICE(11, 400, 0, false, ItemType.BROKEN_PUMICE, ItemType.JADE_PICKAXES),
    GRANITE(12, 500, 0, false, ItemType.BROKEN_GRANITE, ItemType.JADE_PICKAXES),
    RHYOLITE(13, 750, 0, false, ItemType.BROKEN_RHYOLITE, ItemType.JADE_PICKAXES),
    DIORITE(14, 1000, 0, false, ItemType.BROKEN_DIORITE, ItemType.DIAMOND_PICKAXES),
    ANDESITE(15, 5000, 0, false, ItemType.BROKEN_ANDESITE, ItemType.DIAMOND_PICKAXES),
    BEDROCK(16, 1000000, 0, false, null, ItemType.NONE),

    // Drops from above
    BROKEN_TALC(17, 20, 0, false, ItemType.BROKEN_TALC, ItemType.ANY),
    BROKEN_GYPSUM(18, 40, 0, false, ItemType.BROKEN_GYPSUM, ItemType.ANY),
    BROKEN_CALCITE(19, 60, 0, false, ItemType.BROKEN_CALCITE, ItemType.ANY),
    BROKEN_LIMESTONE(20, 80, 0, false, ItemType.BROKEN_LIMESTONE, ItemType.ANY),
    BROKEN_SANDSTONE(21, 90, 0, false, ItemType.BROKEN_SANDSTONE, ItemType.ANY),
    BROKEN_MARBLE(22, 100, 0, false, ItemType.BROKEN_MARBLE, ItemType.ANY),
    BROKEN_APATITE(23, 120, 0, false, ItemType.BROKEN_APATITE, ItemType.ANY),
    BROKEN_BASALT(24, 140, 0, false, ItemType.BROKEN_BASALT, ItemType.ANY),
    BROKEN_OBSIDIAN(25, 150, 0, false, ItemType.BROKEN_OBSIDIAN, ItemType.ANY),
    BROKEN_FELDSPAR(26, 175, 0, false, ItemType.BROKEN_FELDSPAR, ItemType.ANY),
    BROKEN_PUMICE(27, 200, 0, false, ItemType.BROKEN_PUMICE, ItemType.ANY),
    BROKEN_GRANITE(28, 250, 0, false, ItemType.BROKEN_GRANITE, ItemType.ANY),
    BROKEN_RHYOLITE(29, 375, 0, false, ItemType.BROKEN_RHYOLITE, ItemType.ANY),
    BROKEN_DIORITE(30, 500, 0, false, ItemType.BROKEN_DIORITE, ItemType.ANY),
    BROKEN_ANDESITE(31, 1000, 0, false, ItemType.BROKEN_ANDESITE, ItemType.ANY),

    // Other natural generation
    DIRT(32, 5, 0, false, ItemType.DIRT, ItemType.SHOVELS),
    GRASS(33, 10, 0, false, ItemType.DIRT, ItemType.SHOVELS),
    GRAVEL(34, 20, 0, false, ItemType.GRAVEL, ItemType.SHOVELS),
    SAND(35, 5, 0, false, ItemType.SAND, ItemType.SHOVELS),
    LOG(36, 20, 0, false, ItemType.LOG, ItemType.ANY),
    LEAVES(37, 10, 0, false, null, ItemType.ANY),
    CLAY(38, 20, 0, false, ItemType.CLAY, ItemType.SHOVELS),
    LEYLINE(39, 1000000, 0, false, null, ItemType.NONE),

    // Liquids
    WATER(40, 100, 0, false, null, ItemType.NONE),
    LAVA(41, 100, 0, false, null, ItemType.NONE),
    OIL(42, 100, 0, false, null, ItemType.NONE),
    MERCURY(43, 100, 0, false, null, ItemType.NONE),

    // Ores
    ANTHITE(44, 100, 0, false, null, null),
    BARITE(45, 100, 0, false, null, null),
    BAUXITE(46, 100, 0, false, null, null),
    BERYL(47, 100, 0, false, null, null),
    BORNITE(48, 100, 0, false, null, null),
    CASSITERITE(49, 100, 0, false, null, null),
    CHALCOCITE(50, 100, 0, false, null, null),
    CHALCOPYRITE(51, 100, 0, false, null, null),
    CHROMITE(52, 100, 0, false, null, null),
    CINNABAR(53, 100, 0, false, null, null),
    COAL_ORE(54, 100, 0, false, null, null),
    COBALTITE(55, 100, 0, false, null, null),
    COLTAN(56, 100, 0, false, null, null),
    DOLOMITE(57, 100, 0, false, null, null),
    GALENA(58, 100, 0, false, null, null),
    GOLD_ORE(59, 100, 0, false, null, null),
    HEMATITE(60, 100, 0, false, null, null),
    ILMENITE(61, 100, 0, false, null, null),
    IRIDIUM_ORE(62, 100, 0, false, null, null),
    MAGNETITE(63, 100, 0, false, null, null),
    MALACHITE(64, 100, 0, false, null, null),
    MOLYBDENITE(65, 100, 0, false, null, null),
    PENTLANDITE(66, 100, 0, false, null, null),
    PYROLUSITE(67, 100, 0, false, null, null),
    SCHEELITE(68, 100, 0, false, null, null),
    SPERRYLITE(69, 100, 0, false, null, null),
    SPHALERITE(70, 100, 0, false, null, null),
    URANINITE(71, 100, 0, false, null, null),
    WOLFRAMITE(72, 100, 0, false, null, null),

    // Drops from above
    BROKEN_ANTHITE(73, 100, 0, false, null, null),
    BROKEN_BARITE(74, 100, 0, false, null, null),
    BROKEN_BAUXITE(75, 100, 0, false, null, null),
    BROKEN_BERYL(76, 100, 0, false, null, null),
    BROKEN_BORNITE(77, 100, 0, false, null, null),
    BROKEN_CASSITERITE(78, 100, 0, false, null, null),
    BROKEN_CHALCOCITE(79, 100, 0, false, null, null),
    BROKEN_CHALCOPYRITE(80, 100, 0, false, null, null),
    BROKEN_CHROMITE(81, 100, 0, false, null, null),
    BROKEN_CINNABAR(82, 100, 0, false, null, null),
    BROKEN_COAL_ORE(83, 100, 0, false, null, null),
    BROKEN_COBALTITE(84, 100, 0, false, null, null),
    BROKEN_COLTAN(85, 100, 0, false, null, null),
    BROKEN_DOLOMITE(86, 100, 0, false, null, null),
    BROKEN_GALENA(87, 100, 0, false, null, null),
    BROKEN_GOLD_ORE(88, 100, 0, false, null, null),
    BROKEN_HEMATITE(89, 100, 0, false, null, null),
    BROKEN_ILMENITE(90, 100, 0, false, null, null),
    BROKEN_IRIDIUM_ORE(91, 100, 0, false, null, null),
    BROKEN_MAGNETITE(92, 100, 0, false, null, null),
    BROKEN_MALACHITE(93, 100, 0, false, null, null),
    BROKEN_MOLYBDENITE(94, 100, 0, false, null, null),
    BROKEN_PENTLANDITE(95, 100, 0, false, null, null),
    BROKEN_PYROLUSITE(96, 100, 0, false, null, null),
    BROKEN_SCHEELITE(97, 100, 0, false, null, null),
    BROKEN_SPERRYLITE(98, 100, 0, false, null, null),
    BROKEN_SPHALERITE(99, 100, 0, false, null, null),
    BROKEN_URANINITE(100, 100, 0, false, null, null),
    BROKEN_WOLFRAMITE(101, 100, 0, false, null, null),

    // Purified metal blocks
    SILVER_BLOCK(102, 100, 0, false, null, null),
    SULFUR_BLOCK(103, 100, 0, false, null, null),
    BARIUM_BLOCK(104, 100, 0, false, null, null),
    ALUMINUM_BLOCK(105, 100, 0, false, null, null),
    BERYLLIUM_BLOCK(106, 100, 0, false, null, null),
    SILICON_BLOCK(107, 100, 0, false, null, null),
    COPPER_BLOCK(108, 100, 0, false, null, null),
    IRON_BLOCK(109, 100, 0, false, null, null),
    TIN_BLOCK(110, 100, 0, false, null, null),
    CHROMIUM_BLOCK(111, 100, 0, false, null, null),
    MAGNESIUM_BLOCK(112, 100, 0, false, null, null),
    COBALT_BLOCK(113, 100, 0, false, null, null),
    ARSENIC_BLOCK(114, 100, 0, false, null, null),
    MANGANESE_BLOCK(115, 100, 0, false, null, null),
    NIOBIUM_BLOCK(116, 100, 0, false, null, null),
    TANTALUM_BLOCK(117, 100, 0, false, null, null),
    LEAD_BLOCK(118, 100, 0, false, null, null),
    GOLD_BLOCK(119, 100, 0, false, null, null),
    TITANIUM_BLOCK(120, 100, 0, false, null, null),
    IRIDIUM_BLOCK(121, 100, 0, false, null, null),
    MOLYBDENUM_BLOCK(122, 100, 0, false, null, null),
    NICKEL_BLOCK(123, 100, 0, false, null, null),
    CALCIUM_BLOCK(124, 100, 0, false, null, null),
    TUNGSTEN_BLOCK(125, 100, 0, false, null, null),
    PLATINUM_BLOCK(126, 100, 0, false, null, null),
    ZINC_BLOCK(127, 100, 0, false, null, null),
    URANIUM_BLOCK(128, 100, 0, false, null, null),
    COAL_BLOCK(129, 100, 0, false, null, null),

    // Gemstones
    ALEXANDRITE_ORE(130, 100, 0, false, null, null),
    AMETHYST_ORE(131, 100, 0, false, null, null),
    AQUAMARINE_ORE(132, 100, 0, false, null, null),
    CITRINE_ORE(133, 100, 0, false, null, null),
    DIAMOND_ORE(134, 100, 0, false, null, null),
    EMERALD_ORE(135, 100, 0, false, null, null),
    GARNET_ORE(136, 100, 0, false, null, null),
    JADE_ORE(137, 100, 0, false, null, null),
    LAPIS_LAZULI_ORE(138, 100, 0, false, null, null),
    MOONSTONE_ORE(139, 100, 0, false, null, null),
    MORGANITE_ORE(140, 100, 0, false, null, null),
    ONYX_ORE(141, 100, 0, false, null, null),
    OPAL_ORE(142, 100, 0, false, null, null),
    PEARL_ORE(143, 100, 0, false, null, null),
    PERIDOT_ORE(144, 100, 0, false, null, null),
    RUBELLITE_ORE(145, 100, 0, false, null, null),
    RUBY_ORE(146, 100, 0, false, null, null),
    SAPPHIRE_ORE(147, 100, 0, false, null, null),
    SPINEL_ORE(148, 100, 0, false, null, null),
    TANZANITE_ORE(149, 100, 0, false, null, null),
    TOPAZ_ORE(150, 100, 0, false, null, null),
    TOURMALINE_ORE(151, 100, 0, false, null, null),
    TURQUOISE_ORE(152, 100, 0, false, null, null),
    QUARTZ_ORE(153, 100, 0, false, null, null),
    ZIRCON_ORE(154, 100, 0, false, null, null),

    // Polished gemstone blocks
    ALEXANDRITE_BLOCK(155, 100, 0, false, null, null),
    AMETHYST_BLOCK(156, 100, 0, false, null, null),
    AQUAMARINE_BLOCK(157, 100, 0, false, null, null),
    CITRINE_BLOCK(158, 100, 0, false, null, null),
    DIAMOND_BLOCK(159, 100, 0, false, null, null),
    EMERALD_BLOCK(160, 100, 0, false, null, null),
    GARNET_BLOCK(161, 100, 0, false, null, null),
    JADE_BLOCK(162, 100, 0, false, null, null),
    LAPIS_LAZULI_BLOCK(163, 100, 0, false, null, null),
    MOONSTONE_BLOCK(164, 100, 0, false, null, null),
    MORGANITE_BLOCK(165, 100, 0, false, null, null),
    ONYX_BLOCK(166, 100, 0, false, null, null),
    OPAL_BLOCK(167, 100, 0, false, null, null),
    PEARL_BLOCK(168, 100, 0, false, null, null),
    PERIDOT_BLOCK(169, 100, 0, false, null, null),
    RUBELLITE_BLOCK(170, 100, 0, false, null, null),
    RUBY_BLOCK(171, 100, 0, false, null, null),
    SAPPHIRE_BLOCK(172, 100, 0, false, null, null),
    SPINEL_BLOCK(173, 100, 0, false, null, null),
    TANZANITE_BLOCK(174, 100, 0, false, null, null),
    TOPAZ_BLOCK(175, 100, 0, false, null, null),
    TOURMALINE_BLOCK(176, 100, 0, false, null, null),
    TURQUOISE_BLOCK(177, 100, 0, false, null, null),
    QUARTZ_BLOCK(178, 100, 0, false, null, null),
    ZIRCON_BLOCK(179, 100, 0, false, null, null),

    // Wooden craftables
    PLANKS(180, 100, 0, false, null, null),
    STAIRS(181, 100, 0, false, null, null),
    FENCE(182, 100, 0, true, null, null),
    LADDER(183, 100, 0, true, null, null),
    WOODEN_SLAB(184, 100, 0, true, null, null),
    DOOR(185, 100, 0, false, null, null),

    // Storage
    WOODEN_CHEST(186, 100, 0, false, null, null),
    STONE_CHEST(187, 100, 0, false, null, null),
    DIAMOND_CHEST(188, 100, 0, false, null, null),
    RUBY_CHEST(189, 100, 0, false, null, null),
    SAPPHIRE_CHEST(190, 100, 0, false, null, null),
    TITANIUM_CHEST(191, 100, 0, false, null, null),
    TUNGSTEN_CHEST(192, 100, 0, false, null, null),
    WOODEN_BOWL(193, 100, 0, false, null, null),
    STONE_TANK(194, 100, 0, false, null, null),
    DIAMOND_TANK(195, 100, 0, false, null, null),
    RUBY_TANK(196, 100, 0, false, null, null),
    SAPPHIRE_TANK(197, 100, 0, false, null, null),
    TITANIUM_TANK(198, 100, 0, false, null, null),
    TUNGSTEN_TANK(199, 100, 0, false, null, null),

    // Logistics
    COMBUSTION_ENGINE(200, 100, 0, false, null, null),
    ELECTRIC_ENGINE(201, 100, 0, false, null, null),
    ETHERIC_ENGINE(202, 100, 0, false, null, null),
    ITEM_PIPE(203, 100, 0, false, null, null),
    LIQUID_PIPE(204, 100, 0, false, null, null),
    VACUUM_PIPE(205, 100, 0, false, null, null),
    ETHER_PIPE(206, 100, 0, false, null, null),

    // Machines
    FURNACE(207, 100, 0, false, null, null),
    GRINDER(208, 100, 0, false, null, null),
    CRAFTING_TABLE(209, 100, 0, false, null, null),
    AUTOMATED_CRAFTING_TABLE(210, 100, 0, false, null, null),
    COIN_PRESS(211, 100, 0, false, null, null),
    STORAGE_COMPUTER(212, 100, 0, false, null, null),
    CIRCUIT_DESIGNER(213, 100, 0, false, null, null),
    INTEGRATED_CIRCUIT(214, 100, 0, false, null, null),
    CIRCUIT_WIRE(215, 100, 0, false, null, null),
    COAL_GENERATOR(216, 100, 0, false, null, null),
    PNEUMATIC_GENERATOR(217, 100, 0, false, null, null),
    SOLAR_PANEL(218, 100, 0, false, null, null),
    REFINERY(219, 100, 0, false, null, null),
    AUTOMATED_FARMING_UNIT(220, 100, 0, false, null, null),
    PISTON(221, 100, 0, false, null, null),
    SHIELD(222, 100, 0, false, null, null),
    QUARRY(223, 100, 0, false, null, null),
    PUMP(224, 100, 0, false, null, null),
    DIGITAL_MARKET_ADAPTER(225, 100, 0, false, null, null),
    DIGITAL_BANK_ADAPTER(226, 100, 0, false, null, null),
    WIRELESS_STORAGE_DEVICE(227, 100, 0, false, null, null),
    TESSERACT(228, 100, 0, false, null, null),
    TELEPORTER(229, 100, 0, false, null, null),
    LEYLINE_ETHER_TAP(230, 100, 0, false, null, null),
    MILL(231, 100, 0, false, null, null);

    private static final Map<Integer, BlockType> map = new HashMap<>();

    static {
        for(final BlockType blockType : values()) {
            if(map.containsKey(blockType.id))
                throw new IllegalStateException();
            map.put(blockType.id, blockType);
        }
    }

    @JsonCreator
    public static BlockType fromId(final int id) {
        return map.get(id);
    }

    private final int id;
    private final int hardness;
    private final int lightProduced;
    private final boolean transparent;
    private final ItemType breakItem;
    private final ItemType[] breakableBy;

    BlockType(final int id, final int hardness, final int lightProduced, final boolean transparent,
              final ItemType breakItem, final ItemType[] breakableBy) {
        this.id = id;
        this.hardness = hardness;
        this.lightProduced = lightProduced;
        this.transparent = transparent;
        this.breakItem = breakItem;
        this.breakableBy = breakableBy;
    }

    @JsonValue
    public int toValue() {
        return id;
    }

    public boolean isTransparent() {
        return transparent;
    }
}
