package com.murqin.nocroptrample;

import com.murqin.nocroptrample.config.ModConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

/**
 * Shared mod bootstrap, called by each platform's entrypoint.
 */
public final class NoCropTrample {
    public static final String MOD_ID = "nocroptrample";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private NoCropTrample() {
    }

    /**
     * Loads the config and logs the active settings. Must be called once by
     * the platform entrypoint, before anything reads {@link ModConfig}.
     *
     * @param configDir the platform's config directory
     */
    public static void init(Path configDir) {
        LOGGER.info("NoCropTrample mod initialized!");

        ModConfig.init(configDir);
        ModConfig.load();

        LOGGER.info("Farmland trampling prevention settings loaded (all prevention defaults OFF unless configured).");
        LOGGER.info("Min Feather Falling level: {}", ModConfig.getMinFeatherFallingLevel());
        LOGGER.info("Max safe fall distance: {}", ModConfig.getMaxSafeFallDistance() > 0
                ? ModConfig.getMaxSafeFallDistance() + " blocks"
                : "Unlimited");
    }
}
