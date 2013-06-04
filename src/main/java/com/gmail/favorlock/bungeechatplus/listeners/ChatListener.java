package com.gmail.favorlock.bungeechatplus.listeners;

import java.util.ArrayList;
import java.util.logging.Level;

import com.gmail.favorlock.bungeechatplus.BungeeChatPlus;
import com.gmail.favorlock.bungeechatplus.entities.Channel;
import com.gmail.favorlock.bungeechatplus.entities.Chatter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ChatListener implements Listener {
	
	BungeeChatPlus plugin;
	ArrayList<String> servers;
	
	public ChatListener(BungeeChatPlus plugin) {
		this.plugin = plugin;
		this.servers = plugin.getConfig().FactionServers;
	}
	
	@EventHandler
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
		
		if (plugin.getChatterManager().getChatter(sender.getName()) == null) {
			plugin.getChatterManager().loadChatter(sender.getName());
		}
		
		Chatter chatter = plugin.getChatterManager().getChatter(sender.getName());
		Channel channel = chatter.getActiveChannel();
		
		if (chatter.getPrefix().equals(plugin.getConfig().JailGroupPrefix)) {
			return;
		}
		
		if (channel == null) {
			ProxyServer.getInstance().getLogger().log(Level.INFO, "Null Channel @ " + chatter.getName() +
					"\nPlease send a copy of all files and the user name above to Favorlock.");
			
			return;
		}
		
		channel.sendMessage(event, message);
	}
	
	@EventHandler
	public void onPlayerLogin(PostLoginEvent event) {
		plugin.getChatterManager().loadChatter(event.getPlayer().getName());
	}
	
	@EventHandler
	public void onPlayerDisconnect(PlayerDisconnectEvent event) {
		plugin.getChatterManager().update(event.getPlayer().getName());
	}

}
