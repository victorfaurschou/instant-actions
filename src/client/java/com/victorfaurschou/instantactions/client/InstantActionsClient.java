package com.victorfaurschou.instantactions.client;

import com.victorfaurschou.instantactions.InstantActionsConfig;
import com.victorfaurschou.instantactions.network.ConfigSyncPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
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

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
			dispatcher.register(ClientCommands.literal("instant-actions")
				.then(ClientCommands.literal("config")
					.executes(ctx -> {
						Minecraft mc = Minecraft.getInstance();
						mc.execute(() -> mc.gui.setScreen(ClothConfigScreen.create(null)));
						return 1;
					}))
				.then(ClientCommands.literal("version")
					.executes(ctx -> {
						String version = FabricLoader.getInstance()
							.getModContainer("instant-actions")
							.map(c -> c.getMetadata().getVersion().getFriendlyString())
							.orElse("unknown");
						ctx.getSource().sendFeedback(Component.literal("Instant Actions " + version));
						return 1;
					}))));
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
