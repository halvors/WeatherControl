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

package org.halvors.weathercontrol.thread;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.halvors.weathercontrol.WeatherControl;
import org.halvors.weathercontrol.util.ConfigurationManager;
import org.halvors.weathercontrol.util.WorldConfiguration;

public class WeatherControlThread implements Runnable {
    private WeatherControl plugin;
    
    private final ConfigurationManager configManager;
    
    public boolean interrupted = false;
    
    public int rainSteps = 0; // 1 step = 5 seconds
    public int rainIntSteps = 0;
    public int thunderSteps = 0; // 1 step = 5 seconds
    public int thunderIntSteps = 0;

    public WeatherControlThread(WeatherControl plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigurationManager();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(5000); // 5 seconds
                
                for (World world : Bukkit.getServer().getWorlds()) {
                	WorldConfiguration worldConfig = configManager.get(world);
                	
                	if (worldConfig.intervalEnable) {
                		if ((rainSteps * 5) >= worldConfig.intervalWeatherInterval) { // 10 seconds
                			if (rainIntSteps == 0) {
                				world.setStorm(true);
                				world.setThundering(false);

                				if (worldConfig.intervalShowMessages) {
                					plugin.log(Level.INFO, "It's now storm in " + world.getName() + ".");
                				}
                			}

                			if ((rainIntSteps * 5) >= worldConfig.intervalWeatherLength) {
                				world.setStorm(false);
                				world.setThundering(false);
                            
                				rainIntSteps = 0;
                				rainSteps = 0;
                            
                				if (worldConfig.intervalShowMessages) {
                					plugin.log(Level.INFO, "It's no longer storm in " + world.getName() + ".");
                				}
                			} else {
                				rainIntSteps++;
                			}

                			if ((thunderSteps * 5) >= worldConfig.intervalThunderInterval) { // 10 seconds
                				if (thunderIntSteps == 0) {
                					world.setThundering(true);
                					
                					if (worldConfig.intervalShowMessages) {
                						plugin.log(Level.INFO, "It's now thunder in " + world.getName() + ".");
                					}
                				}

                				if ((thunderIntSteps * 5) >= worldConfig.intervalThunderLength) {
                					world.setThundering(false);
                                
                					thunderIntSteps = 0;
                					thunderSteps = 0;
                                
                					if (worldConfig.intervalShowMessages) {
                						plugin.log(Level.INFO, "It's no longer thunder in " + world.getName() + ".");
                					}
                				} else {
                					thunderIntSteps++;
                				}
                			} else {
                				world.setThundering(false);
                            
                				thunderSteps++;
                			}
                		} else {
                			world.setStorm(false);
                			world.setThundering(false);

                			rainSteps++;
                    	}
                	}
                }
            } catch (InterruptedException ex) {
                break;
            }

            if (Thread.interrupted() || interrupted) {
                break;
            }
        }
    }
}