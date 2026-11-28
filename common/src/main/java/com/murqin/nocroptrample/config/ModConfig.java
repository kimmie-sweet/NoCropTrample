package com.murqin.nocroptrample.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.murqin.nocroptrample.NoCropTrample;
import java.io.IOException;
import java.nio.file.*;
import java.util.function.Consumer;

/** Global defaults persisted in config/nocroptrample.json; active worlds use GameRules. */
public final class ModConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Object LOCK = new Object();
    private static volatile Path path;
    private static volatile Consumer<ConfigSnapshot> listener = s -> {};
    private static volatile ConfigSnapshot current = ConfigSnapshot.DEFAULTS;
    private ModConfig() {}
    public static void init(Path dir) { path = dir == null ? null : dir.toAbsolutePath().resolve(NoCropTrample.MOD_ID + ".json"); }
    public static void setChangeListener(Consumer<ConfigSnapshot> l) { listener = l == null ? s -> {} : l; }
    public static ConfigSnapshot snapshot() { return current; }
    public static boolean isPreventEmptyTrampling() { return current.preventEmptyTrampling(); }
    public static boolean isPreventPlayerTrampling() { return current.preventPlayerTrampling(); }
    public static boolean isPreventMobTrampling() { return current.preventMobTrampling(); }
    public static int getMinFeatherFallingLevel() { return current.minFeatherFallingLevel(); }
    public static int getMaxSafeFallDistance() { return current.maxSafeFallDistance(); }
    public static void setPreventPlayerTrampling(boolean v) { ConfigSnapshot c=current; applyAndSave(new ConfigSnapshot(v,c.preventFarmerVillagerTrampling(),c.preventBabyVillagerTrampling(),c.preventOtherVillagerTrampling(),c.preventMobTrampling(),c.preventEmptyPlayerTrampling(),c.preventEmptyFarmerVillagerTrampling(),c.preventEmptyBabyVillagerTrampling(),c.preventEmptyOtherVillagerTrampling(),c.preventEmptyMobTrampling(),c.minFeatherFallingLevel(),c.maxSafeFallDistance())); }
    public static void setPreventFarmerVillagerTrampling(boolean v) { ConfigSnapshot c=current; applyAndSave(new ConfigSnapshot(c.preventPlayerTrampling(),v,c.preventBabyVillagerTrampling(),c.preventOtherVillagerTrampling(),c.preventMobTrampling(),c.preventEmptyPlayerTrampling(),c.preventEmptyFarmerVillagerTrampling(),c.preventEmptyBabyVillagerTrampling(),c.preventEmptyOtherVillagerTrampling(),c.preventEmptyMobTrampling(),c.minFeatherFallingLevel(),c.maxSafeFallDistance())); }
    public static void setPreventBabyVillagerTrampling(boolean v) { ConfigSnapshot c=current; applyAndSave(new ConfigSnapshot(c.preventPlayerTrampling(),c.preventFarmerVillagerTrampling(),v,c.preventOtherVillagerTrampling(),c.preventMobTrampling(),c.preventEmptyPlayerTrampling(),c.preventEmptyFarmerVillagerTrampling(),c.preventEmptyBabyVillagerTrampling(),c.preventEmptyOtherVillagerTrampling(),c.preventEmptyMobTrampling(),c.minFeatherFallingLevel(),c.maxSafeFallDistance())); }
    public static void setPreventOtherVillagerTrampling(boolean v) { ConfigSnapshot c=current; applyAndSave(new ConfigSnapshot(c.preventPlayerTrampling(),c.preventFarmerVillagerTrampling(),c.preventBabyVillagerTrampling(),v,c.preventMobTrampling(),c.preventEmptyPlayerTrampling(),c.preventEmptyFarmerVillagerTrampling(),c.preventEmptyBabyVillagerTrampling(),c.preventEmptyOtherVillagerTrampling(),c.preventEmptyMobTrampling(),c.minFeatherFallingLevel(),c.maxSafeFallDistance())); }
    public static void setPreventMobTrampling(boolean v) { ConfigSnapshot c=current; applyAndSave(new ConfigSnapshot(c.preventPlayerTrampling(),c.preventFarmerVillagerTrampling(),c.preventBabyVillagerTrampling(),c.preventOtherVillagerTrampling(),v,c.preventEmptyPlayerTrampling(),c.preventEmptyFarmerVillagerTrampling(),c.preventEmptyBabyVillagerTrampling(),c.preventEmptyOtherVillagerTrampling(),c.preventEmptyMobTrampling(),c.minFeatherFallingLevel(),c.maxSafeFallDistance())); }
    public static void setPreventEmptyTrampling(boolean v) { ConfigSnapshot c=current; applyAndSave(new ConfigSnapshot(c.preventPlayerTrampling(),c.preventFarmerVillagerTrampling(),c.preventBabyVillagerTrampling(),c.preventOtherVillagerTrampling(),c.preventMobTrampling(),v,v,v,v,v,c.minFeatherFallingLevel(),c.maxSafeFallDistance())); }
    public static void setMinFeatherFallingLevel(int v) { ConfigSnapshot c=current; applyAndSave(new ConfigSnapshot(c.preventPlayerTrampling(),c.preventFarmerVillagerTrampling(),c.preventBabyVillagerTrampling(),c.preventOtherVillagerTrampling(),c.preventMobTrampling(),c.preventEmptyPlayerTrampling(),c.preventEmptyFarmerVillagerTrampling(),c.preventEmptyBabyVillagerTrampling(),c.preventEmptyOtherVillagerTrampling(),c.preventEmptyMobTrampling(),ConfigSnapshot.clampFeatherFallingLevel(v),c.maxSafeFallDistance())); }
    public static void setMaxSafeFallDistance(int v) { ConfigSnapshot c=current; applyAndSave(new ConfigSnapshot(c.preventPlayerTrampling(),c.preventFarmerVillagerTrampling(),c.preventBabyVillagerTrampling(),c.preventOtherVillagerTrampling(),c.preventMobTrampling(),c.preventEmptyPlayerTrampling(),c.preventEmptyFarmerVillagerTrampling(),c.preventEmptyBabyVillagerTrampling(),c.preventEmptyOtherVillagerTrampling(),c.preventEmptyMobTrampling(),c.minFeatherFallingLevel(),ConfigSnapshot.clampFallDistance(v))); }
    public static void applyAndSave(ConfigSnapshot s) { if (s == null) return; synchronized (LOCK) { if (s.equals(current)) return; current=s; saveLocked(s); } }
    public static void reset() { applyAndSave(ConfigSnapshot.DEFAULTS); }
    public static boolean load() { return loadInternal(false); }
    public static boolean reload() { return loadInternal(true); }
    private static boolean loadInternal(boolean preserve) { synchronized (LOCK) {
        if (path == null) { NoCropTrample.LOGGER.warn("Config path not initialized"); return false; }
        if (!Files.exists(path)) { current=ConfigSnapshot.DEFAULTS; saveLocked(current); return true; }
        try {
            Data d=GSON.fromJson(Files.readString(path), Data.class);
            if (d == null) throw new JsonParseException("Empty config");
            // Migrate the former single empty-farmland toggle to all five new empty categories.
            boolean legacyEmpty = Boolean.TRUE.equals(d.preventEmptyTrampling);
            ConfigSnapshot s = new ConfigSnapshot(
                val(d.preventPlayerTrampling), val(d.preventFarmerVillagerTrampling), val(d.preventBabyVillagerTrampling), val(d.preventOtherVillagerTrampling), val(d.preventMobTrampling),
                d.preventEmptyPlayerTrampling != null ? d.preventEmptyPlayerTrampling : legacyEmpty,
                d.preventEmptyFarmerVillagerTrampling != null ? d.preventEmptyFarmerVillagerTrampling : legacyEmpty,
                d.preventEmptyBabyVillagerTrampling != null ? d.preventEmptyBabyVillagerTrampling : legacyEmpty,
                d.preventEmptyOtherVillagerTrampling != null ? d.preventEmptyOtherVillagerTrampling : legacyEmpty,
                d.preventEmptyMobTrampling != null ? d.preventEmptyMobTrampling : legacyEmpty,
                d.minFeatherFallingLevel, d.maxSafeFallDistance == null ? 0 : ConfigSnapshot.sanitizeFallDistance(d.maxSafeFallDistance));
            current=s; listener.accept(s); NoCropTrample.LOGGER.info("Config loaded from {}", path); return true;
        } catch (IOException | JsonParseException | NumberFormatException e) {
            NoCropTrample.LOGGER.error("Failed to load config from {}", path, e);
            if (!preserve) { current=ConfigSnapshot.DEFAULTS; listener.accept(current); }
            return false;
        }
    }}
    public static void save() { synchronized (LOCK) { saveLocked(current); } }
    private static boolean val(Boolean b) { return Boolean.TRUE.equals(b); }
    private static void saveLocked(ConfigSnapshot s) {
        if (path == null) return; Path tmp=null;
        try { Path parent=path.getParent(); if (parent != null) Files.createDirectories(parent);
            Data d=new Data();
            d.preventPlayerTrampling=s.preventPlayerTrampling(); d.preventFarmerVillagerTrampling=s.preventFarmerVillagerTrampling();
            d.preventBabyVillagerTrampling=s.preventBabyVillagerTrampling(); d.preventOtherVillagerTrampling=s.preventOtherVillagerTrampling(); d.preventMobTrampling=s.preventMobTrampling();
            d.preventEmptyPlayerTrampling=s.preventEmptyPlayerTrampling(); d.preventEmptyFarmerVillagerTrampling=s.preventEmptyFarmerVillagerTrampling();
            d.preventEmptyBabyVillagerTrampling=s.preventEmptyBabyVillagerTrampling(); d.preventEmptyOtherVillagerTrampling=s.preventEmptyOtherVillagerTrampling(); d.preventEmptyMobTrampling=s.preventEmptyMobTrampling();
            d.minFeatherFallingLevel=s.minFeatherFallingLevel(); d.maxSafeFallDistance=(double)s.maxSafeFallDistance();
            tmp=Files.createTempFile(parent, NoCropTrample.MOD_ID+"-", ".tmp"); Files.writeString(tmp,GSON.toJson(d));
            try { Files.move(tmp,path,StandardCopyOption.REPLACE_EXISTING,StandardCopyOption.ATOMIC_MOVE); }
            catch (AtomicMoveNotSupportedException ex) { Files.move(tmp,path,StandardCopyOption.REPLACE_EXISTING); }
        } catch(IOException e) { NoCropTrample.LOGGER.error("Failed to save config",e); }
        finally { if(tmp!=null) try { Files.deleteIfExists(tmp); } catch(IOException ignored) {} }
        listener.accept(s);
    }
    private static final class Data {
        Boolean preventEmptyTrampling; // legacy field, read only
        Boolean preventPlayerTrampling, preventFarmerVillagerTrampling, preventBabyVillagerTrampling, preventOtherVillagerTrampling, preventMobTrampling;
        Boolean preventEmptyPlayerTrampling, preventEmptyFarmerVillagerTrampling, preventEmptyBabyVillagerTrampling, preventEmptyOtherVillagerTrampling, preventEmptyMobTrampling;
        int minFeatherFallingLevel=0; Double maxSafeFallDistance=0.0;
    }
}
