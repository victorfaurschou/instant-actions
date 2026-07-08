package com.victorfaurschou.instantactions.network;

import com.victorfaurschou.instantactions.InstantActions;
import com.victorfaurschou.instantactions.PlayerConfig;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ConfigSyncPayload(
	boolean enabled,
	boolean doubleTilling,
	boolean doubleSeeding,
	boolean boneMealWithRadius,
	boolean instantBoneMeal,
	boolean chainHarvest,
	boolean instantCompost,
	boolean instantTaming,
	boolean instantAnimalMaturing,
	boolean eatToFull
) implements CustomPacketPayload {
	public static final Type<ConfigSyncPayload> TYPE = new Type<>(
		Identifier.parse(InstantActions.MOD_ID + ":config_sync")
	);

	public static final StreamCodec<RegistryFriendlyByteBuf, ConfigSyncPayload> CODEC = StreamCodec.composite(
		ByteBufCodecs.BOOL, ConfigSyncPayload::enabled,
		ByteBufCodecs.BOOL, ConfigSyncPayload::doubleTilling,
		ByteBufCodecs.BOOL, ConfigSyncPayload::doubleSeeding,
		ByteBufCodecs.BOOL, ConfigSyncPayload::boneMealWithRadius,
		ByteBufCodecs.BOOL, ConfigSyncPayload::instantBoneMeal,
		ByteBufCodecs.BOOL, ConfigSyncPayload::chainHarvest,
		ByteBufCodecs.BOOL, ConfigSyncPayload::instantCompost,
		ByteBufCodecs.BOOL, ConfigSyncPayload::instantTaming,
		ByteBufCodecs.BOOL, ConfigSyncPayload::instantAnimalMaturing,
		ByteBufCodecs.BOOL, ConfigSyncPayload::eatToFull,
		ConfigSyncPayload::new
	);

	public PlayerConfig toPlayerConfig() {
		return new PlayerConfig(doubleTilling, doubleSeeding, boneMealWithRadius, instantBoneMeal, chainHarvest, instantCompost, instantTaming, instantAnimalMaturing, eatToFull);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
