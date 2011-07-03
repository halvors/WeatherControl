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

import org.bukkit.World;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreeperPowerEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.PigZapEvent;
import org.halvors.weathercontrol.WeatherControl;
import org.halvors.weathercontrol.util.ConfigManager;
import org.halvors.weathercontrol.util.WorldConfig;

/**
 * Handle events for all Entity related events
 * 
 * @author halvors
 */
public class WeatherControlEntityListener extends EntityListener {
//    private final WeatherControl plugin;
    
    private final ConfigManager configManager;
    
    public WeatherControlEntityListener(final WeatherControl plugin) {
//        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
    }
    
    @Override
    public void onEntityDamage(EntityDamageEvent event) {
        if (!event.isCancelled()) {
            if (event instanceof EntityDamageByEntityEvent) {
                Entity entity = event.getEntity();
                DamageCause cause = event.getCause();
                WorldConfig worldConfig = configManager.getWorldConfig(entity.getWorld());
                
                if (entity instanceof Player) {
                    if ((worldConfig.lightningDisableLightningStrikePlayerDamage) && (cause == DamageCause.LIGHTNING)) {
                        event.setCancelled(true);
                    }
                } else if (entity instanceof Creature) {
                    if ((worldConfig.lightningDisableLightningStrikeMobDamage) && (cause == DamageCause.LIGHTNING)) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
    
    @Override
    public void onCreeperPower(CreeperPowerEvent event) {
        if (!event.isCancelled()) {
            World world = event.getEntity().getWorld();
            WorldConfig worldConfig = configManager.getWorldConfig(world);
        
            if (worldConfig.lightningDisableCreeperPower) {
                event.setCancelled(true);
            }
        }
    }
    
    @Override
    public void onPigZap(PigZapEvent event) {
        if (!event.isCancelled()) {
            World world = event.getEntity().getWorld();
            WorldConfig worldConfig = configManager.getWorldConfig(world);
        
            if (worldConfig.lightningDisablePigZap) {
                event.setCancelled(true);
            }
        }
    }
}