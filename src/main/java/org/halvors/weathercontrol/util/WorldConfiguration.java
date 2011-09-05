/* 
 * Copyright (C) 2011 halvors <halvors@skymiastudios.com>
 * Copyright (C) 2011 speeddemon92 <speeddemon92@gmail.com>
 * Copyright (C) 2011 adamonline45 <adamonline45@gmail.com>
 * 
 * This file is part of Lupi.
 * 
 * Lupi is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Lupi is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Lupi.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.halvors.weathercontrol.util;

import java.io.File;
import java.util.logging.Level;

import org.bukkit.util.config.Configuration;
import org.halvors.weathercontrol.WeatherControl;

/**
 * Holds the configuration for individual worlds.
 * 
 * @author halvors
 */
public class WorldConfiguration {
//    private final Lupi plugin;
    
    private final ConfigurationManager configManager;
    
    private String worldName;
    private Configuration config;
    private File configFile;
    
    /* Configuration data start */
    public boolean disableSnowForm;
    public boolean disableIceForm;
    
    public boolean intervalEnable;
    public boolean intervalShowMessages;
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
    
    public boolean lightningDisableChargedCreeper;
    public boolean lightningDisablePigZap;
    public boolean lightningDisableStrikePlayerDamage;
    public boolean lightningDisableStrikeMobDamage;
    public boolean lightningDisableStrikeFire;
    /* Configuration data end */
    
    public WorldConfiguration(WeatherControl plugin, String worldName) {
//        this.plugin = plugin;
        this.configManager = plugin.getConfigurationManager();
        this.worldName = worldName;

        File baseFolder = new File(plugin.getDataFolder(), "worlds/");
        configFile = new File(baseFolder, worldName + ".yml");
        
        configManager.createDefaultConfiguration(configFile, "config_world.yml");
        config = new Configuration(configFile);
        
        load();

        plugin.log(Level.INFO, "Loaded configuration for world '" + worldName + '"');
    }
    
    /**
     * Load the configuration.
     */
    private void load() {
        config.load();
        
        disableSnowForm = config.getBoolean("disableSnowForm", disableSnowForm);
        disableIceForm = config.getBoolean("disableIceForm", disableIceForm);

        intervalEnable = config.getBoolean("interval.enable", intervalEnable);
        intervalShowMessages = config.getBoolean("interval.showMessages", intervalShowMessages);
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
        lightningDisableChargedCreeper = config.getBoolean("lightning.disableCreeperPower", lightningDisableChargedCreeper);
        lightningDisablePigZap = config.getBoolean("lightning.disablePigZap", lightningDisablePigZap);
        lightningDisableStrikePlayerDamage = config.getBoolean("lightning.disableStrikePlayerDamage", lightningDisableStrikePlayerDamage);
        lightningDisableStrikeMobDamage = config.getBoolean("lightning.disableStrikeMobDamage", lightningDisableStrikeMobDamage);
        lightningDisableStrikeFire = config.getBoolean("lightning.disableLightningStrikeFire", lightningDisableStrikeFire);
        
        config.save();
    }
    
    public String getWorldName() {
        return worldName;
    }
}
