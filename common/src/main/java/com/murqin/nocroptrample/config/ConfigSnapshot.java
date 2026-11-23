package com.murqin.nocroptrample.config;

/**
 * Immutable snapshot of all trampling rules configuration values.
 * Clamps and sanitizes out-of-range values on construction.
 */
public record ConfigSnapshot(
        boolean preventEmptyTrampling,
        boolean preventPlayerTrampling,
        boolean preventMobTrampling,
        boolean preventFarmerVillagerTrampling,
        boolean preventBabyVillagerTrampling,
        boolean preventOtherVillagerTrampling,
        int minFeatherFallingLevel,
        int maxSafeFallDistance
) {
    public static final int MIN_FEATHER_FALLING_LEVEL = 0;
    public static final int MAX_FEATHER_FALLING_LEVEL = 4;
    public static final int MIN_SAFE_FALL_DISTANCE = 0;
    public static final int MAX_SAFE_FALL_DISTANCE = 100;

    // Defaults match the farmer-villager use case: protect farmers only; let
    // players, babies, other villagers and non-villager mobs trample normally.
    public static final ConfigSnapshot DEFAULTS = new ConfigSnapshot(true, false, false, true, false, false, 0, 0);

    /** Backwards-compatible constructor for older integrations. */
    public ConfigSnapshot(boolean empty, boolean player, boolean mob, int feather, int fall) {
        this(empty, player, mob, true, true, true, feather, fall);
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

    /**
     * Converts legacy floating-point JSON values into clamped integer blocks.
     */
    public static int sanitizeFallDistance(double distance) {
        if (!Double.isFinite(distance) || distance <= 0.0) {
            return 0;
        }
        return Math.clamp(Math.round(distance), MIN_SAFE_FALL_DISTANCE, MAX_SAFE_FALL_DISTANCE);
    }
}
