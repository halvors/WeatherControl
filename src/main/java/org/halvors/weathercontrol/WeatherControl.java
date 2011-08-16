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

package org.halvors.weathercontrol;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.halvors.weathercontrol.commands.WeatherControlCommandExecutor;
import org.halvors.weathercontrol.listeners.WeatherControlBlockListener;
import org.halvors.weathercontrol.listeners.WeatherControlEntityListener;
import org.halvors.weathercontrol.listeners.WeatherControlPlayerListener;
import org.halvors.weathercontrol.listeners.WeatherControlWeatherListener;
import org.halvors.weathercontrol.listeners.WeatherControlWorldListener;
import org.halvors.weathercontrol.manager.WandManager;
import org.halvors.weathercontrol.thread.WeatherControlThread;
import org.halvors.weathercontrol.util.ConfigurationManager;
import org.halvors.weathercontrol.util.WorldConfiguration;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

/**
 * @author halvors
 */
public class WeatherControl extends JavaPlugin {
    private final Logger log = Logger.getLogger("Minecraft");
    private PluginManager pm;
    private PluginDescriptionFile desc;
    
    private Thread thread;
    
    private final ConfigurationManager configManager;
    private final WandManager wandManager;
    
    private final WeatherControlBlockListener blockListener;
    private final WeatherControlEntityListener entityListener;
    private final WeatherControlPlayerListener playerListener;
    private final WeatherControlWeatherListener weatherListener;
    private final WeatherControlWorldListener worldListener;
    
    public static PermissionHandler Permissions;
    
    public WeatherControl() {
        this.configManager = new ConfigurationManager(this);
        this.wandManager = new WandManager(this);
        
        this.blockListener = new WeatherControlBlockListener(this);
        this.entityListener = new WeatherControlEntityListener(this);
        this.playerListener = new WeatherControlPlayerListener(this);
        this.weatherListener = new WeatherControlWeatherListener(this);
        this.worldListener = new WeatherControlWorldListener(this);
    }
    
    @Override
    public void onEnable() {
        pm = getServer().getPluginManager();
        desc = getDescription();
        
        // Load configuration
        configManager.load();
        
        // Create our thread
        thread = new Thread(new WeatherControlThread(this), "wc_thread" );
        thread.start();
        
        for (World world : getServer().getWorlds()) {
        	WorldConfiguration worldConfig = configManager.get(world);
        	
        	if (worldConfig.intervalEnable) {
        		world.setThunderDuration(0);
        		world.setWeatherDuration(0);
        		
        		log(Level.INFO, "Disabling vanilla weather.");
        	}
        }
        
        // Register our events
        pm.registerEvent(Event.Type.BLOCK_IGNITE, blockListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_FORM, blockListener, Event.Priority.Normal, this);
        
        pm.registerEvent(Event.Type.ENTITY_DAMAGE, entityListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.CREEPER_POWER, entityListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PIG_ZAP, entityListener, Event.Priority.Normal, this);
        
        pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Event.Priority.Normal, this);
        
        pm.registerEvent(Event.Type.LIGHTNING_STRIKE, weatherListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.WEATHER_CHANGE, weatherListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.THUNDER_CHANGE, weatherListener, Event.Priority.Normal, this);
        
        pm.registerEvent(Event.Type.WORLD_LOAD, worldListener, Event.Priority.Normal, this);
        
        // Register our commands        
        this.getCommand("wc").setExecutor(new WeatherControlCommandExecutor(this));
        
        log(Level.INFO, "version " + getVersion() + " is enabled!");
        
        setupPermissions();
    }
    
    @Override
    public void onDisable() {
        configManager.unload();
        
        try {
            thread.interrupt();
            thread.join();
            log(Level.INFO, "Thread successfully joined.");
        } catch (InterruptedException e) {
        	e.printStackTrace();
        }
        
        log(Level.INFO, "Plugin disabled!");
    }
    
    private void setupPermissions() {
        Plugin permissions = getServer().getPluginManager().getPlugin("Permissions");

        if (Permissions == null) {
            if (permissions != null) {
                Permissions = ((Permissions)permissions).getHandler();
            } else {
                log(Level.INFO, "Permission system not detected, defaulting to OP");
            }
        }
    }
    
    public boolean hasPermissions(Player player, String node) {
        if (Permissions != null) {
            return Permissions.has(player, node);
        } else {
            return player.isOp();
        }
    }
    
    public String getName() {
    	return desc.getName();
    }
    
    public String getVersion() {
    	return desc.getVersion();
    }
    
    public void log(Level level, String msg) {
        this.log.log(level, "[" + getName() + "] " + msg);
    }
    
    public ConfigurationManager getConfigurationManager() {
        return configManager;
    }
    
    public WandManager getWandManager() {
    	return wandManager;
    }
}