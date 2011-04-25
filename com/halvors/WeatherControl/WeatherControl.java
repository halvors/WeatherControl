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

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.halvors.WeatherControl.util.ConfigManager;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class WeatherControl extends JavaPlugin {
	public static String name;
	public static String version;
	
	private Logger log = Logger.getLogger("Minecraft");
	private PluginManager pm;
	private PluginDescriptionFile pdfFile;

	private ConfigManager configManager;
	
	private WeatherControlBlockListener blockListener;
	private WeatherControlEntityListener entityListener;
	private WeatherControlPlayerListener playerListener;
	
    public static PermissionHandler Permissions;
    
    private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
    
    public void onEnable() {
    	pm = this.getServer().getPluginManager();
    	pdfFile = this.getDescription();
    	
    	configManager = new ConfigManager(this);
    	
    	blockListener = new WeatherControlBlockListener(this);
    	entityListener = new WeatherControlEntityListener(this);
    	playerListener = new WeatherControlPlayerListener(this);
    	
        // Load name and version from pdfFile
        name = pdfFile.getName();
        version = pdfFile.getVersion();
        
        // Load configuration
        configManager.load();
        
        for (World world : this.getServer().getWorlds()) {
    		configManager.getWorldConfig(world).load();
    	}
        
        // Register our events Type.
        pm.registerEvent(Event.Type.BLOCK_IGNITE, blockListener, Priority.Normal, this);
        
        pm.registerEvent(Event.Type.ENTITY_DAMAGE, entityListener, Priority.Normal, this);
        
        pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Priority.Normal, this);
        
		// Register our commands
        getCommand("wc").setExecutor(new WeatherControlCommandExecutor(this));
		
        log(Level.INFO, "version " + version + " is enabled!");
        
        setupPermissions();
    }
    
    public void onDisable() {
    	configManager.save();
    	
    	log(Level.INFO, "Plugin disabled!");
    }
    
    private void setupPermissions() {
    	Plugin permissions = this.getServer().getPluginManager().getPlugin("Permissions");

        if (Permissions == null) {
        	if (permissions != null) {
            	Permissions = ((Permissions)permissions).getHandler();
            } else {
            	log(Level.INFO, "Permission system not detected, defaulting to OP");
            }
        }
    }

    public static boolean hasPermissions(Player p, String s) {
        if (Permissions != null) {
            return Permissions.has(p, s);
        } else {
            return p.isOp();
        }
    }
    
    public boolean isDebugging(final Player player) {
        if (debugees.containsKey(player)) {
            return debugees.get(player);
        } else {
            return false;
        }
    }

    public void setDebugging(final Player player, final boolean value) {
        debugees.put(player, value);
    }
    
    public void log(Level level, String msg) {
        this.log.log(level, "[" + name + "] " + msg);
    }
    
    public ConfigManager getConfigManager() {
    	return configManager;
    }
}