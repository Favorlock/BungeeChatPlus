package com.gmail.favorlock.bungeechatplus.cmd.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import com.gmail.favorlock.bungeechatplus.BungeeChatPlus;
import com.gmail.favorlock.bungeechatplus.cmd.BaseCommand;
import com.gmail.favorlock.bungeechatplus.utils.FontFormat;

public class PrivateMessage extends BaseCommand {
	
	private BungeeChatPlus plugin;
	
	public PrivateMessage(BungeeChatPlus plugin) {
		super("BCP PM");
		this.plugin = plugin;
		setDescription("Send a message to a player");
		setUsage("/bcp pm <player> <message>");
		setArgumentRange(2, -1);
		setPermission("bungeechat.pm");
		setIdentifiers(new String[] { "bcp pm" });
	}

	@Override
	public boolean execute(CommandSender sender, String identifier,
			String[] args) {
		ProxiedPlayer player = plugin.getProxyServer().getPlayer(args[0]);
		
		if (!(sender instanceof ProxiedPlayer)) {
			return false;
		}
		if (player != null) {
			if (sender == player) {
				sender.sendMessage(FontFormat.translateString("&7You cannot send a pm to yourself"));
				return false;
			}
			
			String message = "";
		
			for (int counter = 1; counter < args.length; counter++) {
				message += args[counter] + " ";
			}
		
			message = message.trim();
		
			player.sendMessage(FontFormat.translateString("&6" + sender.getName() +
					" &8 -> &6You&8:&7 " + message));
			return true;
		}
		sender.sendMessage(FontFormat.translateString("&6"+ args[0] + " &7is not online"));
		return false;
	}

}
