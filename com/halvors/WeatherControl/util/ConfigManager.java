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

package com.halvors.WeatherControl.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.World;
import org.bukkit.util.config.Configuration;

import com.halvors.WeatherControl.WeatherControl;

/**
 * Represents the global configuration and also delegates configuration
 * for individual worlds.
 * 
 * @author halvors
 */
public class ConfigManager {

    /**
     * Reference to the plugin.
     */
    private final WeatherControl plugin;
    
    /**
     * Holds configurations for different worlds.
     */
    private HashMap<String, WorldConfig> worlds;

    /**
     * Construct the object.
     * 
     * @param plugin
     */
    public ConfigManager(WeatherControl plugin) {
        this.plugin = plugin;
        this.worlds = new HashMap<String, WorldConfig>();
    }

    /**
     * Load the configuration.
     */
    public void load() {
        // Create the default configuration file
        createDefaultConfig(new File(plugin.getDataFolder(), "config.yml"), "config.yml");
        
        Configuration config = plugin.getConfiguration();
        config.load();
        
        // Load configurations for each world
        for (World world : plugin.getServer().getWorlds()) {
            getWorldConfig(world);
        }
    }

    /**
     * Unload the configuration.
     */
    public void unload() {
        worlds.clear();
    }
    
    /**
    * Create a default configuration file from the .jar.
    *
    * @param actual
    * @param defaultName
    */
    public void createDefaultConfig(File actual, String defaultName) {
    	// Make parent directories
        File parent = actual.getParentFile();
        
        if (!parent.exists()) {
        	parent.mkdirs();
        }

        if (!actual.exists()) {
        	InputStream input = ConfigManager.class.getResourceAsStream(defaultName);
            
            if (input != null) {
                FileOutputStream output = null;

                try {
                    output = new FileOutputStream(actual);
                    byte[] buf = new byte[8192];
                    int length = 0;
                    while ((length = input.read(buf)) > 0) {
                        output.write(buf, 0, length);
                    }

                    plugin.log(Level.INFO, "Configuration file written: " + actual.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (input != null) {
                            input.close();
                        }
                    } catch (IOException e) {
                    }

                    try {
                        if (output != null) {
                            output.close();
                        }
                    } catch (IOException e) {
                    }
                }
            }
        }
    }
    
    /**
     * Get the configuration for a world.
     * 
     * @param world
     * @return
     */
    public WorldConfig getWorldConfig(World world) {
        String worldName = world.getName();
        WorldConfig config = worlds.get(worldName);
        
        if (config == null) {
            config = new WorldConfig(plugin, worldName);
            worlds.put(worldName, config);
        }

        return config;
    }
}