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

package com.halvors.WeatherControl;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.halvors.WeatherControl.util.ConfigManager;
import com.halvors.WeatherControl.util.WorldConfig;

public class WeatherControlCommandExecutor implements CommandExecutor {
	private final WeatherControl plugin;

	private ConfigManager configManager;
	
	public WeatherControlCommandExecutor(WeatherControl instance) {
		this.plugin = instance;
		
		configManager = plugin.getConfigManager();
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			
			if (args.length == 0) {
				if (WeatherControl.hasPermissions(player, "WeatherControl.help")) {
					showHelp(player, label);
					
					return true;
				}
			}
			
			String subCommand = args[0];
			
			if (subCommand.equalsIgnoreCase("help")) {
				if (WeatherControl.hasPermissions(player, "WeatherControl.help")) {
					showHelp(player, label);
					
					return true;
				}
			} else if (subCommand.equalsIgnoreCase("weather")) {
				if (WeatherControl.hasPermissions(player, "WeatherControl.weather")) {
					World world = player.getWorld();
					WorldConfig worldConfig = configManager.getWorldConfig(world);
					
					if (worldConfig.weatherEnable) {
						if (args.length == 1) {
							if (world.hasStorm()) {
								player.sendMessage(ChatColor.GREEN + "It will storm in " + world.getWeatherDuration() / 20 + " seconds.");
							} else {
								player.sendMessage(ChatColor.GREEN + "No active storm.");
							
							}
						} else if (args.length >= 2) {
							if (args[1].equalsIgnoreCase("on")) {	
								world.setStorm(true);
								world.setThundering(false);
								
								if (args.length >= 3) {
									int duration = Integer.parseInt(args[2]) * 20;	
									world.setWeatherDuration(duration);
								} else {
									world.setWeatherDuration(worldConfig.weatherDefaultWeatherDuration * 20);
								}
								
								player.sendMessage(ChatColor.GREEN + "It will storm in " + world.getWeatherDuration() / 20 + " seconds.");
							} else if (args[1].equalsIgnoreCase("off")) {
								if (world.hasStorm()) {
									world.setStorm(false);
									world.setThundering(false);
									
									player.sendMessage(ChatColor.GREEN + "It's no longer storm.");
								} else {
									player.sendMessage(ChatColor.GREEN + "It's no active storm!");
								}
							}
						}
					} else {
						player.sendMessage(ChatColor.RED + "Weather is disabled!");
					}
					
					return true;
				}
			} else if (subCommand.equalsIgnoreCase("thunder")) {
				if (WeatherControl.hasPermissions(player, "WeatherControl.thunder")) {
					World world = player.getWorld();
					WorldConfig worldConfig = configManager.getWorldConfig(world);
					
					if (worldConfig.thunderEnable) {
						if (args.length == 1) {
							if (world.isThundering()) {
								player.sendMessage(ChatColor.GREEN + "It will thunder in " + world.getThunderDuration() / 20 + " seconds.");
							} else {
								player.sendMessage(ChatColor.GREEN + "No active thunder.");
							}
						} else if (args.length >= 2) {
							if (args[1].equalsIgnoreCase("on")) {	
								world.setStorm(true);
								world.setThundering(true);
								
								if (args.length >= 3) {
									int duration = Integer.parseInt(args[2]) * 20;	
									world.setThunderDuration(duration);
								} else {
									world.setThunderDuration(worldConfig.thunderDefaultThunderDuration * 20);
								}
								
								player.sendMessage(ChatColor.GREEN + "It will thunder in " + world.getThunderDuration() / 20 + " seconds.");
							} else if (args[1].equalsIgnoreCase("off")) {
								if (world.isThundering()) {
									world.setStorm(false);
									world.setThundering(false);
									
									player.sendMessage(ChatColor.GREEN + "It's no longer thunder.");
								} else {
									player.sendMessage(ChatColor.GREEN + "It's no thunder active!");
								}
							}
						}
					} else {
						player.sendMessage(ChatColor.RED + "Thunder is disabled!");
					}

					return true;
				}
			} else if (subCommand.equalsIgnoreCase("clear")) {
				if (WeatherControl.hasPermissions(player, "WeatherControl.clear")) {
					World world = player.getWorld();
					
					if ((world.hasStorm()) || (world.isThundering())) {
						world.setStorm(false);
						world.setThundering(false);
					
						player.sendMessage(ChatColor.GREEN + "It's now clearing.");
					} else {
						player.sendMessage(ChatColor.RED + "It's already clear.");
					}
						
					return true;
				}
			} else if (subCommand.equalsIgnoreCase("strike")) {
				if (WeatherControl.hasPermissions(player, "WeatherControl.strike")) {
					World world = player.getWorld();
					WorldConfig worldConfig = configManager.getWorldConfig(world);
					int distance = 0;
					
					switch (args.length) {
					case 1:
						world.strikeLightning(player.getLocation());
						player.sendMessage(ChatColor.GREEN + "You have been struck by lightning!");
						break;
						
					case 2:
						distance = worldConfig.lightningLightningStrikeDistance;
						break;
						
					case 3:
						distance = Integer.parseInt(args[2]);
						break;
					}
					
					if (distance != 0) {
						List<Entity> entities = player.getNearbyEntities(distance, distance, distance);
						String entityType = null;
						
						if (args[1].equalsIgnoreCase("creeper")) {
							entityType = "creeper";
							
							for (Entity entity : entities) {
								if (entity instanceof Creeper) {
									world = entity.getWorld();
									world.strikeLightning(entity.getLocation());
		          				}
							}
							
							if (entities.size() > 1) {
								entityType += "s";
							}
						
							player.sendMessage(ChatColor.GREEN + Integer.toString(entities.size()) + " " + entityType + " have been struck by lightning!");
						} else if (args[1].equalsIgnoreCase("pig")) {
							entityType = "pig";
							
							for (Entity entity : entities) {
								if (entity instanceof Pig) {
									world = entity.getWorld();
									world.strikeLightning(entity.getLocation());
		          				}
							}
							
							if (entities.size() > 1) {
								entityType += "s";
							}
						
							player.sendMessage(ChatColor.GREEN + Integer.toString(entities.size()) + " " + entityType + " have been struck by lightning!");
						} else {
							Player target = plugin.getServer().getPlayer(args[1]);
							world = target.getWorld();
							
							world.strikeLightning(target.getLocation());
							player.sendMessage(ChatColor.GREEN + player.getName() + " have been struck by lightning!");
						}
					}
				}
			} else if (subCommand.equalsIgnoreCase("lightning")) {
				if (WeatherControl.hasPermissions(player, "WeatherControl.lightning")) {
					if (args.length == 4) {
						World world = player.getWorld();
						
						double x = Double.parseDouble(args[1]);
 						double y = Double.parseDouble(args[2]);
						double z = Double.parseDouble(args[3]);
						
						world.strikeLightning(new Location(world, x, y, z));
						
						player.sendMessage(ChatColor.GREEN + "Position " + Double.toString(x) + ", " + Double.toString(y) + ", " + Double.toString(z) + " have been struck by lightning!");
					} else {
						player.sendMessage(ChatColor.RED + "You have to specify a valid position!");
					}
				}
			} else if (subCommand.equalsIgnoreCase("wand")) {
				if (WeatherControl.hasPermissions(player, "WeatherControl.wand")) {
					World world = player.getWorld();
					WorldConfig worldConfig = configManager.getWorldConfig(world);
					
					int item = worldConfig.lightningLightningStrikeWandItem;
					
					if (item != 0) {
						player.getInventory().addItem(new ItemStack(item, 1));
						player.sendMessage(ChatColor.GREEN + "You got the lightning strike wand.");
					} else {
						player.sendMessage(ChatColor.RED + "Error: Wand not set in configuration file!");
					}
					
					return true;
				}
			} else if (subCommand.equalsIgnoreCase("reload")) {
				if (WeatherControl.hasPermissions(player, "WeatherControl.reload")) {
					configManager.reload();
					
					player.sendMessage(ChatColor.GREEN + "Reload complete!");
					
					return true;
				}
			} else {
				showHelp(player, label);
			}
		}
		
