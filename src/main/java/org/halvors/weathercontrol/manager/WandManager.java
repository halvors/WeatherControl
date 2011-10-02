/*
 * Copyright (C) 2011 halvors <halvors@skymiastudios.com>.
 *
 * This file is part of WeatherControl.
 *
 * WeatherControl is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WeatherControl is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with WeatherControl.  If not, see <http://www.gnu.org/licenses/>.
 */

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