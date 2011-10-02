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

package org.halvors.weathercontrol.listeners;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.halvors.weathercontrol.WeatherControl;
import org.halvors.weathercontrol.util.ConfigurationManager;
import org.halvors.weathercontrol.util.WorldConfiguration;

/**
 * Handle events for all Weather related events
 * 
 * @author halvors
 */
public class WeatherListener extends org.bukkit.event.weather.WeatherListener {
//    private final WeatherControl plugin;
    
	private final ConfigurationManager configManager;
    
    public WeatherListener(final WeatherControl plugin) {
//        this.plugin = plugin;
        this.configManager = plugin.getConfigurationManager();
    }
    
    @Override
    public void onWeatherChange(WeatherChangeEvent event) {
        if (!event.isCancelled()) {
            World world = event.getWorld();
            WorldConfiguration worldConfig = configManager.get(world);

            if (!worldConfig.weatherEnable) {
                if (event.toWeatherState()) {
                    event.setCancelled(true);
                }
            }
        }
    }
    
    @Override
    public void onThunderChange(ThunderChangeEvent event) {
        if (!event.isCancelled()) {
            World world = event.getWorld();
            WorldConfiguration worldConfig = configManager.get(world);
        
            if ((!worldConfig.weatherEnable) || (!worldConfig.thunderEnable)) {
                if (event.toThunderState()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @Override
    public void onLightningStrike(LightningStrikeEvent event) {
        if (!event.isCancelled()) {
        	Location location = event.getLightning().getLocation();
            World world = event.getWorld();
            WorldConfiguration worldConfig = configManager.get(world);
        
            if (worldConfig.lightningExplosion) {
            	world.createExplosion(location, 4F);
            }
            
            if (!worldConfig.lightningEnable) {
                event.setCancelled(true);
            }
        }
    }
}