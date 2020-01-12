package com.runetide.common.domain;

public enum CraftingRecipe {
    /*ALEXANDRITE_BLOCK(
            "AAAAA",
            "AAAAA",
            "AAAAA",
            "AAAAA",
            "AAAAA",
            ItemType.ALEXANDRITE_BLOCK, 1, "A", ItemType.ALEXANDRITE_GEM),
    AMETHYST_BLOCK(
            "AAAAA",
            "AAAAA",
            "AAAAA",
            "AAAAA",
            "AAAAA",
            ItemType.AMETHYST_BLOCK, 1, "A", ItemType.AMETHYST_GEM),
    AQUAMARINE_BLOCK(
            "AAAAA",
            "AAAAA",
            "AAAAA",
            "AAAAA",
            "AAAAA",
            ItemType.AQUAMARINE_BLOCK, 1, "A", ItemType.AQUAMARINE_GEM),
    WOODEN_PICKAXE(
            " WWW ",
            "W | W",
            "  |  ",
            "  |  ",
            "  |  ",
            ItemType.WOODEN_PICKAXE, 1, "W|", ItemType.WOODEN_PLANK, ItemType.STICK)*/

    ;
    /*private final ItemType[] grid;
    private final ItemType output;
    private final int count;*/

    CraftingRecipe(final String row1, final String row2, final String row3, final String row4, final String row5,
                   final ItemType output, final int count, final String legend, final ItemType... key) {

    }
}
