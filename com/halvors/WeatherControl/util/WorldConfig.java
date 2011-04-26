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
    public boolean disableWeather;
    public int defaultWeatherDuration;
    
    
    public boolean disableThunder;
    public int defaultThunderDuration;
    
    public boolean disableLightningStrike;
    public boolean disableLightningStrikeDamage;
    public boolean disableLightningStrikeFire;
    public boolean clickLightningStrike;
    public int clickLightningStrikeItem;
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
        
        disableWeather = config.getBoolean("disableWeather", disableWeather);
        defaultWeatherDuration = config.getInt("defaultWeatherDuration", defaultWeatherDuration);
        
        disableThunder = config.getBoolean("disableThunder", disableThunder);
        defaultThunderDuration = config.getInt("defaultThunderDuration", defaultThunderDuration);
        
        disableLightningStrike = config.getBoolean("disableLightningStrike", disableLightningStrike);
        disableLightningStrikeDamage = config.getBoolean("protection.disableLightningStrikeDamage", disableLightningStrikeDamage);
        disableLightningStrikeFire = config.getBoolean("protection.disableLightningStrikeFire", disableLightningStrikeFire);
        clickLightningStrike = config.getBoolean("clickLightningStrike", clickLightningStrike);
        clickLightningStrikeItem = config.getInt("clickLightningStrikeItem", clickLightningStrikeItem);
    }

    public String getWorldName() {
        return this.worldName;
    }
}