package com.victorfaurschou.instantactions.client;

import com.victorfaurschou.instantactions.InstantActionsConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ModMenuIntegration implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return ModMenuIntegration::buildConfigScreen;
	}

	private static Screen buildConfigScreen(Screen parent) {
		ConfigBuilder builder = ConfigBuilder.create()
			.setParentScreen(parent)
			.setTitle(Component.literal("Instant Actions"))
			.setSavingRunnable(() -> {
				InstantActionsConfig.save();
				InstantActionsClient.sendConfig();
			});

		ConfigCategory general = builder.getOrCreateCategory(Component.literal("General"));
		ConfigEntryBuilder entries = builder.entryBuilder();

		general.addEntry(entries
			.startBooleanToggle(Component.literal("Instant Bone Meal"), InstantActionsConfig.instantBoneMeal)
			.setDefaultValue(false)
			.setTooltip(Component.literal("Instantly grow a crop to full maturity, consuming however many bone meal are required."))
			.setSaveConsumer(value -> InstantActionsConfig.instantBoneMeal = value)
			.build());

		general.addEntry(entries
			.startBooleanToggle(Component.literal("Bone Meal with Radius"), InstantActionsConfig.boneMealWithRadius)
			.setDefaultValue(false)
			.setTooltip(Component.literal("When bonemealing a crop, also apply bone meal to adjacent crops of the same type (cardinals + diagonals, 1 block away)."))
			.setSaveConsumer(value -> InstantActionsConfig.boneMealWithRadius = value)
			.build());

		general.addEntry(entries
			.startBooleanToggle(Component.literal("Chain Harvest"), InstantActionsConfig.chainHarvest)
			.setDefaultValue(false)
			.setTooltip(Component.literal("Breaking a fully-grown crop automatically harvests all connected crops of the same type within a 3-block radius."))
			.setSaveConsumer(value -> InstantActionsConfig.chainHarvest = value)
			.build());

		general.addEntry(entries
			.startBooleanToggle(Component.literal("Instant Compost"), InstantActionsConfig.instantCompost)
			.setDefaultValue(false)
			.setTooltip(Component.literal("Instantly fill the composter with one click, consuming as many items as needed."))
			.setSaveConsumer(value -> InstantActionsConfig.instantCompost = value)
			.build());

		general.addEntry(entries
			.startBooleanToggle(Component.literal("Instant Taming"), InstantActionsConfig.instantTaming)
			.setDefaultValue(false)
			.setTooltip(Component.literal("Guarantee taming success on the first try."))
			.setSaveConsumer(value -> InstantActionsConfig.instantTaming = value)
			.build());

		general.addEntry(entries
			.startBooleanToggle(Component.literal("Eat to Full"), InstantActionsConfig.eatToFull)
			.setDefaultValue(false)
			.setTooltip(Component.literal("Eating food automatically consumes as many of the same item as needed to refill the hunger bar."))
			.setSaveConsumer(value -> InstantActionsConfig.eatToFull = value)
			.build());

		return builder.build();
	}
}
