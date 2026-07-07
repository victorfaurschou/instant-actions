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

	public static boolean instantCompost = false;
	public static boolean instantBoneMeal = false;
	public static boolean boneMealWithRadius = false;
	public static boolean instantTaming = false;
	public static boolean eatToFull = false;
	public static boolean chainHarvest = false;
	public static boolean doubleSeeding = false;
	public static boolean doubleTilling = false;
	public static boolean instantAnimalMaturing = false;

	public static void load() {
		if (!Files.exists(CONFIG_PATH)) return;
		try {
			Data data = GSON.fromJson(Files.readString(CONFIG_PATH), Data.class);
			if (data == null) return;
			instantCompost = data.instantCompost;
			instantBoneMeal = data.instantBoneMeal;
			boneMealWithRadius = data.boneMealWithRadius;
			instantTaming = data.instantTaming;
			eatToFull = data.eatToFull;
			chainHarvest = data.chainHarvest;
			doubleSeeding = data.doubleSeeding;
			doubleTilling = data.doubleTilling;
			instantAnimalMaturing = data.instantAnimalMaturing;
		} catch (IOException ignored) {}
	}

	public static void save() {
		try {
			Data data = new Data();
			data.instantCompost = instantCompost;
			data.instantBoneMeal = instantBoneMeal;
			data.boneMealWithRadius = boneMealWithRadius;
			data.instantTaming = instantTaming;
			data.eatToFull = eatToFull;
			data.chainHarvest = chainHarvest;
			data.doubleSeeding = doubleSeeding;
			data.doubleTilling = doubleTilling;
			data.instantAnimalMaturing = instantAnimalMaturing;
			Files.writeString(CONFIG_PATH, GSON.toJson(data));
		} catch (IOException ignored) {}
	}

	private static class Data {
		boolean instantCompost = false;
		boolean instantBoneMeal = false;
		boolean boneMealWithRadius = false;
		boolean instantTaming = false;
		boolean eatToFull = false;
		boolean chainHarvest = false;
		boolean doubleSeeding = false;
		boolean doubleTilling = false;
		boolean instantAnimalMaturing = false;
	}
}
