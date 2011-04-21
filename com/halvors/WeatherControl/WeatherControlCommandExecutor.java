package com.halvors.WeatherControl;

import org.bukkit.ChatColor;
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
			}
		}
		
		return false;
	}

	private void showHelp(Player player) {
		player.sendMessage(WeatherControl.name);
		player.sendMessage(ChatColor.RED + "[]" + ChatColor.WHITE + " Required, " + ChatColor.GREEN + "()" + ChatColor.WHITE + " Optional");

		String command = "/lc ";
		
		if (WeatherControl.hasPermissions(player, "LocalChat.help")) {
			player.sendMessage(command + "help" + ChatColor.YELLOW + " - Show help");
		}
	}
}