package com.victorfaurschou.instantactions.client;

import com.victorfaurschou.instantactions.InstantActionsConfig;
import com.victorfaurschou.instantactions.network.ConfigSyncPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class InstantActionsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			if (!ClientPlayNetworking.canSend(ConfigSyncPayload.TYPE)) {
				client.player.sendSystemMessage(Component.literal(
					"[Instant Actions] The host doesn't have the mod installed. Toggles will have no effect."
				).withStyle(ChatFormatting.YELLOW));
				return;
			}
			sendConfig();
		});
	}

	public static void sendConfig() {
		if (!ClientPlayNetworking.canSend(ConfigSyncPayload.TYPE)) return;
		ClientPlayNetworking.send(new ConfigSyncPayload(
			InstantActionsConfig.instantCompost,
			InstantActionsConfig.instantBoneMeal,
			InstantActionsConfig.boneMealWithRadius,
			InstantActionsConfig.instantTaming,
			InstantActionsConfig.eatToFull,
			InstantActionsConfig.chainHarvest
		));
	}
}
