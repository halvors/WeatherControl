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

import java.util.HashMap;
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
	
	private World senderWorld = null;
	
	public WeatherControlCommandExecutor(WeatherControl plugin) {
		this.plugin = plugin;
		
		configManager = plugin.getConfigManager();
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (sender instanceof Player) ? (Player)sender : null;
        boolean isPlayer = (sender instanceof Player);
		String consoleError = "This command is not avaliable from console.";
		
		if (args.length == 0) {
			if (WeatherControl.hasPermissions(sender, "WeatherControl.help")) {
				showHelp(sender, label);
				
				return true;
			}
		}
		
		if (args.length >= 1) {
			String subCommand = args[0];
			
			if (subCommand.equalsIgnoreCase("help")) {
				if (WeatherControl.hasPermissions(sender, "WeatherControl.help")) {
					showHelp(sender, label);
					
					return true;
				}
			} else if (subCommand.equalsIgnoreCase("weather")) {
				if (WeatherControl.hasPermissions(sender, "WeatherControl.weather")) {
					World world = null;
					
					if (isPlayer) {
						world = player.getWorld();
					} else {
						world = senderWorld;
					}
					
					WorldConfig worldConfig = configManager.getWorldConfig(world);
					
					if (worldConfig.weatherEnable) {
						if (args.length == 1) {
							if (world.hasStorm()) {
								sender.sendMessage(ChatColor.GREEN + "It will storm in " + formatTime(world.getWeatherDuration() / 20) + " .");
							} else {
								world.setStorm(true);
								world.setThundering(false);
								
								sender.sendMessage(ChatColor.GREEN + "It will now storm in " + formatTime(world.getWeatherDuration() / 20) + " .");
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
								
								sender.sendMessage(ChatColor.GREEN + "It will storm in " + world.getWeatherDuration() / 20 + " seconds.");
							} else if (args[1].equalsIgnoreCase("off")) {
								if (world.hasStorm()) {
									world.setStorm(false);
									world.setThundering(false);
								
									sender.sendMessage(ChatColor.GREEN + "It's no longer storm.");
								} else {
									sender.sendMessage(ChatColor.GREEN + "It's no active storm!");
								}
							}
						}
					} else {
						sender.sendMessage(ChatColor.RED + "Weather is disabled!");
					}
					
					return true;
				}
			} else if (subCommand.equalsIgnoreCase("thunder")) {
				if (WeatherControl.hasPermissions(sender, "WeatherControl.thunder")) {
					World world = player.getWorld();
					WorldConfig worldConfig = configManager.getWorldConfig(world);
						
					if (worldConfig.thunderEnable) {
						if (args.length == 1) {
							if (world.isThundering()) {
								sender.sendMessage(ChatColor.GREEN + "It will thunder in " + world.getThunderDuration() / 20 + " seconds.");
							} else {
								sender.sendMessage(ChatColor.GREEN + "No active thunder.");
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
								
								sender.sendMessage(ChatColor.GREEN + "It will thunder in " + world.getThunderDuration() / 20 + " seconds.");
							} else if (args[1].equalsIgnoreCase("off")) {
								if (world.isThundering()) {
									world.setStorm(false);
									world.setThundering(false);
									
									sender.sendMessage(ChatColor.GREEN + "It's no longer thunder.");
								} else {
									sender.sendMessage(ChatColor.GREEN + "It's no thunder active!");
								}
							}
						}
					} else {
						sender.sendMessage(ChatColor.RED + "Thunder is disabled!");
					}
					
					return true;
				}
			} else if (subCommand.equalsIgnoreCase("clear")) {
				if (WeatherControl.hasPermissions(sender, "WeatherControl.clear")) {
					World world = player.getWorld();
				
					if ((world.hasStorm()) || (world.isThundering())) {
						world.setStorm(false);
						world.setThundering(false);
				
						sender.sendMessage(ChatColor.GREEN + "It's now clearing.");
					} else {
						sender.sendMessage(ChatColor.RED + "It's already clear.");
					}
					
					return true;
				}
			} else if (subCommand.equalsIgnoreCase("strike")) {
				if (WeatherControl.hasPermissions(sender, "WeatherControl.strike")) {
					World world = player.getWorld();
					WorldConfig worldConfig = configManager.getWorldConfig(world);
					
					if (worldConfig.lightningEnable) {
						Player target = null;
						
						switch (args.length) {
						case 1:
							if (isPlayer) {
								target = player;
								sender.sendMessage(ChatColor.GREEN + "You have been struck by lightning!");
							} else {
								sender.sendMessage(ChatColor.RED + consoleError);
							}
							break;
							
						case 2:
							target = plugin.getServer().getPlayer(args[1]);
								
							if (target != null) {
								sender.sendMessage(ChatColor.GREEN + target.getName() + " have been struck by lightning!");
							} else {
								sender.sendMessage(ChatColor.RED + "sender does not exist!");
							}
							break;
						}
							
						if (target != null) {
							target.getWorld().strikeLightning(target.getLocation());
						}
					} else {
						sender.sendMessage(ChatColor.RED + "Lightning is disabled!");
					}
				}
			} else if (subCommand.equalsIgnoreCase("strikemob")) {
				if (WeatherControl.hasPermissions(sender, "WeatherControl.strikemob")) {	
					if (isPlayer) {
						World world = player.getWorld();
						WorldConfig worldConfig = plugin.getConfigManager().getWorldConfig(world);
						
						if (worldConfig.lightningEnable) {
							if (args.length >= 2) {
								String entityType = null;
								int distance = 0;
							
								if (args.length == 3) {
									distance = Integer.parseInt(args[2]);
								} else {
									distance = worldConfig.lightningLightningStrikeDistance;
								}
							
								List<Entity> entities = player.getNearbyEntities(distance, distance, distance);
					
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
									
									sender.sendMessage(ChatColor.GREEN + Integer.toString(entities.size()) + " " + entityType + " have been struck by lightning!");
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
				
									sender.sendMessage(ChatColor.GREEN + Integer.toString(entities.size()) + " " + entityType + " have been struck by lightning!");
								} else {
									sender.sendMessage(ChatColor.RED + consoleError);
								}
							}
						}
					} else {
						sender.sendMessage(ChatColor.RED + "Lightning strike is disabled!");
					}
				}
			} else if (subCommand.equalsIgnoreCase("strikepos")) {
				if (WeatherControl.hasPermissions(sender, "WeatherControl.lightning")) {
					World world = player.getWorld();
					WorldConfig worldConfig = plugin.getConfigManager().getWorldConfig(world);
						
					if (worldConfig.lightningEnable) {
						if (args.length == 4) {
							double x = Double.parseDouble(args[1]);
							double y = Double.parseDouble(args[2]);
							double z = Double.parseDouble(args[3]);
						
							world.strikeLightning(new Location(world, x, y, z));
						
							sender.sendMessage(ChatColor.GREEN + "Position " + Double.toString(x) + ", " + Double.toString(y) + ", " + Double.toString(z) + " have been struck by lightning!");
						} else {
							sender.sendMessage(ChatColor.RED + "You have to specify a valid position!");
						}
					} else {
						sender.sendMessage(ChatColor.RED + "Lightning strike is disabled!");
					}
				}
			} else if (subCommand.equalsIgnoreCase("wand")) {
				if (WeatherControl.hasPermissions(sender, "WeatherControl.wand")) {
					if (isPlayer) {
						World world = player.getWorld();
						WorldConfig worldConfig = configManager.getWorldConfig(world);
					
						int item = worldConfig.lightningLightningStrikeWandItem;
				
						if (item != 0) {
							player.getInventory().addItem(new ItemStack(item, 1));
							sender.sendMessage(ChatColor.GREEN + "You got the lightning strike wand.");
						} else {
							sender.sendMessage(ChatColor.RED + "Error: Wand not set in configuration file!");
						}
					} else {
						sender.sendMessage(ChatColor.RED + consoleError);
					}
					
					return true;
				}
			} else if (subCommand.equalsIgnoreCase("world")) {
				if (WeatherControl.hasPermissions(sender, "WeatherControl.world")) {
					if (args.length >= 1) {
						senderWorld = plugin.getServer().getWorld(args[1]);
						
						sender.sendMessage(ChatColor.GREEN + "Switched to world '" + senderWorld.getName() + "'.");
					}
					
					return true;
				}
			} else {
				showHelp(sender, label);
			}
		}
		
		return false;
	}

	private void showHelp(CommandSender sender, String label) {
		String command = "/" + label + " ";
		
		sender.sendMessage(ChatColor.GREEN + WeatherControl.name + ChatColor.GREEN + " (" + ChatColor.WHITE + WeatherControl.version + ChatColor.GREEN + ")");
		sender.sendMessage(ChatColor.RED + "[]" + ChatColor.WHITE + " Required, " + ChatColor.GREEN + "<>" + ChatColor.WHITE + " Optional.");

		if (WeatherControl.hasPermissions(sender, "WeatherControl.help")) {
			sender.sendMessage(command + "help" + ChatColor.YELLOW + " - Show help.");
		}
		
		if (WeatherControl.hasPermissions(sender, "WeatherControl.weather")) {
			sender.sendMessage(command + "weather " + ChatColor.GREEN + "<" + ChatColor.WHITE + "on|off" + ChatColor.GREEN + "> <" + ChatColor.WHITE + "duration" + ChatColor.GREEN + ">" + ChatColor.YELLOW + " - Show or toogle weather.");
		}
		
		if (WeatherControl.hasPermissions(sender, "WeatherControl.thunder")) {
			sender.sendMessage(command + "thunder " + ChatColor.GREEN + "<" + ChatColor.WHITE + "on|off" + ChatColor.GREEN + "> <" + ChatColor.WHITE + "duration" + ChatColor.GREEN + ">" + ChatColor.YELLOW + " - Show or toogle thunder.");
		}
		
		if (WeatherControl.hasPermissions(sender, "WeatherControl.clear")) {
			sender.sendMessage(command + "clear " + ChatColor.YELLOW + " - Toogle clear.");
		}
		
		if (WeatherControl.hasPermissions(sender, "WeatherControl.strike")) {
			sender.sendMessage(command + "strike " + ChatColor.GREEN + "<" + ChatColor.WHITE + "sender" + ChatColor.GREEN + ">" + ChatColor.YELLOW + " - Lightning strike you or/and other players.");
		}
		
		if (WeatherControl.hasPermissions(sender, "WeatherControl.strikemob")) {
			sender.sendMessage(command + "strikemob " + ChatColor.RED + "[" + ChatColor.WHITE + "pig|creeper" + ChatColor.RED + "] " + ChatColor.GREEN + "<" + ChatColor.WHITE + "distance" + ChatColor.GREEN + ">" + ChatColor.YELLOW + " - Lightning strike mob.");
		}
		
		if (WeatherControl.hasPermissions(sender, "WeatherControl.strikepos")) {
			sender.sendMessage(command + "strikepos " + ChatColor.RED + "[" + ChatColor.WHITE + "x" + ChatColor.RED + "] [" + ChatColor.WHITE + "y" + ChatColor.RED + "] [" + ChatColor.WHITE + "z" + ChatColor.RED + "]" + ChatColor.YELLOW + " - Lightning strike a specific position.");
		}
		
		if (WeatherControl.hasPermissions(sender, "WeatherControl.wand")) {
			sender.sendMessage(command + "wand" + ChatColor.YELLOW + " - Give you the wand item.");
		}
		
		if (WeatherControl.hasPermissions(sender, "WeatherControl.reload")) {
			sender.sendMessage(command + "reload" + ChatColor.YELLOW + " - Reload " + WeatherControl.name + ".");
		}
	}
	
	private String formatTime(int seconds) {
		if (seconds >= 60) {
			return Integer.toString(seconds) + " secounds";
		} else {
			return Integer.toString(seconds / 60) + " minutes";
		}
	}
}