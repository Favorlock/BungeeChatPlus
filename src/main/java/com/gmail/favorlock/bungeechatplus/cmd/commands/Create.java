package com.gmail.favorlock.bungeechatplus.cmd.commands;

import net.md_5.bungee.api.CommandSender;

import com.gmail.favorlock.bungeechatplus.BungeeChatPlus;
import com.gmail.favorlock.bungeechatplus.cmd.BaseCommand;
import com.gmail.favorlock.bungeechatplus.utils.FontFormat;

public class Create extends BaseCommand {
	
	BungeeChatPlus plugin;

	public Create(BungeeChatPlus plugin) {
		super("BCP Create");
		this.plugin = plugin;
		setDescription("Create a new channel");
		setUsage("/bcp create <channel>");
		setArgumentRange(1, 1);
		setPermission("bungeechat.channels.create");
		setIdentifiers(new String[] { "bcp create" });
	}

	@Override
	public boolean execute(CommandSender sender, String identifier,
			String[] args) {
		if(plugin.getChannelManager().addChannel(args[0])) {
			sender.sendMessage(FontFormat.translateString("&eThe channel has been created!"));
			return true;
		}
		sender.sendMessage(FontFormat.translateString("&4The channel already exist!"));
		return false;
	}

}
