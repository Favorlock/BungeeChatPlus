package com.gmail.favorlock.bungeechatplus.listeners;

import java.util.ArrayList;

import com.gmail.favorlock.bungeechatplus.BungeeChatPlus;
import com.gmail.favorlock.bungeechatplus.entities.Chatter;
import com.gmail.favorlock.bungeechatplus.utils.ChatFormat;
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
		
		if (plugin.getConfig().Settings_EnableRegex) {
			plugin.getRegexManager().filterChat(event);
		}
		
		String message = event.getMessage();
		ProxiedPlayer sender = (ProxiedPlayer)event.getSender();
		Chatter chatter = plugin.getChatter(sender.getName());
		
		message = ChatFormat.formatMessage(message, plugin, sender, chatter);
		
		for (ProxiedPlayer player : plugin.getPlayers()) {
			Chatter listener = plugin.getChatter(player.getName());
			if ((chatter.getVerbose() == true) && (listener.getVerbose() == true)) {
				if (player.getServer().getInfo().getName() != sender.getServer().getInfo().getName()) {
					player.sendMessage(FontFormat.translateString(message));
				}
			}
		}
	}
	
	@Subscribe
	public void onPlayerLogin(LoginEvent event) {
		plugin.addChatter(event);
	}

}
