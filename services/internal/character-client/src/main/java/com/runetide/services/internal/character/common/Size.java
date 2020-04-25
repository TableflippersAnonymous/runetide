package com.runetide.services.internal.character.common;

import com.fasterxml.jackson.annotation.JsonValue;
import com.runetide.common.Constants;
import com.runetide.common.domain.IndexedEnum;

public enum Size implements IndexedEnum {
    TINY(0, 32, Constants.OFFSETS_PER_BLOCK_Y/2, 40.0, Constants.OFFSETS_PER_BLOCK_Y),
    SMALL(1, Constants.OFFSETS_PER_BLOCK_Y/2, Constants.OFFSETS_PER_BLOCK_Y, 40.0, Constants.OFFSETS_PER_BLOCK_Y * 3/2),
    MEDIUM(2, Constants.OFFSETS_PER_BLOCK_Y, Constants.OFFSETS_PER_BLOCK_Y * 2, 80.0, Constants.OFFSETS_PER_BLOCK_Y * 3),
    LARGE(3, Constants.OFFSETS_PER_BLOCK_Y * 3, Constants.OFFSETS_PER_BLOCK_Y * 4, 140.0, Constants.OFFSETS_PER_BLOCK_Y * 6),
    HUGE(4, Constants.OFFSETS_PER_BLOCK_Y * 6, Constants.OFFSETS_PER_BLOCK_Y * 8, 280.0, Constants.OFFSETS_PER_BLOCK_Y * 12),
    GARGANTUAN(5, Constants.OFFSETS_PER_BLOCK_Y * 12, Constants.OFFSETS_PER_BLOCK_Y * 24, 512.0, Constants.OFFSETS_PER_BLOCK_Y * 32),
    COLOSSAL(6, Constants.OFFSETS_PER_BLOCK_Y * 32, Constants.OFFSETS_PER_BLOCK_Y * 128, 8192.0, Constants.OFFSETS_PER_BLOCK_Y * 256);

    private final int id;
    private final int minSize;
    private final int meanSize;
    private final double stdDev;
    private final int maxSize;

    Size(int id, int minSize, int meanSize, double stdDev, int maxSize) {
        this.id = id;
        this.minSize = minSize;
        this.meanSize = meanSize;
        this.stdDev = stdDev;
        this.maxSize = maxSize;
    }

    @Override
    @JsonValue
    public int toValue() {
        return id;
    }
}
