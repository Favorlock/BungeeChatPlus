package com.gmail.favorlock.bungeechatplus.cmd.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import com.gmail.favorlock.bungeechatplus.BungeeChatPlus;
import com.gmail.favorlock.bungeechatplus.cmd.BaseCommand;
import com.gmail.favorlock.bungeechatplus.entities.Channel;
import com.gmail.favorlock.bungeechatplus.utils.FontFormat;

public class Leave extends BaseCommand {
	
	BungeeChatPlus plugin;

	public Leave(BungeeChatPlus plugin) {
		super("BCP Leave");
		this.plugin = plugin;
		setDescription("Leave a channel");
		setUsage("/bcp leave <channel>");
		setArgumentRange(1, 1);
		setPermission("bungeechat.channels.leave");
		setIdentifiers(new String[] { "bcp leave" });
	}

	@Override
	public boolean execute(CommandSender sender, String identifier,
			String[] args) {
		if (!(sender instanceof ProxiedPlayer)) {
			return false;
		}
		Channel channel = plugin.getChannelManager().getChannel(args[0]);
		if(plugin.getChatterManager().getChatter(sender.getName()).removeChannel(channel)) {
			sender.sendMessage(FontFormat.translateString("&eYou are no longer in channel &2" + channel.getName()));
			return true;
		}
		sender.sendMessage(FontFormat.translateString("&2You are not in channel &2" + channel.getName()));
		return false;
	}

}