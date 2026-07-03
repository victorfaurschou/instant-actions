package com.victorfaurschou.instantactions.network;

import com.victorfaurschou.instantactions.InstantActions;
import com.victorfaurschou.instantactions.PlayerConfig;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ConfigSyncPayload(
	boolean instantCompost,
	boolean instantBoneMeal,
	boolean boneMealWithRadius,
	boolean instantTaming,
	boolean eatToFull,
	boolean chainHarvest,
	boolean doubleSeeding,
	boolean doubleTilling
) implements CustomPacketPayload {
	public static final Type<ConfigSyncPayload> TYPE = new Type<>(
		Identifier.parse(InstantActions.MOD_ID + ":config_sync")
	);

	public static final StreamCodec<RegistryFriendlyByteBuf, ConfigSyncPayload> CODEC = StreamCodec.composite(
		ByteBufCodecs.BOOL, ConfigSyncPayload::instantCompost,
		ByteBufCodecs.BOOL, ConfigSyncPayload::instantBoneMeal,
		ByteBufCodecs.BOOL, ConfigSyncPayload::boneMealWithRadius,
		ByteBufCodecs.BOOL, ConfigSyncPayload::instantTaming,
		ByteBufCodecs.BOOL, ConfigSyncPayload::eatToFull,
		ByteBufCodecs.BOOL, ConfigSyncPayload::chainHarvest,
		ByteBufCodecs.BOOL, ConfigSyncPayload::doubleSeeding,
		ByteBufCodecs.BOOL, ConfigSyncPayload::doubleTilling,
		ConfigSyncPayload::new
	);

	public PlayerConfig toPlayerConfig() {
		return new PlayerConfig(instantCompost, instantBoneMeal, boneMealWithRadius, instantTaming, eatToFull, chainHarvest, doubleSeeding, doubleTilling);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
