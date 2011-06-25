package com.halvors.weathercontrol.manager;

import java.util.HashMap;

/**
 * Manage wands
 * 
 * @author halvors
 */
public class WandManager {
	private final static HashMap<String, Integer> wands = new HashMap<String, Integer>();

	public static Integer getWandCount(String name) {
		return wands.get(name);
	}

	public static void addWandCount(String name, int count) {
		if (wands.containsKey(name)) {
			wands.remove(name);
		}
		
		wands.put(name, count);
	}

	public static void removeWandCount(String name) {
		if (wands.containsKey(name)) {
			wands.remove(name);
		}
	}

	public static boolean hasWandCount(String name) {
		if (wands.containsKey(name)) {
			return true;
		}
	    
	    return false;
	}
}