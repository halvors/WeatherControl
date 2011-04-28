<<<<<<< HEAD
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

=======
>>>>>>> 482fb8815fa8d13e587d4cf60eb186f89487eed4
package com.halvors.WeatherControl;

import org.bukkit.World;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.weather.WeatherListener;

import com.halvors.WeatherControl.util.WorldConfig;

public class WeatherControlWeatherListener extends WeatherListener {
	private final WeatherControl plugin;
	
	public WeatherControlWeatherListener(WeatherControl instance) {
		this.plugin = instance;
	}
	
	@Override
	public void onWeatherChange(WeatherChangeEvent event) {
<<<<<<< HEAD
		if (!event.isCancelled()) {
			World world = event.getWorld();
			WorldConfig worldConfig = plugin.getConfigManager().getWorldConfig(world);

			if (!worldConfig.weatherEnable) {
				if (event.toWeatherState()) {
					event.setCancelled(true);
				}
			}
=======
		World world = event.getWorld();
		WorldConfig worldConfig = plugin.getConfigManager().getWorldConfig(world);
		
		if (worldConfig.disableWeather) {
			event.setCancelled(true);
>>>>>>> 482fb8815fa8d13e587d4cf60eb186f89487eed4
		}
	}
	
	@Override
	public void onThunderChange(ThunderChangeEvent event) {
<<<<<<< HEAD
		if (!event.isCancelled()) {
			World world = event.getWorld();
			WorldConfig worldConfig = plugin.getConfigManager().getWorldConfig(world);
		
			if ((!worldConfig.weatherEnable) || (!worldConfig.thunderEnable)) {
				if (event.toThunderState()) {
					event.setCancelled(true);
				}
			}
=======
		World world = event.getWorld();
		WorldConfig worldConfig = plugin.getConfigManager().getWorldConfig(world);
		
		if (worldConfig.disableThunder) {
			event.setCancelled(true);
>>>>>>> 482fb8815fa8d13e587d4cf60eb186f89487eed4
		}
	}

	@Override
	public void onLightningStrike(LightningStrikeEvent event) {
<<<<<<< HEAD
		if (!event.isCancelled()) {
			World world = event.getWorld();
			WorldConfig worldConfig = plugin.getConfigManager().getWorldConfig(world);
		
			if (!worldConfig.lightningEnable) {
				event.setCancelled(true);
			}
=======
		World world = event.getWorld();
		WorldConfig worldConfig = plugin.getConfigManager().getWorldConfig(world);
		
		if (worldConfig.disableLightningStrike) {
			event.setCancelled(true);
>>>>>>> 482fb8815fa8d13e587d4cf60eb186f89487eed4
		}
	}
}
