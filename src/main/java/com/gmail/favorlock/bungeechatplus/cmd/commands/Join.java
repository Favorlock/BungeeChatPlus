package com.gmail.favorlock.bungeechatplus.cmd.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import com.gmail.favorlock.bungeechatplus.BungeeChatPlus;
import com.gmail.favorlock.bungeechatplus.cmd.BaseCommand;
import com.gmail.favorlock.bungeechatplus.entities.Channel;
import com.gmail.favorlock.bungeechatplus.utils.FontFormat;

public class Join extends BaseCommand {
	
	BungeeChatPlus plugin;

	public Join(BungeeChatPlus plugin) {
		super("BCP Join");
		this.plugin = plugin;
		setDescription("Join a channel");
		setUsage("/bcp join <channel>");
		setArgumentRange(1, 1);
		setPermission("bungeechat.channels.join");
		setIdentifiers(new String[] { "bcp join" });
	}

	@Override
	public boolean execute(CommandSender sender, String identifier,
			String[] args) {
		if (!(sender instanceof ProxiedPlayer)) {
			return false;
		}
		Channel channel = plugin.getChannelManager().getChannel(args[0]);
		if(plugin.getChatterManager().getChatter(sender.getName()).addChannel(channel)) {
			sender.sendMessage(FontFormat.translateString("&eYou you joined the channel &2" + channel.getName()));
			return true;
		}
		sender.sendMessage(FontFormat.translateString("&2You are already in the channel &2" + channel.getName()));
		return false;
	}

}
