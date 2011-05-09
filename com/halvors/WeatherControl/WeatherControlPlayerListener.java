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

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

import com.halvors.WeatherControl.util.WorldConfig;

/**
 * Handle events for all Player related events
 * 
 * @author halvors
 */
public class WeatherControlPlayerListener extends PlayerListener {
    private final WeatherControl plugin;

    public WeatherControlPlayerListener(WeatherControl plugin) {
        this.plugin = plugin;
    }   

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
    	if (!event.isCancelled()) {
    		Player player = event.getPlayer();
    		
    		if (WeatherControl.hasPermissions(player, "WeatherControl.lightning")) {
    			Action action = event.getAction();
    			World world = player.getWorld();
    			WorldConfig worldConfig = plugin.getConfigManager().getWorldConfig(world);
    	
    			int item = worldConfig.lightningWand;
						
    			if (item != 0) {
    				if ((event.hasItem()) && (event.getItem().getTypeId() == item)) {
    					if (worldConfig.lightningEnable) {
    						Block block = player.getTargetBlock(null, 500);
							
    						if ((action == Action.LEFT_CLICK_BLOCK) || (action == Action.LEFT_CLICK_AIR)) {
    							for (int i = 1; i < plugin.getWeatherManager().getWandCount(player.getName()); i++) {
    								world.strikeLightning(block.getLocation());
    							}
    						} else if ((action == Action.RIGHT_CLICK_BLOCK) || (action == Action.RIGHT_CLICK_AIR)) {
    							for (int i = 1; i < plugin.getWeatherManager().getWandCount(player.getName()); i++) {
    								world.strikeLightningEffect(block.getLocation());
    							}
    						}
    					} else {
    						player.sendMessage(ChatColor.RED + "Lightning is disabled!");
    					}
    				}
    			} else {
    				player.sendMessage(ChatColor.RED + "Error: Wand not set in configuration file!");
    			}
    		}
    	}
    }
}