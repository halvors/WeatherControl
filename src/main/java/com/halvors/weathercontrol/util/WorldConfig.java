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

package com.halvors.weathercontrol.util;

import java.io.File;
import java.util.logging.Level;

import org.bukkit.util.config.Configuration;

import com.halvors.weathercontrol.WeatherControl;

/**
 * Holds the configuration for individual worlds
 *
 * @author halvors
 */
public class WorldConfig {
//	private WeatherControl plugin;
	
	private ConfigManager configManager;
	
    private String worldName;
    private File configFile;

    /* Configuration data start */
    public boolean disableSnowForm;
    
    public boolean intervalEnable;
    public int intervalWeatherLength;
    public int intervalWeatherInterval;
    public int intervalThunderLength;
    public int intervalThunderInterval;
    
    public boolean weatherEnable;

    public boolean thunderEnable;
    
    public boolean lightningEnable;
    
    public boolean lightningExplosion;
    public int lightningWand;
    public int lightningCount;
    public int lightningDistance;
    
    public boolean lightningDisableCreeperPower;
    public boolean lightningDisablePigZap;
    public boolean lightningDisableLightningStrikePlayerDamage;
    public boolean lightningDisableLightningStrikeMobDamage;
    public boolean lightningDisableLightningStrikeFire;
    /* Configuration data end */

    public WorldConfig(WeatherControl plugin, String worldName) {
//    	this.plugin = plugin;
    	this.configManager = plugin.getConfigManager();
    	this.worldName = worldName;
    	
    	File baseFolder = new File(plugin.getDataFolder(), "worlds/");
        configFile = new File(baseFolder, worldName + ".yml");

        configManager.checkConfig(configFile, "config_world.yml");
        
        load();

        plugin.log(Level.INFO, "Loaded configuration for world '" + worldName + "'");
    }

    /**
     * Load the configuration.
     */
    public void load() {	
        Configuration config = new Configuration(configFile);
        config.load();
        
        disableSnowForm = config.getBoolean("disableSnowForm", disableSnowForm);
        
        intervalEnable = config.getBoolean("interval.enable", intervalEnable);
        intervalWeatherLength = config.getInt("interval.weatherLength", intervalWeatherLength);
        intervalWeatherInterval = config.getInt("interval.weatherInterval", intervalWeatherInterval);
        intervalThunderLength = config.getInt("interval.thunderLength", intervalThunderLength);
        intervalThunderInterval = config.getInt("interval.thunderInterval", intervalThunderInterval);
        
        weatherEnable = config.getBoolean("weather.enable", weatherEnable);

        thunderEnable = config.getBoolean("thunder.enable", thunderEnable);

        lightningEnable = config.getBoolean("lightning.enable", lightningEnable);
        
        lightningExplosion = config.getBoolean("lightning.explosion", lightningExplosion);
        lightningWand = config.getInt("lightning.wand", lightningWand);
        lightningCount = config.getInt("lightning.count", lightningCount);
        lightningDistance = config.getInt("lightning.distance", lightningDistance);
        
        lightningDisableCreeperPower = config.getBoolean("lightning.disableCreeperPower", lightningDisableCreeperPower);
        lightningDisablePigZap = config.getBoolean("lightning.disablePigZap", lightningDisablePigZap);
        lightningDisableLightningStrikePlayerDamage = config.getBoolean("lightning.disableLightningStrikePlayerDamage", lightningDisableLightningStrikePlayerDamage);
        lightningDisableLightningStrikeMobDamage = config.getBoolean("lightning.disableLightningStrikeMobDamage", lightningDisableLightningStrikeMobDamage);
        lightningDisableLightningStrikeFire = config.getBoolean("lightning.disableLightningStrikeFire", lightningDisableLightningStrikeFire);
    }

    /**
     * Get world name.
     * 
     * @return worldName
     */
    public String getWorldName() {
        return this.worldName;
    }
}