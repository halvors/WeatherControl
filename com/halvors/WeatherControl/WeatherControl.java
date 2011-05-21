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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.halvors.WeatherControl.util.ConfigManager;
import com.halvors.WeatherControl.util.WorldConfig;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

/**
 * @author halvors
 */
public class WeatherControl extends JavaPlugin {
    public static String name;
    public static String version;
    
    private final Logger log = Logger.getLogger("Minecraft");
    private PluginManager pm;
    private PluginDescriptionFile pdfFile;

    private final ConfigManager configManager = new ConfigManager(this);
    private final WeatherManager weatherManager = new WeatherManager(this);
    
    private final WeatherControlBlockListener blockListener = new WeatherControlBlockListener(this);
    private final WeatherControlEntityListener entityListener = new WeatherControlEntityListener(this);
    private final WeatherControlPlayerListener playerListener = new WeatherControlPlayerListener(this);
    private final WeatherControlWeatherListener weatherListener = new WeatherControlWeatherListener(this);
    private final WeatherControlWorldListener worldListener = new WeatherControlWorldListener(this);
    
    public static PermissionHandler Permissions;
    
    private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
   
    @Override
    public void onEnable() {
        pm = this.getServer().getPluginManager();
        pdfFile = this.getDescription();
        
        // Load name and version from pdfFile
        name = pdfFile.getName();
        version = pdfFile.getVersion();
        
        // Load configuration
        configManager.load();
        
        // Register our events
        pm.registerEvent(Event.Type.BLOCK_IGNITE, blockListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.SNOW_FORM, blockListener, Event.Priority.Normal, this);
        
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
        
        log(Level.INFO, "version " + version + " is enabled!");
        
        setupPermissions();
    }
    
    @Override
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
    
    public static boolean hasPermissions(Player player, String node) {
        if (Permissions != null) {
            return Permissions.has(player, node);
        } else {
            return player.isOp();
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
    
    public WeatherManager getWeatherManager() {
        return weatherManager;
    }
}