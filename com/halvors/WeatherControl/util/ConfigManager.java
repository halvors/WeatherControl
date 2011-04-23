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
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.util.config.Configuration;
import org.bukkit.util.config.ConfigurationNode;

import com.halvors.WeatherControl.WeatherControl;

public class ConfigManager {
	private final WeatherControl plugin;
	
	private File configFile;
    
	// General
	public int LightningStrikeItem;
	
 	// Messages
	
	// Rain
	public String Its_now_raining;
	public String Its_no_longer_raining;
	
	// Thunder
	public String Its_now_thundering;
	public String Its_no_longer_thundering;
	
	// Clear
	public String Its_now_clearing;
	public String Its_already_clear;
	
	public ConfigManager(WeatherControl instance) {
		plugin = instance;
		
	    configFile = new File(plugin.getDataFolder(), "config.yml");
	}
	
	// Load configuration
	public void load() {
		checkConfig();
		
        Configuration config = new Configuration(configFile);
        config.load();
        
        loadGlobals(config);
    }

	// Save configuration
	public void save() {
		Configuration config = new Configuration(configFile);

		saveGlobals(config);
		
		config.save();
	}
	
	// Reload configuration
	public void reload() {
        load();
    }
	
	private void checkConfig() {
        if (!configFile.exists()) {
            try {
                configFile.getParentFile().mkdir();
                configFile.createNewFile();
                OutputStream output = new FileOutputStream(configFile, false);
                InputStream input = ConfigManager.class.getResourceAsStream("config.yml");
                byte[] buf = new byte[8192];
                while (true) {
                    int length = input.read(buf);
                    if (length < 0) {
                        break;
                    }
                    output.write(buf, 0, length);
                }
                input.close();
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
	
	private void loadGlobals(Configuration config) {
		LightningStrikeItem = config.getInt("LightningStrikeItem", LightningStrikeItem);
		
        // Messages
		ConfigurationNode messages = config.getNode("Messages");
		
		// Rain
		Its_now_raining = messages.getString("Its_now_raining", Its_now_raining);
		Its_no_longer_raining = messages.getString("Its_no_longer_raining", Its_no_longer_raining);
		
		// Thunder
		Its_now_thundering = messages.getString("Its_now_thundering", Its_now_thundering);
		Its_no_longer_thundering = messages.getString("Its_no_longer_thundering", Its_no_longer_thundering);
		
		// Clear
		Its_now_clearing = messages.getString("Its_now_clearing", Its_now_clearing);
		Its_already_clear = messages.getString("Its_already_clear", Its_already_clear);
	}
	
    private void saveGlobals(Configuration config) {
    	config.setProperty("LightningStrikeItem", LightningStrikeItem);

        // Messages
		
		// Rain
		config.setProperty("Messages.Its_now_raining", Its_now_raining);
		config.setProperty("Messages.Its_no_longer_raining", Its_no_longer_raining);
		
		// Thunder
		config.setProperty("Messages.Its_now_thundering", Its_now_thundering);
		config.setProperty("Messages.Its_no_longer_thundering", Its_no_longer_thundering);
		
		// Clear
		config.setProperty("Messages.Its_now_clearing", Its_now_clearing);
		config.setProperty("Messages.Its_already_clear", Its_already_clear);
        
        config.save();
    }
}