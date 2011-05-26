package com.halvors.WeahterControl.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.halvors.WeahterControl.command.BaseCommand;
import com.halvors.WeatherControl.WeatherControl;

public class HelpCommand extends BaseCommand {
    public HelpCommand(WeatherControl plugin) {
        super(plugin);
        name = "Help";
        description = "Displays the help menu";
        usage = "/ch help [page#]";
        minArgs = 0;
        maxArgs = 1;
        identifiers.add("ch help");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    	if (sender instanceof Player) {
    		Player player = (Player) sender;
    		
    		player.sendMessage("Help");
    	}
    }
}