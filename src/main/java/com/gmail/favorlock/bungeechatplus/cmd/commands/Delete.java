package com.gmail.favorlock.bungeechatplus.cmd.commands;

import net.md_5.bungee.api.CommandSender;

import com.gmail.favorlock.bungeechatplus.BungeeChatPlus;
import com.gmail.favorlock.bungeechatplus.cmd.BaseCommand;
import com.gmail.favorlock.bungeechatplus.utils.FontFormat;

public class Delete extends BaseCommand {
	
	BungeeChatPlus plugin;

	public Delete(BungeeChatPlus plugin) {
		super("BCP Delete");
		this.plugin = plugin;
		setDescription("Delete a channel");
		setUsage("/bcp delete <channel>");
		setArgumentRange(1, 1);
		setPermission("bungeechat.channels.delete");
		setIdentifiers(new String[] { "bcp delete" });
	}

	@Override
	public boolean execute(CommandSender sender, String identifier,
			String[] args) {
		if(plugin.getChannelManager().removeChannel(args[0])) {
			sender.sendMessage(FontFormat.translateString("&eThe channel has been deleted!"));
			return true;
		}
		sender.sendMessage(FontFormat.translateString("&4The channel does not exist!"));
		return false;
	}

}
