package org.halvors.weathercontrol.manager;

import java.util.HashMap;

import org.bukkit.entity.Player;

/**
 * Manage wands
 * 
 * @author halvors
 */
public class WandManager {
	private final static HashMap<String, Integer> wands = new HashMap<String, Integer>();

	public static int getWandCount(Player player) {
		return wands.get(player.getName());
	}

	public static void addWandCount(Player player, int count) {
		String name = player.getName();
		
		if (wands.containsKey(name)) {
			wands.remove(name);
		}
		
		wands.put(name, count);
	}

	public static void removeWandCount(Player player) {
		String name = player.getName();
		
		if (wands.containsKey(name)) {
			wands.remove(name);
		}
	}

	public static boolean hasWandCount(Player player) {
		return wands.containsKey(player.getName());
	}
}