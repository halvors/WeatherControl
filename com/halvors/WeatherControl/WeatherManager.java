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

package com.halvors.WeatherControl;

import java.util.HashMap;

public class WeatherManager {
	private final WeatherControl plugin;
	
	private final HashMap<String, Integer> wandCount = new HashMap<String, Integer>();
	
	public WeatherManager(WeatherControl plugin) {
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