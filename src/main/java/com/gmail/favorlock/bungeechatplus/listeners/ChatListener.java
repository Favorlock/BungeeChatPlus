package com.gmail.favorlock.bungeechatplus.listeners;

import com.gmail.favorlock.bungeechatplus.BungeeChatPlus;
import com.gmail.favorlock.bungeechatplus.entities.Channel;
import com.gmail.favorlock.bungeechatplus.entities.Chatter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;

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
            if (plugin.getProxyServer().getPlayers().contains(sender)) {
                plugin.getChatterManager().loadChatter(sender.getName());
            } else {
                return;
            }
        }

        Chatter chatter = plugin.getChatterManager().getChatter(sender.getName());

        if (chatter == null) {
            return;
        }

        if (!(chatter.getPrefix() == null)) {
            if (chatter.getPrefix().equals(plugin.getConfig().JailGroupPrefix)) {
                return;
            }
        }

        Channel channel = chatter.getActiveChannel();

		if (channel == null) {
			return;
		}

		channel.sendMessage(sender, message);
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
