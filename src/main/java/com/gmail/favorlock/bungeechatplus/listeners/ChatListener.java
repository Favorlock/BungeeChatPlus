package com.gmail.favorlock.bungeechatplus.listeners;

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

import java.util.ArrayList;
import java.util.logging.Level;

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
            plugin.logToFile("Regex Processing Starting");
			plugin.getRegexManager().filterChat(event);
            plugin.logToFile(("Regex Processing Complete"));
		}
		
		String message = event.getMessage();
        plugin.logToFile("Message: " + message);
		ProxiedPlayer sender = (ProxiedPlayer)event.getSender();
        plugin.logToFile("Sender: " + sender.getName());

        if (plugin.getChatterManager().getChatter(sender.getName()) == null) {
            plugin.logToFile("Chatter object for " + sender.getName() + "is null!");
            if (plugin.getProxyServer().getPlayers().contains(sender.getName())) {
                plugin.logToFile("Loading chatter!");
                plugin.getChatterManager().loadChatter(sender.getName());
            } else {
                plugin.logToFile("Chatter is not online!");
                return;
            }
        }

        Chatter chatter = plugin.getChatterManager().getChatter(sender.getName());

        if (chatter == null) {
            plugin.logToFile("Chatter is still null... How is this possible!");
            return;
        }

        if (!(chatter.getPrefix() == null)) {
            plugin.logToFile("Checking if player is Jailed!");
            if (chatter.getPrefix().equals(plugin.getConfig().JailGroupPrefix)) {
                plugin.logToFile("Player " + sender.getName() + " is jailed!");
                return;
            }
        }

        Channel channel = chatter.getActiveChannel();
        plugin.logToFile("Active Channel: " + chatter.getActiveChannel().getName());

		if (channel == null) {
            plugin.logToFile("Channel object for " + chatter.getActiveChannel().getName() + "is null!");
            plugin.logToFile("ChatListener.java | Line 76");
			return;
		}

        plugin.logToFile("Sending message to channel " + chatter.getActiveChannel().getName());
		channel.sendMessage(sender, message);
	}
	
	@EventHandler
	public void onPlayerLogin(PostLoginEvent event) {
        plugin.logToFile("Player connecting: " + event.getPlayer().getName());
		plugin.getChatterManager().loadChatter(event.getPlayer().getName());
	}
	
	@EventHandler
	public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        plugin.logToFile("Player disconnecting: " + event.getPlayer().getName());
		plugin.getChatterManager().update(event.getPlayer().getName());
	}

}
