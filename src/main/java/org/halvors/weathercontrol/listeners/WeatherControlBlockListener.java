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

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.halvors.weathercontrol.WeatherControl;
import org.halvors.weathercontrol.util.ConfigurationManager;
import org.halvors.weathercontrol.util.WorldConfiguration;

/**
 * Handle events for all Block related events
 * 
 * @author halvors
 */
public class WeatherControlBlockListener extends BlockListener {
//	private final WeatherControl plugin;
	
	private final ConfigurationManager configManager;
	
    public WeatherControlBlockListener(final WeatherControl plugin) {
//        this.plugin = plugin;
        this.configManager = plugin.getConfigurationManager();
    }
	
    @Override
	public void onBlockIgnite(BlockIgniteEvent event) {
		if (!event.isCancelled()) {
			Block block = event.getBlock();
			IgniteCause cause = event.getCause();
			World world = block.getWorld();
			WorldConfiguration worldConfig = configManager.get(world);
			
			if ((worldConfig.lightningDisableLightningStrikeFire) && (cause == IgniteCause.LIGHTNING)) {
				event.setCancelled(true);
			}
		}
	}
    
    @Override
    public void onBlockForm(BlockFormEvent event) {
    	if (!event.isCancelled()) {
    		Block block = event.getBlock();
    		Material type = block.getType();
    		World world = block.getWorld();
    		WorldConfiguration worldConfig = configManager.get(world);
    		
    		if (type.equals(Material.SNOW) && worldConfig.disableSnowForm) {
    			event.setCancelled(true);
    		}
    	}
    }
}