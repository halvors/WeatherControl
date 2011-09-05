package org.halvors.weathercontrol.manager;

import java.util.HashMap;

import org.halvors.weathercontrol.WeatherControl;

/**
 * Hold values for each player's wand.
 * 
 * @author halvors
 */
public class WandManager {
//	private final WeatherControl plugin;
	private final HashMap<String, Integer> wands;

	private static WandManager instance;
	
	public WandManager(WeatherControl plugin) {
//		this.plugin = plugin;
		this.wands = new HashMap<String, Integer>();
		
		WandManager.instance = this;
	}
	
	public static WandManager getInstance() {
		return instance;
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