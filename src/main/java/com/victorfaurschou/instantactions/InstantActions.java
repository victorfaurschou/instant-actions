package com.victorfaurschou.instantactions;

import com.victorfaurschou.instantactions.network.ConfigSyncPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class InstantActions implements ModInitializer {
	public static final String MOD_ID = "instant-actions";

	@Override
	public void onInitialize() {
		InstantActionsConfig.load();

		PayloadTypeRegistry.serverboundPlay().register(ConfigSyncPayload.TYPE, ConfigSyncPayload.CODEC);

		ServerPlayNetworking.registerGlobalReceiver(ConfigSyncPayload.TYPE, (payload, context) -> {
			if (payload.enabled()) {
				PlayerConfigStore.set(context.player().getUUID(), payload.toPlayerConfig());
			} else {
				PlayerConfigStore.remove(context.player().getUUID());
			}
		});

		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) ->
			PlayerConfigStore.remove(handler.player.getUUID())
		);
	}
}
