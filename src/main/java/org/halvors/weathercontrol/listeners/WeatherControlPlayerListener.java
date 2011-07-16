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

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.halvors.weathercontrol.WeatherControl;
import org.halvors.weathercontrol.manager.WandManager;
import org.halvors.weathercontrol.util.ConfigurationManager;
import org.halvors.weathercontrol.util.WorldConfiguration;

/**
 * Handle events for all Player related events
 * 
 * @author halvors
 */
public class WeatherControlPlayerListener extends PlayerListener {
    private final WeatherControl plugin;

    private final ConfigurationManager configManager;
    private final WandManager wandManager;
    
    public WeatherControlPlayerListener(final WeatherControl plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigurationManager();
        this.wandManager = plugin.getWandManager();
    }   

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        Player player = event.getPlayer();
        World world = player.getWorld();
        WorldConfiguration worldConfig = configManager.get(world);
        
        if (event.hasItem()) {
            if (plugin.hasPermissions(player, "WeatherControl.wand")) {
            	int item = worldConfig.lightningWand;
                int count = worldConfig.lightningCount;
                    
                if (wandManager.hasWandCount(player.getName())) {
                	count = wandManager.getWandCount(player.getName());
                }
            
                if (item != 0) {
                	if (event.getItem().getTypeId() == item) {
                		if (worldConfig.lightningEnable) {
                			Location pos = player.getTargetBlock(null, 120).getLocation();
                        
                            if (action.equals(Action.LEFT_CLICK_BLOCK) || action.equals(Action.LEFT_CLICK_AIR)) {
                            	for (int i = 0; i < count; i++) {
                            		world.strikeLightning(pos);
                            	}
                            } else if (action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_AIR)) {
                            	for (int i = 0; i < count; i++) {
                            		world.strikeLightningEffect(pos);
                            	}
                            }
                		} else {
                           player.sendMessage(ChatColor.RED + configManager.Lightning_is_disabled);
                        }
                    }
            	}
            }
        }
    }
}