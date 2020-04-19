package com.runetide.services.internal.xp.common;

import java.util.Arrays;

public enum XPType {
    CHARACTER(100, 300, 1.1, 20, 2, 1.05),
    MONSTER(150, 300, 1.1, 20, 2, 1.05),
    SKILL_FIGHTING(100, 30, 1.1),
    SKILL_SURVIVAL(100, 30, 1.1),
    SKILL_MAGIC(100, 30, 1.1),
    SKILL_GATHERING(100, 100, 1.04),
    SKILL_MINING(100, 500, 1.04),
    SKILL_INTIMIDATION(100, 30, 1.08),
    SKILL_DECEPTION(100, 30, 1.08),
    SKILL_PERSUASION(100, 30, 1.08),
    SKILL_STEALTH(100, 30, 1.1),
    SKILL_SLEIGHT_OF_HAND(100, 100, 1.02),
    SKILL_PERFORMANCE(100, 100, 1.01),
    SKILL_PERCEPTION(100, 30, 1.1),
    SKILL_MEDICINE(100, 30, 1.1),
    SKILL_INVESTIGATION(100, 50, 1.05),
    SKILL_ANIMAL_HANDLING(100, 3, 1.1),
    SKILL_ATHLETICS(100, 30, 1.08),
    SKILL_ACROBATICS(100, 30, 1.08),
    SKILL_CRAFTING(100, 30, 1.08),
    SKILL_MECHANICS(100, 30, 1.08),
    SKILL_LEADERSHIP(100, 30, 1.1),
    SKILL_COOKING(100, 30, 1.04),
    SKILL_FISHING(100, 30, 1.04),
    SKILL_LOCK_PICKING(100, 30, 1.02)
    ;

    private final int levelCap;
    private final long[] xpByLevel;
    private final long[] deathXpByLevel;

    XPType(int levelCap, int xpAdder, double xpMultiplier) {
        this(levelCap, xpAdder, xpMultiplier, 0, 0, 0);
    }

    XPType(int levelCap, int xpAdder, double xpMultiplier, int ktlBase, int ktlAdder, double ktlMultiplier) {
        this.levelCap = levelCap;
        xpByLevel = new long[levelCap - 1];
        for(long idx = 0, xp = xpAdder; idx < levelCap - 1; idx++, xp += xp * xpMultiplier + xpAdder)
            xpByLevel[(int) idx] = xp;
        deathXpByLevel = new long[levelCap];
        for(long idx = 0, ktl = ktlBase; idx < levelCap; idx++, ktl += ktl * ktlMultiplier + ktlAdder)
            deathXpByLevel[(int) idx] = getXpByLevel((int) idx + 1) / ktl;
    }

    public int getLevelByXp(long xp) {
        final int result = Arrays.binarySearch(xpByLevel, xp);
        if(result < 0)
            return -result;
        return result + 2;
    }

    public long getXpByLevel(int level) {
        final int idx = level - 2;
        if(idx < 0)
            return 0;
        if(idx >= xpByLevel.length)
            return xpByLevel[xpByLevel.length - 1];
        return xpByLevel[idx];
    }

    public long getDeathXpByLevel(int level) {
        final int idx = level - 1;
        if(idx < 0)
            return 0;
        if(idx >= deathXpByLevel.length)
            return deathXpByLevel[deathXpByLevel.length - 1];
        return deathXpByLevel[idx];
    }
}
