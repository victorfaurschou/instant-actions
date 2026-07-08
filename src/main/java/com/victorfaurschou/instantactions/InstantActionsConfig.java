package com.victorfaurschou.instantactions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class InstantActionsConfig {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("instant-actions.json");

	public static final ThreadLocal<PlayerConfig> BONE_MEAL_PLAYER_CONFIG = new ThreadLocal<>();

	public static boolean enabled = true;
	public static boolean doubleTilling = false;
	public static boolean doubleSeeding = false;
	public static boolean boneMealWithRadius = false;
	public static boolean instantBoneMeal = false;
	public static boolean chainHarvest = false;
	public static boolean instantCompost = false;
	public static boolean instantTaming = false;
	public static boolean instantAnimalMaturing = false;
	public static boolean eatToFull = false;

	public static void load() {
		if (!Files.exists(CONFIG_PATH)) return;
		try {
			Data data = GSON.fromJson(Files.readString(CONFIG_PATH), Data.class);
			if (data == null) return;
			enabled = data.enabled;
			doubleTilling = data.doubleTilling;
			doubleSeeding = data.doubleSeeding;
			boneMealWithRadius = data.boneMealWithRadius;
			instantBoneMeal = data.instantBoneMeal;
			chainHarvest = data.chainHarvest;
			instantCompost = data.instantCompost;
			instantTaming = data.instantTaming;
			instantAnimalMaturing = data.instantAnimalMaturing;
			eatToFull = data.eatToFull;
		} catch (IOException ignored) {}
	}

	public static void save() {
		try {
			Data data = new Data();
			data.enabled = enabled;
			data.doubleTilling = doubleTilling;
			data.doubleSeeding = doubleSeeding;
			data.boneMealWithRadius = boneMealWithRadius;
			data.instantBoneMeal = instantBoneMeal;
			data.chainHarvest = chainHarvest;
			data.instantCompost = instantCompost;
			data.instantTaming = instantTaming;
			data.instantAnimalMaturing = instantAnimalMaturing;
			data.eatToFull = eatToFull;
			Files.writeString(CONFIG_PATH, GSON.toJson(data));
		} catch (IOException ignored) {}
	}

	private static class Data {
		boolean enabled = true;
		boolean doubleTilling = false;
		boolean doubleSeeding = false;
		boolean boneMealWithRadius = false;
		boolean instantBoneMeal = false;
		boolean chainHarvest = false;
		boolean instantCompost = false;
		boolean instantTaming = false;
		boolean instantAnimalMaturing = false;
		boolean eatToFull = false;
	}
}
