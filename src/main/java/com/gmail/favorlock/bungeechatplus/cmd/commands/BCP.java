package com.gmail.favorlock.bungeechatplus.cmd.commands;

import com.gmail.favorlock.bungeechatplus.BungeeChatPlus;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class BCP extends Command {
	
	BungeeChatPlus plugin;

	public BCP(BungeeChatPlus plugin) {
		super("bcp");
		this.plugin = plugin;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		plugin.getCommandHandler().dispatch(sender, this.getName(), args);
	}

}
