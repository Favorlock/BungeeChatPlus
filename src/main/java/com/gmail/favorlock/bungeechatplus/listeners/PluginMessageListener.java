package com.gmail.favorlock.bungeechatplus.listeners;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;

import com.gmail.favorlock.bungeechatplus.BungeeChatPlus;
import com.gmail.favorlock.bungeechatplus.entities.Chatter;
import com.gmail.favorlock.bungeechatplus.utils.FontFormat;
import com.google.common.eventbus.Subscribe;

import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;

public class PluginMessageListener implements Listener {
	
	BungeeChatPlus plugin;
	
	public PluginMessageListener(BungeeChatPlus plugin) {
		this.plugin = plugin;
	}
	
	@Subscribe
	public void receivePluginMessage(PluginMessageEvent event) throws IOException {
		if (!event.getTag().equalsIgnoreCase("BungeeChatPlus")) {
			return;
		}
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
		String channel = in.readUTF();
		if (channel.equalsIgnoreCase("VaultAffix")) {
			String player = in.readUTF();
			Chatter chatter = this.plugin.getChatter(player);
			chatter.setPrefix(in.readUTF());
			chatter.setSuffix(in.readUTF());
		}
		if (channel.equalsIgnoreCase("FactionChat")) {
			String name = in.readUTF();
			String message = in.readUTF();
			ProxiedPlayer player = null;
			Chatter chatter = this.plugin.getChatter(name);
			
			for (ProxiedPlayer players : plugin.getPlayers()) {
				if (name.equalsIgnoreCase(players.getName())) {
					player = players;
				}
			}
			
			ChatEvent chatevent = new ChatEvent(player, event.getReceiver(), message);
			
			plugin.getRegexManager().filterChat(chatevent);
			
			String outputMessage = plugin.getConfig().Settings_ChatFormat;
			outputMessage = outputMessage.replace("%server", player.getServer().getInfo().getName());
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
			outputMessage = outputMessage.replace("%message", chatevent.getMessage());
			
			for (ProxiedPlayer player2 : plugin.getPlayers()) {
				Chatter listener = plugin.getChatter(player2.getName());
				if ((chatter.getVerbose() == true) && (listener.getVerbose() == true)) {
					if (player.getServer().getInfo().getName() != player2.getServer().getInfo().getName()) {
						player2.sendMessage(FontFormat.translateString(outputMessage));
					}
				}
			}
		}
	}

}
