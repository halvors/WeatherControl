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

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WeatherControlCommandExecutor implements CommandExecutor {
	private final WeatherControl plugin;

	public WeatherControlCommandExecutor(WeatherControl instance) {
		plugin = instance;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			
			if (args.length == 0) {
				if (WeatherControl.hasPermissions(player, "WeatherControl.help")) {
					showHelp(player);
					
					return true;
				}
			}
        
			if (args[0].equalsIgnoreCase("help")) {
				if (WeatherControl.hasPermissions(player, "WeatherControl.help")) {
					showHelp(player);
					
					return true;
				}
			} else if (args[0].equalsIgnoreCase("rain")) {
				if (WeatherControl.hasPermissions(player, "WeatherControl.rain")) {
					World world = player.getWorld();
					
					if (args.length >= 2) {
						int duration = Integer.parseInt(args[1]);
						
						if (!world.hasStorm()) {
							world.setWeatherDuration(duration);
						}
					}
					
					if (world.hasStorm()) {
						world.setStorm(false);
						player.sendMessage(ChatColor.GREEN + plugin.getConfigManager().Its_no_longer_raining);
					} else {
						world.setStorm(true);
						player.sendMessage(ChatColor.GREEN + plugin.getConfigManager().Its_now_raining);
					}

					return true;
				}
			} else if (args[0].equalsIgnoreCase("thunder")) {
				if (WeatherControl.hasPermissions(player, "WeatherControl.thunder")) {
					World world = player.getWorld();
					int duration;
					
					if (args.length == 2) {
						duration = Integer.parseInt(args[1]);

						if (!world.isThundering()) {
							world.setThunderDuration(duration);
						}
					} else if (args.length >= 3) {
						world = plugin.getServer().getWorld(args[3]);
						duration = Integer.parseInt(args[1]);

						if (!world.isThundering()) {
							world.setThunderDuration(duration);
						}
					}

					if (world.isThundering()) {
						world.setThundering(false);
						player.sendMessage(ChatColor.GREEN + plugin.getConfigManager().Its_no_longer_thundering);
					} else {
						world.setThundering(true);
						player.sendMessage(ChatColor.GREEN + plugin.getConfigManager().Its_now_thundering);
					}

				return true;
				}
			} else if (args[0].equalsIgnoreCase("clear")) {
				if (WeatherControl.hasPermissions(player, "WeatherControl.clear")) {
					World world = player.getWorld();
					
					if (world.hasStorm()) {
						world.setStorm(false);
						player.sendMessage(ChatColor.GREEN + plugin.getConfigManager().Its_now_clearing);
					} else {
						player.sendMessage(ChatColor.RED + plugin.getConfigManager().Its_already_clear);
					}

					return true;
				}
			} else if (args[0].equalsIgnoreCase("lightning")) {
				if (WeatherControl.hasPermissions(player, "WeatherControl.clear")) {
					World world = player.getWorld();
					Location location = null;
					
					if (args.length >= 2) {
						player = plugin.getServer().getPlayer(args[1]);
						world = player.getWorld();
						location = player.getLocation();
						
						player.sendMessage(ChatColor.GREEN + "Player " + player.getName() + " has been striked!"); // TODO: Add to ConfigManager
					} else {
						// TODO: Crosshair
					}
					
					world.strikeLightning(location);
				}
			}
		}
		
		return false;
	}

	private void showHelp(Player player) {
		player.sendMessage(ChatColor.YELLOW + WeatherControl.name + ChatColor.GREEN + " (" + ChatColor.YELLOW + WeatherControl.version + ChatColor.GREEN + ")");
		player.sendMessage(ChatColor.RED + "[]" + ChatColor.WHITE + " Required, " + ChatColor.GREEN + "<>" + ChatColor.WHITE + " Optional");

		String command = "/wc ";
		
		if (WeatherControl.hasPermissions(player, "WeatherControl.help")) {
			player.sendMessage(command + "help" + ChatColor.YELLOW + " - Show help.");
		}
		
		if (WeatherControl.hasPermissions(player, "WeatherControl.rain")) {
			player.sendMessage(command + "rain " + ChatColor.GREEN + "<" + ChatColor.WHITE + "duration" + ChatColor.GREEN + ">" + ChatColor.YELLOW + " - Toogle rain.");
		}
		
		if (WeatherControl.hasPermissions(player, "WeatherControl.thunder")) {
			player.sendMessage(command + "thunder " + ChatColor.GREEN + "<" + ChatColor.WHITE + "duration" + ChatColor.GREEN + ">" + ChatColor.YELLOW + " - Toogle thundering.");
		}
		
		if (WeatherControl.hasPermissions(player, "WeatherControl.clear")) {
			player.sendMessage(command + "clear" + ChatColor.YELLOW + " - Toogle clear.");
		}
		
		if (WeatherControl.hasPermissions(player, "WeatherControl.lightning")) {
			player.sendMessage(command + "lightning " + ChatColor.GREEN + "<" + ChatColor.WHITE + "player" + ChatColor.GREEN + ">" + ChatColor.YELLOW + " - Strike a player.");
		}
	}
}