package com.murqin.nocroptrample;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.serialization.Codec;
import com.murqin.nocroptrample.config.ConfigSnapshot;
import com.murqin.nocroptrample.config.ModConfig;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.*;
import java.util.*;
import java.util.function.BiConsumer;

/** Per-world rules controlling crop-covered and empty farmland independently. */
public final class ModGameRules {
    private static final String[] NAMES={"player","farmer_villager","baby_villager","other_villager","mob","empty_player","empty_farmer_villager","empty_baby_villager","empty_other_villager","empty_mob"};
    private static final Identifier[] IDS=new Identifier[NAMES.length];
    public static volatile GameRule<Boolean> preventPlayerTrampling, preventFarmerVillagerTrampling, preventBabyVillagerTrampling, preventOtherVillagerTrampling, preventMobTrampling;
    public static volatile GameRule<Boolean> preventEmptyPlayerTrampling, preventEmptyFarmerVillagerTrampling, preventEmptyBabyVillagerTrampling, preventEmptyOtherVillagerTrampling, preventEmptyMobTrampling;
    public static volatile GameRule<Integer> minFeatherFallingLevel, maxSafeFallDistance;
    private static volatile List<GameRule<?>> allRules=List.of();
    static { for(int i=0;i<NAMES.length;i++) IDS[i]=Identifier.fromNamespaceAndPath(NoCropTrample.MOD_ID,"prevent_"+NAMES[i]+"_trampling"); }
    private ModGameRules() {}
    public static void registerAll(BiConsumer<Identifier,GameRule<?>> registrar) {
        if(isRegistered()) return; ConfigSnapshot d=ModConfig.snapshot();
        preventPlayerTrampling=bool(d.preventPlayerTrampling()); preventFarmerVillagerTrampling=bool(d.preventFarmerVillagerTrampling()); preventBabyVillagerTrampling=bool(d.preventBabyVillagerTrampling()); preventOtherVillagerTrampling=bool(d.preventOtherVillagerTrampling()); preventMobTrampling=bool(d.preventMobTrampling());
        preventEmptyPlayerTrampling=bool(d.preventEmptyPlayerTrampling()); preventEmptyFarmerVillagerTrampling=bool(d.preventEmptyFarmerVillagerTrampling()); preventEmptyBabyVillagerTrampling=bool(d.preventEmptyBabyVillagerTrampling()); preventEmptyOtherVillagerTrampling=bool(d.preventEmptyOtherVillagerTrampling()); preventEmptyMobTrampling=bool(d.preventEmptyMobTrampling());
        minFeatherFallingLevel=integer(d.minFeatherFallingLevel(),0,4); maxSafeFallDistance=integer(d.maxSafeFallDistance(),0,100);
        GameRule<?>[] rs={preventPlayerTrampling,preventFarmerVillagerTrampling,preventBabyVillagerTrampling,preventOtherVillagerTrampling,preventMobTrampling,preventEmptyPlayerTrampling,preventEmptyFarmerVillagerTrampling,preventEmptyBabyVillagerTrampling,preventEmptyOtherVillagerTrampling,preventEmptyMobTrampling,minFeatherFallingLevel,maxSafeFallDistance};
        for(int i=0;i<rs.length;i++) registrar.accept(i<NAMES.length?IDS[i]:Identifier.fromNamespaceAndPath(NoCropTrample.MOD_ID,i==10?"min_feather_falling_level":"max_safe_fall_distance"),rs[i]);
        allRules=List.of(rs);
    }
    public static boolean isRegistered(){return !allRules.isEmpty();}
    public static List<GameRule<?>> allRules(){return allRules;}
    public static boolean isModRule(GameRule<?> r){return allRules.contains(r);}
    public static ConfigSnapshot snapshot(GameRules r){if(r==null||!isRegistered())return ModConfig.snapshot(); return new ConfigSnapshot(
        r.get(preventPlayerTrampling),r.get(preventFarmerVillagerTrampling),r.get(preventBabyVillagerTrampling),r.get(preventOtherVillagerTrampling),r.get(preventMobTrampling),
        r.get(preventEmptyPlayerTrampling),r.get(preventEmptyFarmerVillagerTrampling),r.get(preventEmptyBabyVillagerTrampling),r.get(preventEmptyOtherVillagerTrampling),r.get(preventEmptyMobTrampling),r.get(minFeatherFallingLevel),r.get(maxSafeFallDistance));}
    public static void applySnapshot(MinecraftServer s,ConfigSnapshot c){if(s==null||c==null)return; GameRules r=s.getGameRules();
        set(s,preventPlayerTrampling,c.preventPlayerTrampling());set(s,preventFarmerVillagerTrampling,c.preventFarmerVillagerTrampling());set(s,preventBabyVillagerTrampling,c.preventBabyVillagerTrampling());set(s,preventOtherVillagerTrampling,c.preventOtherVillagerTrampling());set(s,preventMobTrampling,c.preventMobTrampling());
        set(s,preventEmptyPlayerTrampling,c.preventEmptyPlayerTrampling());set(s,preventEmptyFarmerVillagerTrampling,c.preventEmptyFarmerVillagerTrampling());set(s,preventEmptyBabyVillagerTrampling,c.preventEmptyBabyVillagerTrampling());set(s,preventEmptyOtherVillagerTrampling,c.preventEmptyOtherVillagerTrampling());set(s,preventEmptyMobTrampling,c.preventEmptyMobTrampling());set(s,minFeatherFallingLevel,c.minFeatherFallingLevel());set(s,maxSafeFallDistance,c.maxSafeFallDistance());}
    public static void applySnapshotToRules(GameRules r,ConfigSnapshot c){if(r==null||c==null||!isRegistered())return;
        r.set(preventPlayerTrampling,c.preventPlayerTrampling(),null);r.set(preventFarmerVillagerTrampling,c.preventFarmerVillagerTrampling(),null);r.set(preventBabyVillagerTrampling,c.preventBabyVillagerTrampling(),null);r.set(preventOtherVillagerTrampling,c.preventOtherVillagerTrampling(),null);r.set(preventMobTrampling,c.preventMobTrampling(),null);
        r.set(preventEmptyPlayerTrampling,c.preventEmptyPlayerTrampling(),null);r.set(preventEmptyFarmerVillagerTrampling,c.preventEmptyFarmerVillagerTrampling(),null);r.set(preventEmptyBabyVillagerTrampling,c.preventEmptyBabyVillagerTrampling(),null);r.set(preventEmptyOtherVillagerTrampling,c.preventEmptyOtherVillagerTrampling(),null);r.set(preventEmptyMobTrampling,c.preventEmptyMobTrampling(),null);r.set(minFeatherFallingLevel,c.minFeatherFallingLevel(),null);r.set(maxSafeFallDistance,c.maxSafeFallDistance(),null);}
    public static void setPreventEmptyTrampling(MinecraftServer s,boolean v){set(s,preventEmptyPlayerTrampling,v);set(s,preventEmptyFarmerVillagerTrampling,v);set(s,preventEmptyBabyVillagerTrampling,v);set(s,preventEmptyOtherVillagerTrampling,v);set(s,preventEmptyMobTrampling,v);}
    public static void setPreventPlayerTrampling(MinecraftServer s,boolean v){set(s,preventPlayerTrampling,v);}
    public static void setPreventMobTrampling(MinecraftServer s,boolean v){set(s,preventMobTrampling,v);}
    public static void setPreventFarmerVillagerTrampling(MinecraftServer s,boolean v){set(s,preventFarmerVillagerTrampling,v);}
    public static void setPreventBabyVillagerTrampling(MinecraftServer s,boolean v){set(s,preventBabyVillagerTrampling,v);}
    public static void setPreventOtherVillagerTrampling(MinecraftServer s,boolean v){set(s,preventOtherVillagerTrampling,v);}
    public static void setEmptyPlayer(MinecraftServer s,boolean v){set(s,preventEmptyPlayerTrampling,v);} public static void setEmptyFarmer(MinecraftServer s,boolean v){set(s,preventEmptyFarmerVillagerTrampling,v);} public static void setEmptyBaby(MinecraftServer s,boolean v){set(s,preventEmptyBabyVillagerTrampling,v);} public static void setEmptyOtherVillager(MinecraftServer s,boolean v){set(s,preventEmptyOtherVillagerTrampling,v);} public static void setEmptyMob(MinecraftServer s,boolean v){set(s,preventEmptyMobTrampling,v);}
    public static void setMinFeatherFallingLevel(MinecraftServer s,int v){set(s,minFeatherFallingLevel,ConfigSnapshot.clampFeatherFallingLevel(v));} public static void setMaxSafeFallDistance(MinecraftServer s,int v){set(s,maxSafeFallDistance,ConfigSnapshot.clampFallDistance(v));}
    private static <T> void set(MinecraftServer s,GameRule<T> rule,T value){if(s!=null&&rule!=null&&!Objects.equals(s.getGameRules().get(rule),value))s.getGameRules().set(rule,value,s);}
    private static GameRule<Boolean> bool(boolean d){return new GameRule<>(GameRuleCategory.MISC,GameRuleType.BOOL,BoolArgumentType.bool(),GameRuleTypeVisitor::visitBoolean,Codec.BOOL,b->b?1:0,d,FeatureFlagSet.of());}
    private static GameRule<Integer> integer(int d,int min,int max){return new GameRule<>(GameRuleCategory.MISC,GameRuleType.INT,IntegerArgumentType.integer(min,max),GameRuleTypeVisitor::visitInteger,Codec.intRange(min,max),i->i,d,FeatureFlagSet.of());}
}
