package com.gmail.favorlock.bungeechatplus.cmd.commands;

import com.gmail.favorlock.bungeechatplus.BungeeChatPlus;
import com.gmail.favorlock.bungeechatplus.cmd.BaseCommand;
import com.gmail.favorlock.bungeechatplus.entities.Channel;
import com.gmail.favorlock.bungeechatplus.entities.Chatter;
import com.gmail.favorlock.bungeechatplus.utils.FontFormat;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Leave extends BaseCommand {

	BungeeChatPlus plugin;

	public Leave(BungeeChatPlus plugin) {
		super("BCP Leave");
		this.plugin = plugin;
		setDescription("Leave a channel");
		setUsage("/bcp leave <channel>");
		setArgumentRange(1, 1);
		setPermission("bungeechat.leave");
		setIdentifiers(new String[] { "bcp leave" });
	}

	@Override
	public boolean execute(CommandSender sender, String identifier, String[] args) {
		if (!(sender instanceof ProxiedPlayer)) {
			return false;
		}
		Chatter chatter = plugin.getChatterManager().getChatter(sender.getName());
		if (chatter == null) {
			return false;
		}
		Channel channel = plugin.getChannelManager().getChannel(args[0]);
		if (channel == null) {
			sender.sendMessage(FontFormat.translateString("&eThe channel &2" + args[0] + "&e does not exist"));
			return false;
		}
		if (chatter.removeChannel(channel)) {
			channel.getChatters().remove(chatter);
			sender.sendMessage(FontFormat.translateString("&eYou are no longer in channel &2" + channel.getName()));
			return true;
		}
		sender.sendMessage(FontFormat.translateString("&2You are not in channel &2" + channel.getName()));
		return false;
	}

}
