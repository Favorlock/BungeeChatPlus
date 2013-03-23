package com.gmail.favorlock.bungeechatplus.cmd.commands;

import net.md_5.bungee.api.CommandSender;

import com.gmail.favorlock.bungeechatplus.BungeeChatPlus;
import com.gmail.favorlock.bungeechatplus.cmd.BaseCommand;
import com.gmail.favorlock.bungeechatplus.entities.Channel;
import com.gmail.favorlock.bungeechatplus.utils.FontFormat;

public class List extends BaseCommand {
	
	BungeeChatPlus plugin;

	public List(BungeeChatPlus plugin) {
		super("BCP List");
		this.plugin = plugin;
		setDescription("List all channels");
		setUsage("/bcp list");
		setArgumentRange(0, 0);
		setPermission("bungeechat.list");
		setIdentifiers(new String[] { "bcp list" });
	}

	@Override
	public boolean execute(CommandSender sender, String identifier,
			String[] args) {
		sender.sendMessage(FontFormat.translateString("&a-----------------------------------------------------\n" +
				"&eChannels"));

		for (Channel channel : plugin.getChannelManager().getChannels()) {
			if (!(channel.getMaxChatters() == -1)) {
				sender.sendMessage(FontFormat.translateString("&a" + channel.getName() + " &8-&7 " +
						channel.getChatters().size() + "&8/&7" + channel.getMaxChatters()));
			} else {
				sender.sendMessage(FontFormat.translateString("&a" + channel.getName() + " &8-&7 " +
						channel.getChatters().size() + "&8/&7" + Character.toString('\u221E')));
			}
		}

		sender.sendMessage(FontFormat.translateString("&a-----------------------------------------------------\n"));
		return true;
	}

}
