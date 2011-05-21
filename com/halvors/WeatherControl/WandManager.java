package com.halvors.WeatherControl;

import java.util.HashMap;

/**
 * Manage wand
 * 
 * @author halvors
 */
public class WandManager {
	private final WeatherControl plugin;
	
	private final HashMap<String, Integer> wandCount = new HashMap<String, Integer>();

	public WandManager(final WeatherControl plugin) {
		this.plugin = plugin;
	}

	public Integer getWandCount(final String name) {
		return wandCount.get(name);
	}

	public void addWandCount(final String name, final int count) {
		if (wandCount.containsKey(name)) {
			wandCount.remove(name);
		}
		
		wandCount.put(name, count);
	}

	public void removeWandCount(final String name) {
		if (wandCount.containsKey(name)) {
			wandCount.remove(name);
		}
	}

	public boolean hasWandCount(final String name) {
		if (wandCount.containsKey(name)) {
			return true;
		}
	    
	    return false;
	}
}