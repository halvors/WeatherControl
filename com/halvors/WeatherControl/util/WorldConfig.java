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
import java.util.logging.Level;

import org.bukkit.util.config.Configuration;

import com.halvors.WeatherControl.WeatherControl;

/**
 * Holds the configuration for individual worlds.
 *
 * @author halvors
 */

public class WorldConfig {
	private final WeatherControl plugin;
	
    private String worldName;
    private File configFile;

    /* Configuration data start */
    public boolean wandLightningStrike;
    public int wandLightningStrikeItem;
    public boolean disableLightningStrikeDamage;
    public boolean disableLightningStrikeFire;
    /* Configuration data end */

    /**
     * Construct the object.
     *
     * @param plugin
     * @param worldName
     */
    
    public WorldConfig(WeatherControl instance, String worldName) {
    	plugin = instance;
    	this.worldName = worldName;
    	
    	File baseFolder = new File(plugin.getDataFolder(), "worlds/");
        configFile = new File(baseFolder, worldName + ".yml");

        plugin.getConfigManager().checkConfig(configFile, "config_world.yml");

        load();

        plugin.log(Level.INFO, "Loaded configuration for world '" + worldName + "'");
    }

    /**
     * Load the configuration.
     */
    
    public void load() {	
        Configuration config = new Configuration(configFile);
        config.load();
        
        wandLightningStrike = config.getBoolean("wandLightningStrike", wandLightningStrike);
        wandLightningStrikeItem = config.getInt("wandLightningStrikeItem", wandLightningStrikeItem);
        
        disableLightningStrikeDamage = config.getBoolean("protection.disableLightningStrikeDamage", disableLightningStrikeDamage);
        disableLightningStrikeFire = config.getBoolean("protection.disableLightningStrikeFire", disableLightningStrikeFire);
    }
    
    public void save() {
    	Configuration config = new Configuration(configFile);
    	
        config.setProperty("wandLightningStrike", wandLightningStrike);
        config.setProperty("wandLightningStrikeItem", wandLightningStrikeItem);
    	
    	config.setProperty("protection.disableLightningStrikeDamage", disableLightningStrikeDamage);
    	config.setProperty("protection.disableLightningStrikeFire", disableLightningStrikeFire);
    	
    	config.save();
    }

    public String getWorldName() {
        return this.worldName;
    }
}