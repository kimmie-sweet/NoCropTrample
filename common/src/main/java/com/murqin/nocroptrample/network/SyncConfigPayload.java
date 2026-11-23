package com.murqin.nocroptrample.network;

import com.murqin.nocroptrample.NoCropTrample;
import com.murqin.nocroptrample.config.ConfigSnapshot;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * Optional read-only server-to-client payload that shares the server's active
 * NoCropTrample configuration with modded clients so the config screen can display it.
 */
public record SyncConfigPayload(
        boolean preventEmptyTrampling,
        boolean preventPlayerTrampling,
        boolean preventMobTrampling,
        boolean preventFarmerVillagerTrampling,
        boolean preventBabyVillagerTrampling,
        boolean preventOtherVillagerTrampling,
        int minFeatherFallingLevel,
        int maxSafeFallDistance
) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SyncConfigPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(NoCropTrample.MOD_ID, "sync_config"));

    public static final StreamCodec<ByteBuf, SyncConfigPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, SyncConfigPayload::preventEmptyTrampling,
            ByteBufCodecs.BOOL, SyncConfigPayload::preventPlayerTrampling,
            ByteBufCodecs.BOOL, SyncConfigPayload::preventMobTrampling,
            ByteBufCodecs.BOOL, SyncConfigPayload::preventFarmerVillagerTrampling,
            ByteBufCodecs.BOOL, SyncConfigPayload::preventBabyVillagerTrampling,
            ByteBufCodecs.BOOL, SyncConfigPayload::preventOtherVillagerTrampling,
            ByteBufCodecs.VAR_INT, SyncConfigPayload::minFeatherFallingLevel,
            ByteBufCodecs.VAR_INT, SyncConfigPayload::maxSafeFallDistance,
            SyncConfigPayload::new);

    public static SyncConfigPayload from(ConfigSnapshot snapshot) {
        return new SyncConfigPayload(
                snapshot.preventEmptyTrampling(),
                snapshot.preventPlayerTrampling(),
                snapshot.preventMobTrampling(),
                snapshot.preventFarmerVillagerTrampling(),
                snapshot.preventBabyVillagerTrampling(),
                snapshot.preventOtherVillagerTrampling(),
                snapshot.minFeatherFallingLevel(),
                snapshot.maxSafeFallDistance());
    }

    public ConfigSnapshot toSnapshot() {
        return new ConfigSnapshot(
                preventEmptyTrampling,
                preventPlayerTrampling,
                preventMobTrampling,
                preventFarmerVillagerTrampling,
                preventBabyVillagerTrampling,
                preventOtherVillagerTrampling,
                minFeatherFallingLevel,
                maxSafeFallDistance);
    }

    @Override
    public Type<SyncConfigPayload> type() {
        return TYPE;
    }
}
