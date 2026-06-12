package com.victorfaurschou.instantactions.client;

import com.victorfaurschou.instantactions.InstantActionsConfig;
import com.victorfaurschou.instantactions.network.ConfigSyncPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class InstantActionsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> sendConfig());
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
