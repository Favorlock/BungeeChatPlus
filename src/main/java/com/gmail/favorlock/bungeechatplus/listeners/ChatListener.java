package com.gmail.favorlock.bungeechatplus.listeners;

import java.util.ArrayList;
import com.gmail.favorlock.bungeechatplus.BungeeChatPlus;
import com.gmail.favorlock.bungeechatplus.entities.Channel;
import com.gmail.favorlock.bungeechatplus.entities.Chatter;
import com.google.common.eventbus.Subscribe;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
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
		if (event.isCancelled()) {
			return;
		}
		if (!(event.getSender() instanceof ProxiedPlayer)) {
			return;
		}
		if (event.getMessage().startsWith("/")) {
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
		Chatter chatter = plugin.getChatterManager().getChatter(sender.getName());
		Channel channel = chatter.getActiveChannel();
		
		channel.sendMessage(event, message);
	}
	
	@Subscribe
	public void onPlayerLogin(PostLoginEvent event) {
		plugin.getChatterManager().loadChatter(event.getPlayer().getName());
	}
	
	@Subscribe
	public void onPlayerDisconnect(PlayerDisconnectEvent event) {
		plugin.getChatterManager().update(event.getPlayer().getName());
	}

}
