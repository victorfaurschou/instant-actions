package com.victorfaurschou.instantactions.client;

import com.victorfaurschou.instantactions.InstantActionsConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.AlertScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ClothConfigScreen {

	private static Component tip(String key) {
		return Component.translatable("config.instant-actions." + key + ".tooltip");
	}

	public static Screen create(Screen parent) {
		if (!InstantActionsConfig.enabled) {
			return new AlertScreen(
				() -> Minecraft.getInstance().gui.setScreen(parent),
				Component.translatable("config.instant-actions.title"),
				Component.translatable("config.instant-actions.disabled_warning")
			);
		}

		ConfigBuilder builder = ConfigBuilder.create()
			.setParentScreen(parent)
			.setTitle(Component.translatable("config.instant-actions.title"))
			.setSavingRunnable(() -> {
				InstantActionsConfig.save();
				InstantActionsClient.sendConfig();
			});

		ConfigEntryBuilder entries = builder.entryBuilder();

		ConfigCategory farming = builder.getOrCreateCategory(Component.translatable("config.instant-actions.category.farming"));

		farming.addEntry(entries
			.startBooleanToggle(Component.translatable("config.instant-actions.double_tilling"), InstantActionsConfig.doubleTilling)
			.setDefaultValue(false)
			.setTooltip(tip("double_tilling"))
			.setSaveConsumer(value -> InstantActionsConfig.doubleTilling = value)
			.build());

		farming.addEntry(entries
			.startBooleanToggle(Component.translatable("config.instant-actions.double_seeding"), InstantActionsConfig.doubleSeeding)
			.setDefaultValue(false)
			.setTooltip(tip("double_seeding"))
			.setSaveConsumer(value -> InstantActionsConfig.doubleSeeding = value)
			.build());

		farming.addEntry(entries
			.startBooleanToggle(Component.translatable("config.instant-actions.double_bone_meal"), InstantActionsConfig.boneMealWithRadius)
			.setDefaultValue(false)
			.setTooltip(tip("double_bone_meal"))
			.setSaveConsumer(value -> InstantActionsConfig.boneMealWithRadius = value)
			.build());

		farming.addEntry(entries
			.startBooleanToggle(Component.translatable("config.instant-actions.instant_crop_maturity"), InstantActionsConfig.instantBoneMeal)
			.setDefaultValue(false)
			.setTooltip(tip("instant_crop_maturity"))
			.setSaveConsumer(value -> InstantActionsConfig.instantBoneMeal = value)
			.build());

		farming.addEntry(entries
			.startBooleanToggle(Component.translatable("config.instant-actions.chain_harvest"), InstantActionsConfig.chainHarvest)
			.setDefaultValue(false)
			.setTooltip(tip("chain_harvest"))
			.setSaveConsumer(value -> InstantActionsConfig.chainHarvest = value)
			.build());

		farming.addEntry(entries
			.startBooleanToggle(Component.translatable("config.instant-actions.instant_composting"), InstantActionsConfig.instantCompost)
			.setDefaultValue(false)
			.setTooltip(tip("instant_composting"))
			.setSaveConsumer(value -> InstantActionsConfig.instantCompost = value)
			.build());

		ConfigCategory animals = builder.getOrCreateCategory(Component.translatable("config.instant-actions.category.animals"));

		animals.addEntry(entries
			.startBooleanToggle(Component.translatable("config.instant-actions.instant_taming"), InstantActionsConfig.instantTaming)
			.setDefaultValue(false)
			.setTooltip(tip("instant_taming"))
			.setSaveConsumer(value -> InstantActionsConfig.instantTaming = value)
			.build());

		animals.addEntry(entries
			.startBooleanToggle(Component.translatable("config.instant-actions.instant_animal_maturing"), InstantActionsConfig.instantAnimalMaturing)
			.setDefaultValue(false)
			.setTooltip(tip("instant_animal_maturing"))
			.setSaveConsumer(value -> InstantActionsConfig.instantAnimalMaturing = value)
			.build());

		ConfigCategory food = builder.getOrCreateCategory(Component.translatable("config.instant-actions.category.food"));

		food.addEntry(entries
			.startBooleanToggle(Component.translatable("config.instant-actions.restore_hunger_instantly"), InstantActionsConfig.eatToFull)
			.setDefaultValue(false)
			.setTooltip(tip("restore_hunger_instantly"))
			.setSaveConsumer(value -> InstantActionsConfig.eatToFull = value)
			.build());

		return builder.build();
	}
}