		return false;
	}

	private void showHelp(Player player, String label) {
		String command = "/" + label + " ";
		
		player.sendMessage(ChatColor.GREEN + WeatherControl.name + ChatColor.GREEN + " (" + ChatColor.WHITE + WeatherControl.version + ChatColor.GREEN + ")");
		player.sendMessage(ChatColor.RED + "[]" + ChatColor.WHITE + " Required, " + ChatColor.GREEN + "<>" + ChatColor.WHITE + " Optional.");

		if (WeatherControl.hasPermissions(player, "WeatherControl.help")) {
			player.sendMessage(command + "help" + ChatColor.YELLOW + " - Show help.");
		}
		
		if (WeatherControl.hasPermissions(player, "WeatherControl.weather")) {
			player.sendMessage(command + "weather " + ChatColor.GREEN + "<" + ChatColor.WHITE + "on|off" + ChatColor.GREEN + "> <" + ChatColor.WHITE + "duration" + ChatColor.GREEN + ">" + ChatColor.YELLOW + " - Show or toogle weather.");
		}
		
		if (WeatherControl.hasPermissions(player, "WeatherControl.thunder")) {
			player.sendMessage(command + "thunder " + ChatColor.GREEN + "<" + ChatColor.WHITE + "on|off" + ChatColor.GREEN + "> <" + ChatColor.WHITE + "duration" + ChatColor.GREEN + ">" + ChatColor.YELLOW + " - Show or toogle thunder.");
		}
		
		if (WeatherControl.hasPermissions(player, "WeatherControl.clear")) {
			player.sendMessage(command + "clear " + ChatColor.YELLOW + " - Toogle clear.");
		}
		
		if (WeatherControl.hasPermissions(player, "WeatherControl.strike")) {
			player.sendMessage(command + "strike " + ChatColor.GREEN + "<" + ChatColor.WHITE + "player|pig|creeper" + ChatColor.GREEN + "> <" + ChatColor.WHITE + "distance" + ChatColor.GREEN + ">" + ChatColor.YELLOW + " - Lightning strike player/pig/creeper.");
		}
		
		if (WeatherControl.hasPermissions(player, "WeatherControl.lightning")) {
			player.sendMessage(command + "lightning " + ChatColor.RED + "[" + ChatColor.WHITE + "x" + ChatColor.RED + "] [" + ChatColor.WHITE + "y" + ChatColor.RED + "] [" + ChatColor.WHITE + "z" + ChatColor.RED + "]" + ChatColor.YELLOW + " - Lightning strike a specific position.");
		}
		
		if (WeatherControl.hasPermissions(player, "WeatherControl.wand")) {
			player.sendMessage(command + "wand" + ChatColor.YELLOW + " - Give you the wand item.");
		}
		
		if (WeatherControl.hasPermissions(player, "WeatherControl.reload")) {
			player.sendMessage(command + "reload" + ChatColor.YELLOW + " - Reload " + WeatherControl.name + ".");
		}
	}
}