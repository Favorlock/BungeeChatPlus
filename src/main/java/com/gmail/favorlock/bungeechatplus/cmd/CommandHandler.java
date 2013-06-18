package com.gmail.favorlock.bungeechatplus.cmd;

import com.gmail.favorlock.bungeechatplus.BungeeChatPlus;
import com.gmail.favorlock.bungeechatplus.utils.FontFormat;
import net.md_5.bungee.api.CommandSender;

import java.util.*;

public class CommandHandler {
	
	protected LinkedHashMap<String, ICommand> commands;
	protected HashMap<String, ICommand> identifiers;
	private final BungeeChatPlus plugin;
	
	public CommandHandler(BungeeChatPlus plugin) {
		this.plugin = plugin;
		this.commands = new LinkedHashMap<String, ICommand>();
		this.identifiers = new HashMap<String, ICommand>();
	}
	
	public void addCommand(ICommand command) {
		this.commands.put(command.getName().toLowerCase(), command);
		for (String ident : command.getIdentifiers()) {
			this.identifiers.put(ident.toLowerCase(), command);
		}
	}
	
	public boolean dispatch(CommandSender sender, String commandName, String[] args) {
		for (int argsIncluded = args.length; argsIncluded >= 0; argsIncluded--) {
			StringBuilder identifierBuilder = new StringBuilder(commandName);
			for(int i = 0; i < argsIncluded; i++) {
				identifierBuilder.append(' ').append(args[i]);
			}
			
			String identifier = identifierBuilder.toString();
			ICommand cmd = getCmdFromIdent(identifier, sender);
			if (cmd != null) {
				String[] realArgs = (String[])Arrays.copyOfRange(args, argsIncluded, args.length);
				
				if (!cmd.isInProgress(sender)) {
					if ((realArgs.length < cmd.getMinArguments()) || ((realArgs.length > cmd.getMaxArguments()) 
																	&& (cmd.getMaxArguments() != -1))) {
						displayCommandHelp(cmd, sender);
						return true;
					}
					if ((realArgs.length > 0) && (realArgs[0].equals("?"))) {
						displayCommandHelp(cmd, sender);
						return true;
					}
				}
				
				if (!sender.hasPermission(cmd.getPermission())) {
					sender.sendMessage("Insufficient permission.");
					return true;
				}
				
				cmd.execute(sender, identifier, realArgs);
				return true;
			}
		}
		sender.sendMessage("Unrecognized command!");
		return true;
	}
	
	private void displayCommandHelp(ICommand cmd, CommandSender sender) {
		sender.sendMessage(new StringBuilder().append(FontFormat.translateString("&cCommand:&e ")).append(cmd.getName()).toString());
		sender.sendMessage(new StringBuilder().append(FontFormat.translateString("&cDescription:&e ")).append(cmd.getDescription()).toString());
		sender.sendMessage(new StringBuilder().append(FontFormat.translateString("&cUsage:&e ")).append(cmd.getUsage()).toString());
		if (cmd.getNotes() != null) {
			for (String note : cmd.getNotes()) {
				sender.sendMessage(new StringBuilder().append(FontFormat.translateString("&e")).append(note).toString());
			}
		}
	}
	
	public ICommand getCmdFromIdent(String ident, CommandSender sender) {
		if (this.identifiers.get(ident.toLowerCase()) == null) {
			for (ICommand cmd : this.commands.values()) {
				if (cmd.isIdentifier(sender, ident)) {
					return cmd;
				}
			}
		}
		return (ICommand)this.identifiers.get(ident.toLowerCase());
	}
	
	public ICommand getCommand(String name) {
		return (ICommand)this.commands.get(name.toLowerCase());
	}
	
	public List<ICommand> getCommands() {
		return new ArrayList<ICommand>(this.commands.values());
	}

}

