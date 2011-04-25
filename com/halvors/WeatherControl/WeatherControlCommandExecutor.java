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
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.halvors.WeatherControl.util.ConfigManager;
import com.halvors.WeatherControl.util.WorldConfig;

public class WeatherControlCommandExecutor implements CommandExecutor {
	private final WeatherControl plugin;

	private ConfigManager configManager;
	
	public WeatherControlCommandExecutor(WeatherControl instance) {
		plugin = instance;
		
		configManager = plugin.getConfigManager();
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
			} else if (args[0].equalsIgnoreCase("weather")) {
				if (WeatherControl.hasPermissions(player, "WeatherControl.weather")) {
					World world = player.getWorld();
					int duration;
					
					if (args.length == 2) {
						duration = Integer.parseInt(args[1]);
						
						if (!world.hasStorm()) {
							world.setWeatherDuration(duration);
						}
					} else if (args.length >= 3) {
						world = plugin.getServer().getWorld(args[2]);
						duration = Integer.parseInt(args[1]);
						
						if (!world.hasStorm()) {
							world.setWeatherDuration(duration);
						}
					}
					
					if (world != null) {
						if (world.hasStorm()) {
							world.setStorm(false);
							player.sendMessage(ChatColor.GREEN + "It's no longer storm.");
						} else {
							world.setStorm(true);
							player.sendMessage(ChatColor.GREEN + "It's now storm.");
						}
					} else {
						player.sendMessage(ChatColor.RED + "World does not exist!");
					}

					return true;
				}
			} else if (args[0].equalsIgnoreCase("lightning")) {
				if (WeatherControl.hasPermissions(player, "WeatherControl.lightning")) {
					World world = player.getWorld();
					int duration;
					
					if (args.length == 2) {
						duration = Integer.parseInt(args[1]);

						world.setThunderDuration(duration);
					} else if (args.length >= 3) {
						world = plugin.getServer().getWorld(args[2]);
						duration = Integer.parseInt(args[1]);

						world.setThunderDuration(duration);
					}
					
					if (world != null) {
						if (world.isThundering()) {
							world.setThundering(false);
							player.sendMessage(ChatColor.GREEN + "It's no longer lightning.");
						} else {
							world.setThundering(true);
							player.sendMessage(ChatColor.GREEN + "It's now lightning.");
						}
					} else {
						player.sendMessage(ChatColor.RED + "World does not exist!");
					}

					return true;
				}
			} else if (args[0].equalsIgnoreCase("clear")) {
				if (WeatherControl.hasPermissions(player, "WeatherControl.clear")) {
					World world = player.getWorld();
					
					if (args.length >= 2) {
						world = plugin.getServer().getWorld(args[1]);
					}
					
					if (world != null) {
						if (world.hasStorm() || world.isThundering()) {
							world.setStorm(false);
							world.setThundering(false);
						
							player.sendMessage(ChatColor.GREEN + "It's now clearing.");
						} else {
							player.sendMessage(ChatColor.RED + "It's already clear.");
						}
					} else {
						player.sendMessage(ChatColor.RED + "World does not exist!");
					}
	
					return true;
				}
			} else if (args[0].equalsIgnoreCase("strike")) {
				if (WeatherControl.hasPermissions(player, "WeatherControl.strike")) {			
					if (args.length >= 2) {
						Player target = plugin.getServer().getPlayer(args[1]);
						World world = player.getWorld();
						
						world.strikeLightning(target.getLocation());
						
						player.sendMessage(ChatColor.GREEN + "Player " + player.getName() + " has been striked by lightning!");
					} else {
						player.getWorld().strikeLightning(player.getLocation());
					}
					
					return true;
				}
			} else if (args[0].equalsIgnoreCase("reload")) {
				if (WeatherControl.hasPermissions(player, "WeatherControl.reload")) {
					configManager.reload();
					
					player.sendMessage(ChatColor.GREEN + "Reload complete!");
					
					return true;
				}
			}
		}
		
		return false;
	}

	private void showHelp(Player player) {
		player.sendMessage(ChatColor.GREEN + WeatherControl.name + ChatColor.GREEN + " (" + ChatColor.WHITE + WeatherControl.version + ChatColor.GREEN + ")");
		player.sendMessage(ChatColor.RED + "[]" + ChatColor.WHITE + " Required, " + ChatColor.GREEN + "<>" + ChatColor.WHITE + " Optional.");

		String command = "/wc ";
		
		if (WeatherControl.hasPermissions(player, "WeatherControl.help")) {
			player.sendMessage(command + "help" + ChatColor.YELLOW + " - Show help.");
		}
		
		if (WeatherControl.hasPermissions(player, "WeatherControl.weather")) {
			player.sendMessage(command + "weather " + ChatColor.GREEN + "<" + ChatColor.WHITE + "duration" + ChatColor.GREEN + "> <" + ChatColor.WHITE + "world" + ChatColor.GREEN + ">" + ChatColor.YELLOW + " - Toogle weather.");
		}
		
		if (WeatherControl.hasPermissions(player, "WeatherControl.lightning")) {
			player.sendMessage(command + "lightning " + ChatColor.GREEN + "<" + ChatColor.WHITE + "duration" + ChatColor.GREEN + "> <" + ChatColor.WHITE + "world" + ChatColor.GREEN + ">" + ChatColor.YELLOW + " - Toogle lightning.");
		}
		
		if (WeatherControl.hasPermissions(player, "WeatherControl.clear")) {
			player.sendMessage(command + "clear " + ChatColor.GREEN + "<" + ChatColor.WHITE + "world" + ChatColor.GREEN + ">" +  ChatColor.YELLOW + " - Toogle clear.");
		}
		
		if (WeatherControl.hasPermissions(player, "WeatherControl.strike")) {
			player.sendMessage(command + "strike " + ChatColor.GREEN + "<" + ChatColor.WHITE + "player" + ChatColor.GREEN + ">" + ChatColor.YELLOW + " - Lightning strike a yourself/player.");
		}
		
		if (WeatherControl.hasPermissions(player, "WeatherControl.reload")) {
			player.sendMessage(command + "reload" + ChatColor.YELLOW + " - Reload " + WeatherControl.name + ".");
		}
	}
}