package com.halvors.WeatherControl;

import java.util.HashMap;

public class WeatherManager {
	private final WeatherControl plugin;
	
	private final HashMap<String, Integer> wandCount = new HashMap<String, Integer>();
	
	public WeatherManager(WeatherControl plugin) {
		this.plugin = plugin;
	}
	
	public Integer getWandCount(String name) {
		return wandCount.get(name);
	}
	
	public void addWandCount(String name, int count) {
		if (wandCount.containsKey(name)) {
			wandCount.remove(name);
		}
		
		wandCount.put(name, count);
	}
	
	public void removeWandCount(String name) {
		if (wandCount.containsKey(name)) {
			wandCount.remove(name);
		}
	}
}
