package com.runetide.common.domain;

public enum ItemType {
    TALC(0, 5, BlockType.TALC, null, null),
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
    ANDESITE(14, 50, BlockType.ANDESITE, null, null),

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
    BROKEN_ANDESITE(29, 50, BlockType.BROKEN_ANDESITE, null, null);
    public static final ItemType[] NONE = new ItemType[] {};
    public static final ItemType[] ANY = null;
    public static final ItemType[] WOODEN_PICKAXES = new ItemType[]{WOODEN_PICKAXE, STONE_PICKAXE};
    public static final ItemType[] STONE_PICKAXES = new ItemType[] {/* FIXME */};

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
}
