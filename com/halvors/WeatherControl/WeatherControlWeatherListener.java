package com.halvors.WeatherControl;

import org.bukkit.World;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.weather.WeatherListener;

import com.halvors.WeatherControl.util.WorldConfig;

public class WeatherControlWeatherListener extends WeatherListener {
	private final WeatherControl plugin;
	
	public WeatherControlWeatherListener(WeatherControl instance) {
		this.plugin = instance;
	}
	
	@Override
	public void onWeatherChange(WeatherChangeEvent event) {
		World world = event.getWorld();
		WorldConfig worldConfig = plugin.getConfigManager().getWorldConfig(world);
		
		if (worldConfig.disableWeather) {
			event.setCancelled(true);
		}
	}
	
	@Override
	public void onThunderChange(ThunderChangeEvent event) {
		World world = event.getWorld();
		WorldConfig worldConfig = plugin.getConfigManager().getWorldConfig(world);
		
		if (worldConfig.disableThunder) {
			event.setCancelled(true);
		}
	}

	@Override
	public void onLightningStrike(LightningStrikeEvent event) {
		World world = event.getWorld();
		WorldConfig worldConfig = plugin.getConfigManager().getWorldConfig(world);
		
		if (worldConfig.disableLightningStrike) {
			event.setCancelled(true);
		}
	}
}
