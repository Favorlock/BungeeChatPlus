package com.gmail.favorlock.bungeechatplus.listeners;

import java.util.ArrayList;

import com.gmail.favorlock.bungeechatplus.BungeeChatPlus;
import com.gmail.favorlock.bungeechatplus.entities.Chatter;
import com.gmail.favorlock.bungeechatplus.utils.FontFormat;
import com.google.common.eventbus.Subscribe;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;

public class ChatListener implements Listener {
	
	BungeeChatPlus plugin;
	ArrayList<String> servers;
	
	public ChatListener(BungeeChatPlus plugin) {
		this.plugin = plugin;
		this.servers = plugin.getConfig().FactionServers;
	}
	
	@Subscribe
	public void onPlayerMessage(ChatEvent event) {
		if (!(event.getSender() instanceof ProxiedPlayer)) {
			return;
		}
		
		for (String server : servers) {
			ProxiedPlayer player = (ProxiedPlayer)event.getSender();
			String playersServer = player.getServer().getInfo().getName();
			if (server.equalsIgnoreCase(playersServer)) {
				return;
			}
		}
		
		plugin.getRegexManager().filterChat(event);
		
		if (!event.isCancelled()) {
			String message = event.getMessage();
			ProxiedPlayer sender = (ProxiedPlayer)event.getSender();
			Chatter chatter = plugin.getChatter(sender.getName());
			
			String outputMessage = plugin.getConfig().Settings_ChatFormat;
			outputMessage = outputMessage.replace("%server", sender.getServer().getInfo().getName());
			if (chatter.getPrefix() != null) {
				outputMessage = outputMessage.replace("%prefix", chatter.getPrefix());
			} else {
				outputMessage = outputMessage.replace("%prefix", "");
			}
			outputMessage = outputMessage.replace("%player", chatter.getName());
			if (chatter.getSuffix() != null) {
				outputMessage = outputMessage.replace("%suffix", chatter.getSuffix());
			} else {
				outputMessage = outputMessage.replace("%suffix", "");
			}
			outputMessage = outputMessage.replace("%message", message);
		
			for (ProxiedPlayer player : plugin.getPlayers()) {
				Chatter listener = plugin.getChatter(player.getName());
				if ((chatter.getVerbose() == true) && (listener.getVerbose() == true)) {
					if (player.getServer().getInfo().getName() != sender.getServer().getInfo().getName()) {
						player.sendMessage(FontFormat.translateString(outputMessage));
					}
				}
			}
		}
	}
	
	@Subscribe
	public void onPlayerLogin(LoginEvent event) {
		plugin.addChatter(event);
	}

}
