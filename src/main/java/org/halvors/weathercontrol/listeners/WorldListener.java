package org.halvors.weathercontrol.listeners;

import java.util.logging.Level;

import org.bukkit.World;
import org.bukkit.event.world.WorldLoadEvent;
import org.halvors.weathercontrol.WeatherControl;
import org.halvors.weathercontrol.util.ConfigurationManager;
import org.halvors.weathercontrol.util.WorldConfiguration;

/**
 * Handle events for all World related events
 * 
 * @author halvors
 */
public class WorldListener extends org.bukkit.event.world.WorldListener {
	private final WeatherControl plugin;
	
	private final ConfigurationManager configManager;
	
	public WorldListener(final WeatherControl plugin) {
		this.plugin = plugin;
		this.configManager = plugin.getConfigurationManager();
	}
	
	@Override
	public void onWorldLoad(WorldLoadEvent event) {
		World world = event.getWorld();
		WorldConfiguration worldConfig = configManager.get(world);
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