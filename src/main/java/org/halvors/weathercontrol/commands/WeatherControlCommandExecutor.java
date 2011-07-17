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

package org.halvors.weathercontrol.commands;

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
import org.halvors.weathercontrol.WeatherControl;
import org.halvors.weathercontrol.manager.WandManager;
import org.halvors.weathercontrol.util.ConfigurationManager;
import org.halvors.weathercontrol.util.WeatherControlUtil;
import org.halvors.weathercontrol.util.WeatherUtil;
import org.halvors.weathercontrol.util.WorldConfiguration;

/**
 * Handle commands
 * 
 * @author halvors
 */
public class WeatherControlCommandExecutor implements CommandExecutor {
    private final WeatherControl plugin;

    private final ConfigurationManager configManager;

    public WeatherControlCommandExecutor(final WeatherControl plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigurationManager();
    }
    
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	if (args.length == 0) {
            if (sender.hasPermission("weathercontrol.weather.help")) {
                showHelp(sender, label);
                
                return true;
            }
        } else {
            String subCommand = args[0];

            if (subCommand.equalsIgnoreCase("help")) {
            	if (sender.hasPermission("weathercontrol.weather.help")) {
            		showHelp(sender, label);
                        
            		return true;
            	}
            }
            
            if (sender instanceof Player) {
            	Player player = (Player) sender;
            	
            	if (subCommand.equalsIgnoreCase("status")) {
            		if (player.hasPermission("weathercontrol.weather.status")) {
            			World world = player.getWorld();
                        
                    	if (world.isThundering() && world.hasStorm()) {
                    	player.sendMessage(ChatColor.GREEN + configManager.It_is_thundering);
                    	} else if (world.hasStorm()) {
                    		player.sendMessage(ChatColor.GREEN + configManager.It_is_storming);
                    	} else {
                            player.sendMessage(ChatColor.GREEN + configManager.It_is_clear);
                        }
                        
                        return true;
                    }
                } else if (subCommand.equalsIgnoreCase("weather")) {
                    if (player.hasPermission("weathercontrol.weather.weather")) {
                        World world = player.getWorld();                    
                        WorldConfiguration worldConfig = configManager.get(world);
                        
                        if (worldConfig.weatherEnable) {
                            if (args.length == 1) {
                                if (world.hasStorm()) {
                                    player.sendMessage(ChatColor.GREEN + configManager.It_will_storm_for_another.replace("<duration>", WeatherUtil.formatTime(world.getWeatherDuration() / 20)));
                                } else {
                                    world.setStorm(true);
                                    world.setThundering(false);
                                    
                                    player.sendMessage(ChatColor.GREEN + configManager.It_will_now_storm_for.replace("<duration>", WeatherUtil.formatTime(world.getWeatherDuration() / 20)));
                                }
                            } else if (args.length >= 2) {
                                if (args[1].equalsIgnoreCase("on")) {    
                                    world.setStorm(true);
                                    world.setThundering(false);
                                        
                                    if (args.length == 3) {
                                        int duration = Integer.parseInt(args[2]) * 20;    
                                        world.setWeatherDuration(duration);
                                    }
                                    
                                    player.sendMessage(ChatColor.GREEN + configManager.It_will_now_storm_for.replace("<duration>", WeatherUtil.formatTime(world.getWeatherDuration() / 20)));
                                } else if (args[1].equalsIgnoreCase("off")) {
                                    if (world.hasStorm()) {
                                        world.setStorm(false);
                                        world.setThundering(false);
                                    
                                        player.sendMessage(ChatColor.GREEN + configManager.It_is_no_longer_storm);
                                    } else {
                                    	player.sendMessage(ChatColor.GREEN + configManager.It_is_no_active_storm);
                                    }
                                }
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + configManager.Weather_is_disabled);
                        }
                        
                        return true;
                    }
                } else if (subCommand.equalsIgnoreCase("thunder")) {
                    if (player.hasPermission("weathercontrol.weather.thunder")) {
                        World world = player.getWorld();
                        WorldConfiguration worldConfig = configManager.get(world);
                            
                        if (worldConfig.thunderEnable) {
                            if (args.length == 1) {
                                if (world.isThundering()) {
                                    player.sendMessage(ChatColor.GREEN + configManager.It_will_thunder_for_another.replace("<duration>", WeatherUtil.formatTime(world.getThunderDuration() / 20)));
                                } else {
                                    world.setStorm(true);
                                    world.setThundering(true);
                                
                                    player.sendMessage(ChatColor.GREEN + configManager.It_will_now_thunder_for.replace("<duration>", WeatherUtil.formatTime(world.getThunderDuration() / 20)));
                                }
                            } else if (args.length >= 2) {
                                if (args[1].equalsIgnoreCase("on")) {    
                                    world.setStorm(true);
                                    world.setThundering(true);
                                    
                                    if (args.length >= 3) {
                                        int duration = Integer.parseInt(args[2]) * 20;
                                        world.setThunderDuration(duration);
                                    }
                                    
                                    player.sendMessage(ChatColor.GREEN + configManager.It_will_now_thunder_for.replace("<duration>", WeatherUtil.formatTime(world.getThunderDuration() / 20)));
                                } else if (args[1].equalsIgnoreCase("off")) {
                                    if (world.isThundering()) {
                                        world.setStorm(false);
                                        world.setThundering(false);
                                        
                                        player.sendMessage(ChatColor.GREEN + configManager.It_is_no_longer_thunder);
                                    } else {
                                        player.sendMessage(ChatColor.GREEN + configManager.It_is_no_active_thunder);
                                    }
                                }
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + configManager.Thunder_is_disabled);
                        }
                        
                        return true;
                    }
                } else if (subCommand.equalsIgnoreCase("clear")) {
                    if (player.hasPermission("weathercontrol.weather.clear")) {
                        World world = player.getWorld();
                    
                        if ((world.hasStorm()) || (world.isThundering())) {
                            world.setStorm(false);
                            world.setThundering(false);
                    
                            player.sendMessage(ChatColor.GREEN + configManager.It_is_now_clearing);
                        } else {
                            player.sendMessage(ChatColor.RED + configManager.It_is_already_clear);
                        }
                        
                        return true;
                    }
                } else if (subCommand.equalsIgnoreCase("strike")) {
                    if (player.hasPermission("weathercontrol.weather.strike")) {
                        World world = player.getWorld();
                        WorldConfiguration worldConfig = configManager.get(world);
                        
                        if (worldConfig.lightningEnable) {
                            Player target = null;
                            
                            switch (args.length) {
                            case 1:
                                target = player;
                                player.sendMessage(ChatColor.GREEN + configManager.You_have_been_struck_by_lightning);
                                break;
                                
                            case 2:
                                target = WeatherControlUtil.getPlayer(args[1]);
                                    
                                if (target != null) {
                                    player.sendMessage(ChatColor.GREEN + configManager.Player_have_been_struck_by_lightning.replace("<player>", target.getName()));
                                } else {
                                    player.sendMessage(ChatColor.RED + configManager.Player_does_not_exist);
                                }
                                break;
                            }
                                
                            if (target != null) {
                                target.getWorld().strikeLightning(target.getLocation());
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + configManager.Lightning_is_disabled);
                        }
                    }
                } else if (subCommand.equalsIgnoreCase("strikemob")) {
                    if (player.hasPermission("weathercontrol.weather.strikemob")) {    
                        World world = player.getWorld();
                        WorldConfiguration worldConfig = configManager.get(world);
                            
                        if (worldConfig.lightningEnable) {
                            if (args.length >= 2) {
                                String entityType = null;
                                int distance = 0;
                                
                                if (args.length == 3) {
                                    distance = Integer.parseInt(args[2]);
                                } else {
                                    distance = worldConfig.lightningDistance;
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
                                        
                                    player.sendMessage(ChatColor.GREEN + configManager.Have_been_struck_by_lightning.replace("<count>", Integer.toString(entities.size())).replace("<type>", entityType));
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
                    
                                    player.sendMessage(ChatColor.GREEN + configManager.Have_been_struck_by_lightning.replace("<count>", Integer.toString(entities.size())).replace("<type>", entityType));
                                }
                            }
                        } else {    
                            player.sendMessage(ChatColor.RED + configManager.Lightning_is_disabled);
                        }
                    }
                } else if (subCommand.equalsIgnoreCase("strikepos")) {
                    if (player.hasPermission("weathercontrol.weather.strikepos")) {
                        World world = player.getWorld();
                        WorldConfiguration worldConfig = configManager.get(world);
                                
                        if (worldConfig.lightningEnable) {
                            if (args.length == 4) {
                                double x = Double.parseDouble(args[1]);
                                double y = Double.parseDouble(args[2]);
                                double z = Double.parseDouble(args[3]);
                                
                                world.strikeLightning(new Location(world, x, y, z));
                                
                                player.sendMessage(ChatColor.GREEN + configManager.Position_has_been_struck_by_lightning.replace("<position>", Double.toString(x) + ", " + Double.toString(y) + ", " + Double.toString(z)));
                            } else {
                                player.sendMessage(ChatColor.RED + configManager.You_have_to_specify_a_valid_position);
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + configManager.Lightning_is_disabled);
                        }
                    }
                } else if (subCommand.equalsIgnoreCase("wand")) {
                    if (player.hasPermission("weathercontrol.weather.wand")) {
                        World world = player.getWorld();
                        WorldConfiguration worldConfig = configManager.get(world);
                        
                        if (args.length == 1) {
                            int item = worldConfig.lightningWand;
                            
                            if (item != 0) {
                                player.getInventory().addItem(new ItemStack(item, 1));
                                player.sendMessage(ChatColor.GREEN + configManager.You_got_the_lightning_strike_wand);
                            } else {
                                player.sendMessage(ChatColor.RED + configManager.Wand_not_set_in_configuration_file);
                            }
                        } else if (args.length == 2) {
                            int count = Integer.parseInt(args[1]);
                            int maxCount = 64;
                            
                            if (count <= maxCount) {
                            	WandManager.addWandCount(player, count);
                            	
                            	player.sendMessage(ChatColor.RED + configManager.Maximum_wand_count_is.replace("<maxcount>", Integer.toString(maxCount)));
                            } else {
                            	player.sendMessage(ChatColor.GREEN + configManager.Wand_count_set_to.replace("<count>", Integer.toString(count)));
                            }
                        }
                        
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    private void showHelp(CommandSender sender, String label) {
        String command = "/" + label + " ";
        
        sender.sendMessage(ChatColor.YELLOW + plugin.getName() + ChatColor.GREEN + " (" + ChatColor.WHITE + plugin.getVersion() + ChatColor.GREEN + ")");
        sender.sendMessage(ChatColor.RED + "[]" + ChatColor.WHITE + " Required, " + ChatColor.GREEN + "<>" + ChatColor.WHITE + " Optional.");

        if (sender.hasPermission("weathercontrol.weather.help")) {
            sender.sendMessage(command + "help" + ChatColor.YELLOW + " - Show help.");
        }
        
        if (sender.hasPermission("weathercontrol.weather.status")) {
            sender.sendMessage(command + "status" + ChatColor.YELLOW + " - Show weather status.");
        }
        
        if (sender.hasPermission("weathercontrol.weather.weather")) {
            sender.sendMessage(command + "weather " + ChatColor.GREEN + "<" + ChatColor.WHITE + "on|off" + ChatColor.GREEN + "> <" + ChatColor.WHITE + "duration" + ChatColor.GREEN + ">" + ChatColor.YELLOW + " - Show or toogle weather.");
        }
        
        if (sender.hasPermission("weathercontrol.weather.thunder")) {
            sender.sendMessage(command + "thunder " + ChatColor.GREEN + "<" + ChatColor.WHITE + "on|off" + ChatColor.GREEN + "> <" + ChatColor.WHITE + "duration" + ChatColor.GREEN + ">" + ChatColor.YELLOW + " - Show or toogle thunder.");
        }
        
        if (sender.hasPermission("weathercontrol.weather.clear")) {
            sender.sendMessage(command + "clear " + ChatColor.YELLOW + " - Toogle clear.");
        }
        
        if (sender.hasPermission("weathercontrol.weather.strike")) {
            sender.sendMessage(command + "strike " + ChatColor.GREEN + "<" + ChatColor.WHITE + "player" + ChatColor.GREEN + ">" + ChatColor.YELLOW + " - Lightning strike you or/and other senders.");
        }
        
        if (sender.hasPermission("weathercontrol.weather.strikemob")) {
            sender.sendMessage(command + "strikemob " + ChatColor.RED + "[" + ChatColor.WHITE + "pig|creeper" + ChatColor.RED + "] " + ChatColor.GREEN + "<" + ChatColor.WHITE + "distance" + ChatColor.GREEN + ">" + ChatColor.YELLOW + " - Lightning strike mob.");
        }
        
        if (sender.hasPermission("weathercontrol.weather.strikepos")) {
            sender.sendMessage(command + "strikepos " + ChatColor.RED + "[" + ChatColor.WHITE + "x" + ChatColor.RED + "] [" + ChatColor.WHITE + "y" + ChatColor.RED + "] [" + ChatColor.WHITE + "z" + ChatColor.RED + "]" + ChatColor.YELLOW + " - Lightning strike a specific position.");
        }
        
        if (sender.hasPermission("weathercontrol.weather.wand")) {
            sender.sendMessage(command + "wand " + ChatColor.GREEN + "<" + ChatColor.WHITE + "count" + ">" + ChatColor.YELLOW + " - Get wand item or set count.");
        }
    }
}
