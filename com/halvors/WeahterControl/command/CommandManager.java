package com.halvors.WeahterControl.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.halvors.WeatherControl.WeatherControl;

public class CommandManager implements CommandExecutor {
	private final WeatherControl plugin;
	
    protected List<BaseCommand> commands;

    public CommandManager(final WeatherControl plugin) {
    	this.plugin = plugin;
        this.commands = new ArrayList<BaseCommand>();
    }

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String input = label + " ";
        
        for (String s : args) {
            input += s + " ";
        }

        BaseCommand match = null;
        String[] trimmedArgs = null;
        StringBuilder identifier = new StringBuilder();

        for (BaseCommand cmd : commands) {
            StringBuilder tmpIdentifier = new StringBuilder();
            String[] tmpArgs = cmd.validate(input, tmpIdentifier);
            
            if (tmpIdentifier.length() > identifier.length()) {
                identifier = tmpIdentifier;
                match = cmd;
                
                if (tmpArgs != null) {
                    trimmedArgs = tmpArgs;
                }
            }
            
            if (tmpArgs != null && tmpIdentifier.length() != 0) {
                if (tmpIdentifier.length() > identifier.length()) {
                    identifier = tmpIdentifier;
                    trimmedArgs = tmpArgs;
                    match = cmd;
                }
            }
        }

        if (match != null) {
            if (trimmedArgs != null) {
                match.execute(sender, trimmedArgs);
                
                return true;
            } else {
                sender.sendMessage("Command: " + match.getName());
                sender.sendMessage("Description: " + match.getDescription());
                sender.sendMessage("Usage: " + match.getUsage());
            }
        }

        return true;
    }

    public void addCommand(BaseCommand command) {
        commands.add(command);
    }

    public void removeCommand(BaseCommand command) {
        commands.remove(command);
    }
}