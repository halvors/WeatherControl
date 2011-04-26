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
					showHelp(player);
					
					return true;
				}
			}
        
			String subCommand = args[0];
			
			if (subCommand.equalsIgnoreCase("help")) {
				if (WeatherControl.hasPermissions(player, "WeatherControl.help")) {
					showHelp(player);
					
					return true;
				}
			} else if (subCommand.equalsIgnoreCase("weather")) {
				if (WeatherControl.hasPermissions(player, "WeatherControl.weather")) {
					World world = player.getWorld();
					WorldConfig worldConfig = configManager.getWorldConfig(world);
					
					if (!worldConfig.disableWeather) {
						if (args.length == 1) {
							if (world.hasStorm()) {
								player.sendMessage(ChatColor.GREEN + "It will storm in " + world.getWeatherDuration() / 20 + " secounds.");
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
									world.setWeatherDuration(worldConfig.defaultWeatherDuration * 20);
								}
								
								player.sendMessage(ChatColor.GREEN + "It will storm in " + world.getWeatherDuration() / 20 + " secounds.");
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
					
					if (!worldConfig.disableThunder) {
						if (args.length == 1) {
							if (world.isThundering()) {
								player.sendMessage(ChatColor.GREEN + "It will thunder in " + world.getThunderDuration() / 20 + " secounds.");
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
									world.setThunderDuration(worldConfig.defaultThunderDuration * 20);
								}
								
								player.sendMessage(ChatColor.GREEN + "It will thunder in " + world.getThunderDuration() / 20 + " secounds.");
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
			} else if (subCommand.equalsIgnoreCase("lightning")) {
				if (WeatherControl.hasPermissions(player, "WeatherControl.lightning")) {	
					World world = player.getWorld();
					WorldConfig worldConfig = plugin.getConfigManager().getWorldConfig(world);
					
					if (!worldConfig.disableLightningStrike) {
						if (args.length >= 2) {
							Player target = plugin.getServer().getPlayer(args[1]);
							world = player.getWorld();
						
							world.strikeLightning(target.getLocation());
						
							player.sendMessage(ChatColor.GREEN + player.getName() + " has been struck by lightning!");
						} else {
							world.strikeLightning(player.getLocation());
						}
					} else {
						player.sendMessage(ChatColor.RED + "Lightning strike is disabled!");
					}
					
					return true;
				}
			} else if (subCommand.equalsIgnoreCase("wand")) {
				if (WeatherControl.hasPermissions(player, "WeatherControl.wand")) {
					World world = player.getWorld();
					WorldConfig worldConfig = configManager.getWorldConfig(world);
					
					player.getInventory().addItem(new ItemStack(worldConfig.lightningStrikeWandItem, 1));
					player.sendMessage(ChatColor.GREEN + "You got the lightning strike wand.");
					
					return true;
				}
			} else if (subCommand.equalsIgnoreCase("reload")) {
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
			player.sendMessage(command + "weather " + ChatColor.GREEN + "<" + ChatColor.WHITE + "on|off" + ChatColor.GREEN + "> <" + ChatColor.WHITE + "duration" + ChatColor.GREEN + ">" + ChatColor.YELLOW + " - Toogle weather.");
		}
		
		if (WeatherControl.hasPermissions(player, "WeatherControl.thunder")) {
			player.sendMessage(command + "thunder " + ChatColor.GREEN + "<" + ChatColor.WHITE + "on|off" + ChatColor.GREEN + "> <" + ChatColor.WHITE + "duration" + ChatColor.GREEN + ">" + ChatColor.YELLOW + " - Toogle thunder.");
		}
		
		if (WeatherControl.hasPermissions(player, "WeatherControl.clear")) {
			player.sendMessage(command + "clear " + ChatColor.YELLOW + " - Toogle clear.");
		}
		
		if (WeatherControl.hasPermissions(player, "WeatherControl.lightning")) {
			player.sendMessage(command + "lightning " + ChatColor.GREEN + "<" + ChatColor.WHITE + "player" + ChatColor.GREEN + ">" + ChatColor.YELLOW + " - Lightning strike yourself and/or other player.");
		}
		
		if (WeatherControl.hasPermissions(player, "WeatherControl.wand")) {
			player.sendMessage(command + "wand" + ChatColor.YELLOW + " - Give wand item.");
		}
		
		if (WeatherControl.hasPermissions(player, "WeatherControl.reload")) {
			player.sendMessage(command + "reload" + ChatColor.YELLOW + " - Reload " + WeatherControl.name + ".");
		}
	}
}