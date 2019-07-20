package com.runetide.services.internal.region.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum BlockType {
    AIR(0, true),

    // Rocks
    TALC(1, false),
    GYPSUM(2, false),
    CALCITE(3, false),
    LIMESTONE(4, false),
    SANDSTONE(5, false),
    MARBLE(6, false),
    APATITE(7, false),
    BASALT(8, false),
    OBSIDIAN(9, false),
    FELDSPAR(10, false),
    PUMICE(11, false),
    GRANITE(12, false),
    RHYOLITE(13, false),
    DIORITE(14, false),
    ANDESITE(15, false),
    BEDROCK(16, false),

    // Drops from above
    BROKEN_TALC(17, false),
    BROKEN_GYPSUM(18, false),
    BROKEN_CALCITE(19, false),
    BROKEN_LIMESTONE(20, false),
    BROKEN_SANDSTONE(21, false),
    BROKEN_MARBLE(22, false),
    BROKEN_APATITE(23, false),
    BROKEN_BASALT(24, false),
    BROKEN_OBSIDIAN(25, false),
    BROKEN_FELDSPAR(26, false),
    BROKEN_PUMICE(27, false),
    BROKEN_GRANITE(28, false),
    BROKEN_RHYOLITE(29, false),
    BROKEN_DIORITE(30, false),
    BROKEN_ANDESITE(31, false),

    // Other natural generation
    DIRT(32, false),
    GRASS(33, false),
    GRAVEL(34, false),
    SAND(35, false),
    LOG(36, false),
    LEAVES(37, false),
    CLAY(38, false),
    LEYLINE(39, false),

    // Liquids
    WATER(40, false),
    LAVA(41, false),
    OIL(42, false),
    MERCURY(43, false),

    // Ores
    ANTHITE(44, false),
    BARITE(45, false),
    BAUXITE(46, false),
    BERYL(47, false),
    BORNITE(48, false),
    CASSITERITE(49, false),
    CHALCOCITE(50, false),
    CHALCOPYRITE(51, false),
    CHROMITE(52, false),
    CINNABAR(53, false),
    COAL_ORE(54, false),
    COBALTITE(55, false),
    COLTAN(56, false),
    DOLOMITE(57, false),
    GALENA(58, false),
    GOLD_ORE(59, false),
    HEMATITE(60, false),
    ILMENITE(61, false),
    IRIDIUM_ORE(62, false),
    MAGNETITE(63, false),
    MALACHITE(64, false),
    MOLYBDENITE(65, false),
    PENTLANDITE(66, false),
    PYROLUSITE(67, false),
    SCHEELITE(68, false),
    SPERRYLITE(69, false),
    SPHALERITE(70, false),
    URANINITE(71, false),
    WOLFRAMITE(72, false),

    // Drops from above
    BROKEN_ANTHITE(73, false),
    BROKEN_BARITE(74, false),
    BROKEN_BAUXITE(75, false),
    BROKEN_BERYL(76, false),
    BROKEN_BORNITE(77, false),
    BROKEN_CASSITERITE(78, false),
    BROKEN_CHALCOCITE(79, false),
    BROKEN_CHALCOPYRITE(80, false),
    BROKEN_CHROMITE(81, false),
    BROKEN_CINNABAR(82, false),
    BROKEN_COAL_ORE(83, false),
    BROKEN_COBALTITE(84, false),
    BROKEN_COLTAN(85, false),
    BROKEN_DOLOMITE(86, false),
    BROKEN_GALENA(87, false),
    BROKEN_GOLD_ORE(88, false),
    BROKEN_HEMATITE(89, false),
    BROKEN_ILMENITE(90, false),
    BROKEN_IRIDIUM_ORE(91, false),
    BROKEN_MAGNETITE(92, false),
    BROKEN_MALACHITE(93, false),
    BROKEN_MOLYBDENITE(94, false),
    BROKEN_PENTLANDITE(95, false),
    BROKEN_PYROLUSITE(96, false),
    BROKEN_SCHEELITE(97, false),
    BROKEN_SPERRYLITE(98, false),
    BROKEN_SPHALERITE(99, false),
    BROKEN_URANINITE(100, false),
    BROKEN_WOLFRAMITE(101, false),

    // Purified metal blocks
    SILVER_BLOCK(102, false),
    SULFUR_BLOCK(103, false),
    BARIUM_BLOCK(104, false),
    ALUMINUM_BLOCK(105, false),
    BERYLLIUM_BLOCK(106, false),
    SILICON_BLOCK(107, false),
    COPPER_BLOCK(108, false),
    IRON_BLOCK(109, false),
    TIN_BLOCK(110, false),
    CHROMIUM_BLOCK(111, false),
    MAGNESIUM_BLOCK(112, false),
    COBALT_BLOCK(113, false),
    ARSENIC_BLOCK(114, false),
    MANGANESE_BLOCK(115, false),
    NIOBIUM_BLOCK(116, false),
    TANTALUM_BLOCK(117, false),
    LEAD_BLOCK(118, false),
    GOLD_BLOCK(119, false),
    TITANIUM_BLOCK(120, false),
    IRIDIUM_BLOCK(121, false),
    MOLYBDENUM_BLOCK(122, false),
    NICKEL_BLOCK(123, false),
    CALCIUM_BLOCK(124, false),
    TUNGSTEN_BLOCK(125, false),
    PLATINUM_BLOCK(126, false),
    ZINC_BLOCK(127, false),
    URANIUM_BLOCK(128, false),
    COAL_BLOCK(129, false),

    // Gemstones
    ALEXANDRITE_ORE(130, false),
    AMETHYST_ORE(131, false),
    AQUAMARINE_ORE(132, false),
    CITRINE_ORE(133, false),
    DIAMOND_ORE(134, false),
    EMERALD_ORE(135, false),
    GARNET_ORE(136, false),
    JADE_ORE(137, false),
    LAPIS_LAZULI_ORE(138, false),
    MOONSTONE_ORE(139, false),
    MORGANITE_ORE(140, false),
    ONYX_ORE(141, false),
    OPAL_ORE(142, false),
    PEARL_ORE(143, false),
    PERIDOT_ORE(144, false),
    RUBELLITE_ORE(145, false),
    RUBY_ORE(146, false),
    SAPPHIRE_ORE(147, false),
    SPINEL_ORE(148, false),
    TANZANITE_ORE(149, false),
    TOPAZ_ORE(150, false),
    TOURMALINE_ORE(151, false),
    TURQUOISE_ORE(152, false),
    QUARTZ_ORE(153, false),
    ZIRCON_ORE(154, false),

    // Polished gemstone blocks
    ALEXANDRITE_BLOCK(155, false),
    AMETHYST_BLOCK(156, false),
    AQUAMARINE_BLOCK(157, false),
    CITRINE_BLOCK(158, false),
    DIAMOND_BLOCK(159, false),
    EMERALD_BLOCK(160, false),
    GARNET_BLOCK(161, false),
    JADE_BLOCK(162, false),
    LAPIS_LAZULI_BLOCK(163, false),
    MOONSTONE_BLOCK(164, false),
    MORGANITE_BLOCK(165, false),
    ONYX_BLOCK(166, false),
    OPAL_BLOCK(167, false),
    PEARL_BLOCK(168, false),
    PERIDOT_BLOCK(169, false),
    RUBELLITE_BLOCK(170, false),
    RUBY_BLOCK(171, false),
    SAPPHIRE_BLOCK(172, false),
    SPINEL_BLOCK(173, false),
    TANZANITE_BLOCK(174, false),
    TOPAZ_BLOCK(175, false),
    TOURMALINE_BLOCK(176, false),
    TURQUOISE_BLOCK(177, false),
    QUARTZ_BLOCK(178, false),
    ZIRCON_BLOCK(179, false),

    // Wooden craftables
    PLANKS(180, false),
    STAIRS(181, false),
    FENCE(182, true),
    LADDER(183, true),
    WOODEN_SLAB(184, true),
    DOOR(185, false),

    // Storage
    WOODEN_CHEST(186, false),
    STONE_CHEST(187, false),
    DIAMOND_CHEST(188, false),
    RUBY_CHEST(189, false),
    SAPPHIRE_CHEST(190, false),
    TITANIUM_CHEST(191, false),
    TUNGSTEN_CHEST(192, false),
    WOODEN_BOWL(193, false),
    STONE_TANK(194, false),
    DIAMOND_TANK(195, false),
    RUBY_TANK(196, false),
    SAPPHIRE_TANK(197, false),
    TITANIUM_TANK(198, false),
    TUNGSTEN_TANK(199, false),

    // Logistics
    COMBUSTION_ENGINE(200, false),
    ELECTRIC_ENGINE(201, false),
    ETHERIC_ENGINE(202, false),
    ITEM_PIPE(203, false),
    LIQUID_PIPE(204, false),
    VACUUM_PIPE(205, false),
    ETHER_PIPE(206, false),

    // Machines
    FURNACE(207, false),
    GRINDER(208, false),
    CRAFTING_TABLE(209, false),
    AUTOMATED_CRAFTING_TABLE(210, false),
    COIN_PRESS(211, false),
    STORAGE_COMPUTER(212, false),
    CIRCUIT_DESIGNER(213, false),
    INTEGRATED_CIRCUIT(214, false),
    CIRCUIT_WIRE(215, false),
    COAL_GENERATOR(216, false),
    PNEUMATIC_GENERATOR(217, false),
    SOLAR_PANEL(218, false),
    REFINERY(219, false),
    AUTOMATED_FARMING_UNIT(220, false),
    PISTON(221, false),
    SHIELD(222, false),
    QUARRY(223, false),
    PUMP(224, false),
    DIGITAL_MARKET_ADAPTER(225, false),
    DIGITAL_BANK_ADAPTER(226, false),
    WIRELESS_STORAGE_DEVICE(227, false),
    TESSERACT(228, false),
    TELEPORTER(229, false),
    LEYLINE_ETHER_TAP(230, false);

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
    private final boolean transparent;

    BlockType(final int id, final boolean transparent) {
        this.id = id;
        this.transparent = transparent;
    }

    @JsonValue
    public int toValue() {
        return id;
    }

    public boolean isTransparent() {
        return transparent;
    }
}
