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

package com.halvors.WeatherControl.listeners;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.weather.WeatherListener;

import com.halvors.WeatherControl.WeatherControl;
import com.halvors.WeatherControl.util.ConfigManager;
import com.halvors.WeatherControl.util.WorldConfig;

/**
 * Handle events for all Weather related events
 * 
 * @author halvors
 */
public class WeatherControlWeatherListener extends WeatherListener {
//    private final WeatherControl plugin;
    
    private final ConfigManager configManager;
    
    public WeatherControlWeatherListener(final WeatherControl plugin) {
//        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
    }
    
    @Override
    public void onWeatherChange(WeatherChangeEvent event) {
        if (!event.isCancelled()) {
            World world = event.getWorld();
            WorldConfig worldConfig = configManager.getWorldConfig(world);

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
            WorldConfig worldConfig = configManager.getWorldConfig(world);
        
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
        	Location pos = event.getLightning().getLocation();
            World world = event.getWorld();
            WorldConfig worldConfig = configManager.getWorldConfig(world);
        
            if (worldConfig.lightningExplosion) {
            	world.createExplosion(pos.getX(), pos.getY(), pos.getZ(), 4F);
            	
//                WorldServer worldServer = ((CraftWorld) world).getHandle();
//                worldServer.a(null, , pos.getY(), pos.getZ(), 4F);
            }
            
            if (!worldConfig.lightningEnable) {
                event.setCancelled(true);
            }
        }
    }
}