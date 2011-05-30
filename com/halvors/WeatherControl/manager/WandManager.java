package com.halvors.WeatherControl.manager;

import java.util.HashMap;

import com.halvors.WeatherControl.WeatherControl;

/**
 * Manage wand
 * 
 * @author halvors
 */
public class WandManager {
	private final WeatherControl plugin;
	
	private final HashMap<String, Integer> wandCount;

	public WandManager(final WeatherControl plugin) {
		this.plugin = plugin;
		this.wandCount = new HashMap<String, Integer>();
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