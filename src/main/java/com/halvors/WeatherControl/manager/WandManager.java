package com.halvors.WeatherControl.manager;

import java.util.HashMap;

import com.halvors.WeatherControl.WeatherControl;

/**
 * Manage wands
 * 
 * @author halvors
 */
public class WandManager {
//	private final WeatherControl plugin;
	
	private final HashMap<String, Integer> wands;

	public WandManager(final WeatherControl plugin) {
//		this.plugin = plugin;
		this.wands = new HashMap<String, Integer>();
	}

	public Integer getWandCount(String name) {
		return wands.get(name);
	}

	public void addWandCount(String name, int count) {
		if (wands.containsKey(name)) {
			wands.remove(name);
		}
		
		wands.put(name, count);
	}

	public void removeWandCount(String name) {
		if (wands.containsKey(name)) {
			wands.remove(name);
		}
	}

	public boolean hasWandCount(String name) {
		if (wands.containsKey(name)) {
			return true;
		}
	    
	    return false;
	}
}