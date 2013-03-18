package com.gmail.favorlock.bungeechatplus.cmd.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import com.gmail.favorlock.bungeechatplus.BungeeChatPlus;
import com.gmail.favorlock.bungeechatplus.cmd.BaseCommand;
import com.gmail.favorlock.bungeechatplus.entities.Channel;
import com.gmail.favorlock.bungeechatplus.utils.FontFormat;

public class Focus extends BaseCommand {
	
	BungeeChatPlus plugin;

	public Focus(BungeeChatPlus plugin) {
		super("BCP Focus");
		this.plugin = plugin;
		setDescription("Set your active channel");
		setUsage("/bcp focus <channel>");
		setArgumentRange(1, 1);
		setPermission("bungeechat.channels.focus");
		setIdentifiers(new String[] { "bcp focus" });
	}

	@Override
	public boolean execute(CommandSender sender, String identifier,
			String[] args) {
		if (!(sender instanceof ProxiedPlayer)) {
			return false;
		}
		Channel channel = plugin.getChannelManager().getChannel(args[0]);
		if(plugin.getChatterManager().getChatter(sender.getName()).setActiveChannel(channel)) {
			sender.sendMessage(FontFormat.translateString("&eYou are now talking in &2" + channel.getName()));
			return true;
		}
		sender.sendMessage(FontFormat.translateString("&2You are already talking in &2" + channel.getName()));
		return false;
	}

}
