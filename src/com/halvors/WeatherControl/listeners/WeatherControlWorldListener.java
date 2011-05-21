package com.halvors.WeatherControl.listeners;

import java.util.logging.Level;

import org.bukkit.World;
import org.bukkit.event.world.WorldListener;
import org.bukkit.event.world.WorldLoadEvent;

import com.halvors.WeatherControl.WeatherControl;
import com.halvors.WeatherControl.util.WorldConfig;

/**
 * Handle events for all World related events
 * 
 * @author halvors
 */
public class WeatherControlWorldListener extends WorldListener {
	private final WeatherControl plugin;
	
	public WeatherControlWorldListener(final WeatherControl plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void onWorldLoad(WorldLoadEvent event) {
		World world = event.getWorld();
		WorldConfig worldConfig = plugin.getConfigManager().getWorldConfig(world);
		String worldName = world.getName();
		
		if (!worldConfig.weatherEnable && world.hasStorm()) {
			world.setStorm(false);
			
			plugin.log(Level.INFO, "Stopped storm in " + worldName);
		}
		
		if (!worldConfig.thunderEnable && world.isThundering()) {
			world.setThundering(false);
			
			plugin.log(Level.INFO, "Stopped thunder in " + worldName);
		}
	}
}