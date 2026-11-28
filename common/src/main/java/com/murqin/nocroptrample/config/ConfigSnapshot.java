package com.murqin.nocroptrample.config;

/** Immutable snapshot of all crop-covered and empty-farmland trampling rules. */
public record ConfigSnapshot(
        boolean preventPlayerTrampling,
        boolean preventFarmerVillagerTrampling,
        boolean preventBabyVillagerTrampling,
        boolean preventOtherVillagerTrampling,
        boolean preventMobTrampling,
        boolean preventEmptyPlayerTrampling,
        boolean preventEmptyFarmerVillagerTrampling,
        boolean preventEmptyBabyVillagerTrampling,
        boolean preventEmptyOtherVillagerTrampling,
        boolean preventEmptyMobTrampling,
        int minFeatherFallingLevel,
        int maxSafeFallDistance
) {
    public static final int MIN_FEATHER_FALLING_LEVEL = 0;
    public static final int MAX_FEATHER_FALLING_LEVEL = 4;
    public static final int MIN_SAFE_FALL_DISTANCE = 0;
    public static final int MAX_SAFE_FALL_DISTANCE = 100;

    /** All prevention toggles default to OFF; numeric options retain their neutral defaults. */
    public static final ConfigSnapshot DEFAULTS = new ConfigSnapshot(
            false, false, false, false, false, false, false, false, false, false, 0, 0);

    /** Compatibility constructor for the previous six-category configuration. */
    public ConfigSnapshot(boolean empty, boolean player, boolean mob, boolean farmer, boolean baby, boolean other, int feather, int fall) {
        this(player, farmer, baby, other, mob, empty, empty, empty, empty, empty, feather, fall);
    }

    /** Compatibility constructor for the original configuration. */
    public ConfigSnapshot(boolean empty, boolean player, boolean mob, int feather, int fall) {
        this(empty, player, mob, true, true, true, feather, fall);
    }

    /** Legacy aggregate accessor: true when any empty-farmland category is protected. */
    public boolean preventEmptyTrampling() {
        return preventEmptyPlayerTrampling || preventEmptyFarmerVillagerTrampling
                || preventEmptyBabyVillagerTrampling || preventEmptyOtherVillagerTrampling
                || preventEmptyMobTrampling;
    }

    public ConfigSnapshot {
        minFeatherFallingLevel = clampFeatherFallingLevel(minFeatherFallingLevel);
        maxSafeFallDistance = clampFallDistance(maxSafeFallDistance);
    }

    public static int clampFeatherFallingLevel(int level) {
        return Math.clamp(level, MIN_FEATHER_FALLING_LEVEL, MAX_FEATHER_FALLING_LEVEL);
    }
    public static int clampFallDistance(int distance) {
        return Math.clamp(distance, MIN_SAFE_FALL_DISTANCE, MAX_SAFE_FALL_DISTANCE);
    }
    public static int sanitizeFallDistance(double distance) {
        if (!Double.isFinite(distance) || distance <= 0.0) return 0;
        return Math.clamp(Math.round(distance), MIN_SAFE_FALL_DISTANCE, MAX_SAFE_FALL_DISTANCE);
    }
}
