package com.murqin.nocroptrample.network;

import com.murqin.nocroptrample.NoCropTrample;
import com.murqin.nocroptrample.config.ConfigSnapshot;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/** Server-to-client snapshot for the configuration screen. */
public record SyncConfigPayload(
 boolean preventPlayerTrampling, boolean preventFarmerVillagerTrampling, boolean preventBabyVillagerTrampling, boolean preventOtherVillagerTrampling, boolean preventMobTrampling,
 boolean preventEmptyPlayerTrampling, boolean preventEmptyFarmerVillagerTrampling, boolean preventEmptyBabyVillagerTrampling, boolean preventEmptyOtherVillagerTrampling, boolean preventEmptyMobTrampling,
 int minFeatherFallingLevel, int maxSafeFallDistance) implements CustomPacketPayload {
 public static final Type<SyncConfigPayload> TYPE=new Type<>(Identifier.fromNamespaceAndPath(NoCropTrample.MOD_ID,"sync_config"));
 public static final StreamCodec<ByteBuf,SyncConfigPayload> STREAM_CODEC=StreamCodec.of(
  (buf, value) -> {
   ByteBufCodecs.BOOL.encode(buf,value.preventPlayerTrampling);
   ByteBufCodecs.BOOL.encode(buf,value.preventFarmerVillagerTrampling);
   ByteBufCodecs.BOOL.encode(buf,value.preventBabyVillagerTrampling);
   ByteBufCodecs.BOOL.encode(buf,value.preventOtherVillagerTrampling);
   ByteBufCodecs.BOOL.encode(buf,value.preventMobTrampling);
   ByteBufCodecs.BOOL.encode(buf,value.preventEmptyPlayerTrampling);
   ByteBufCodecs.BOOL.encode(buf,value.preventEmptyFarmerVillagerTrampling);
   ByteBufCodecs.BOOL.encode(buf,value.preventEmptyBabyVillagerTrampling);
   ByteBufCodecs.BOOL.encode(buf,value.preventEmptyOtherVillagerTrampling);
   ByteBufCodecs.BOOL.encode(buf,value.preventEmptyMobTrampling);
   ByteBufCodecs.VAR_INT.encode(buf,value.minFeatherFallingLevel);
   ByteBufCodecs.VAR_INT.encode(buf,value.maxSafeFallDistance);
  },
  buf -> new SyncConfigPayload(
   ByteBufCodecs.BOOL.decode(buf),ByteBufCodecs.BOOL.decode(buf),ByteBufCodecs.BOOL.decode(buf),
   ByteBufCodecs.BOOL.decode(buf),ByteBufCodecs.BOOL.decode(buf),ByteBufCodecs.BOOL.decode(buf),
   ByteBufCodecs.BOOL.decode(buf),ByteBufCodecs.BOOL.decode(buf),ByteBufCodecs.BOOL.decode(buf),
   ByteBufCodecs.BOOL.decode(buf),ByteBufCodecs.VAR_INT.decode(buf),ByteBufCodecs.VAR_INT.decode(buf)));
 public static SyncConfigPayload from(ConfigSnapshot s){return new SyncConfigPayload(s.preventPlayerTrampling(),s.preventFarmerVillagerTrampling(),s.preventBabyVillagerTrampling(),s.preventOtherVillagerTrampling(),s.preventMobTrampling(),s.preventEmptyPlayerTrampling(),s.preventEmptyFarmerVillagerTrampling(),s.preventEmptyBabyVillagerTrampling(),s.preventEmptyOtherVillagerTrampling(),s.preventEmptyMobTrampling(),s.minFeatherFallingLevel(),s.maxSafeFallDistance());}
 public ConfigSnapshot toSnapshot(){return new ConfigSnapshot(preventPlayerTrampling,preventFarmerVillagerTrampling,preventBabyVillagerTrampling,preventOtherVillagerTrampling,preventMobTrampling,preventEmptyPlayerTrampling,preventEmptyFarmerVillagerTrampling,preventEmptyBabyVillagerTrampling,preventEmptyOtherVillagerTrampling,preventEmptyMobTrampling,minFeatherFallingLevel,maxSafeFallDistance);}
 @Override public Type<SyncConfigPayload> type(){return TYPE;}
}
