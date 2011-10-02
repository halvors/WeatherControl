package org.halvors.weathercontrol.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class WeatherControlUtils {
	/**
     * Get the best matching player.
     * 
     * @param name
     * @return the Player
     */
    public static Player getPlayer(String name) {
        return Bukkit.getServer().matchPlayer(name).get(0);
    }
}
