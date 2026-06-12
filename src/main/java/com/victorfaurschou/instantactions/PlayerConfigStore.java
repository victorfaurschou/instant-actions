package com.victorfaurschou.instantactions;

import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerConfigStore {
	private static final Map<UUID, PlayerConfig> store = new ConcurrentHashMap<>();

	public static void set(UUID uuid, PlayerConfig config) {
		store.put(uuid, config);
	}

	public static void remove(UUID uuid) {
		store.remove(uuid);
	}

	public static PlayerConfig get(Player player) {
		return store.get(player.getUUID());
	}
}
