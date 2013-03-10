package com.gmail.favorlock.bungeechatplus.listeners;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;

import com.gmail.favorlock.bungeechatplus.BungeeChatPlus;
import com.gmail.favorlock.bungeechatplus.entities.Chatter;
import com.google.common.eventbus.Subscribe;

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
		plugin.getProxyServer().getLogger().log(Level.INFO, channel);
		if (channel.equalsIgnoreCase("VaultAffix")) {
			String player = in.readUTF();
			Chatter chatter = this.plugin.getChatter(player);
			chatter.setPrefix(in.readUTF());
			chatter.setSuffix(in.readUTF());
		}
	}

}
