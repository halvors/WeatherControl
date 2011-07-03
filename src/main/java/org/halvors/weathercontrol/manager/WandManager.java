package org.halvors.weathercontrol.manager;

import java.util.HashMap;

import org.halvors.weathercontrol.WeatherControl;

/**
 * Manage wands
 * 
 * @author halvors
 */
public class WandManager {
//	private WeatherControl plugin;
	
	private final HashMap<String, Integer> wands = new HashMap<String, Integer>();

	public WandManager(WeatherControl plugin) {
//		this.plugin = plugin;
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