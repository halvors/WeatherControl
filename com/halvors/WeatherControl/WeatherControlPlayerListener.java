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
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

import com.halvors.WeatherControl.util.WorldConfig;

/**
 * Handle events for all Player related events
 * @author halvors
 */
public class WeatherControlPlayerListener extends PlayerListener {
    private final WeatherControl plugin;

    public WeatherControlPlayerListener(WeatherControl instance) {
        this.plugin = instance;
    }   

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
    	Action action = event.getAction();
    	Player player = event.getPlayer();
    	World world = player.getWorld();
    	WorldConfig worldConfig = plugin.getConfigManager().getWorldConfig(world);
    	
		if (worldConfig.lightningEnable) {
			if (WeatherControl.hasPermissions(player, "WeatherControl.lightning")) {
				if (event.hasItem()) {
					int item = worldConfig.lightningLightningStrikeWandItem;
					int count = worldConfig.lightningLightningStrikeWandMultiCount;
					
					if (item != 0) {
						if (event.getItem().getTypeId() == item) {
							if ((action == Action.LEFT_CLICK_BLOCK) || (action == Action.LEFT_CLICK_AIR)) {
								world.strikeLightning(player.getTargetBlock(null, 500).getLocation());
							}
							
							if ((action == Action.RIGHT_CLICK_BLOCK) || (action == Action.RIGHT_CLICK_AIR)) {
								if (count != 0) {
									for (int i = 0; i < count; i++) {
										world.strikeLightning(player.getTargetBlock(null, 500).getLocation());
									}
								} else {
									player.sendMessage(ChatColor.RED + "Error: Count not set in configuration file!");
								}
							}
						}
					} else {
						player.sendMessage(ChatColor.RED + "Error: Wand not set in configuration file!");
					}
				}
    		}
		} else {
			player.sendMessage(ChatColor.RED + "Lightning strike is disabled!");
		}
    }
}