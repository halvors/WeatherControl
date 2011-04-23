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

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

import com.halvors.WeatherControl.util.WorldConfig;

public class WeatherControlPlayerListener extends PlayerListener {
    private final WeatherControl plugin;

    public WeatherControlPlayerListener(WeatherControl instance) {
        plugin = instance;
    }
    
    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
    	if (!event.isCancelled()) {
    		Player player = event.getPlayer();
    		Action action = event.getAction();
    		WorldConfig worldConfig = plugin.getConfigManager().getWorldConfig(player.getWorld());
    		
    		
    		if ((WeatherControl.hasPermissions(player, "WeatherControl.lightningstrike")) && (worldConfig.clickLightning)) {
    			if (player.getItemInHand().getTypeId() == worldConfig.clickLightningItem) {
    				if (action == Action.LEFT_CLICK_AIR) {
    					Block block = player.getTargetBlock(null, 300);
                    
    					if (block != null) {
    						player.getWorld().strikeLightning(block.getLocation());
    					}
    				} else if (action == Action.LEFT_CLICK_BLOCK) {
    					Block block = event.getClickedBlock();
    					player.getWorld().strikeLightning(block.getLocation());
    				}
    			}
        	}
    	}
    }
}