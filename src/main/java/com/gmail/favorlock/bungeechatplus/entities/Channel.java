package com.gmail.favorlock.bungeechatplus.entities;

import java.util.ArrayList;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;

import com.gmail.favorlock.bungeechatplus.config.ChannelStorage;
import com.gmail.favorlock.bungeechatplus.utils.ChatFormat;
import com.gmail.favorlock.bungeechatplus.utils.FontFormat;

public class Channel {
	
	private final ChannelStorage storage;
	private String name;
	private String nick;
	private String format;
	private ArrayList<Chatter> chatters;
	
	public Channel(ChannelStorage storage) {
		this.storage = storage;
		this.name = storage.Name;
		this.nick = storage.Nick;
		this.format = storage.Format;
		this.chatters = new ArrayList<Chatter>();
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setNick(String nick) {
		this.nick = nick;
	}
	
	public String getNick() {
		return this.nick;
	}
	
	public String getFormat() {
		return this.format;
	}
	
	public void addChatter(Chatter chatter) {
		this.chatters.add(chatter);
	}
	
	public void removeChatter(Chatter chatter) {
		this.chatters.remove(chatter);
	}
	
	public void sendMessage(ChatEvent event, String message) {
		ProxiedPlayer sender = (ProxiedPlayer) event.getSender();
		Chatter chatter = storage.getPlugin().getChatterManager().getChatter(sender.getName());
		message = ChatFormat.formatMessage(message, storage.getPlugin(), sender, chatter, this);
				
		for (ProxiedPlayer player : storage.getPlugin().getPlayers()) {
			Chatter listener = storage.getPlugin().getChatterManager().getChatter(player.getName());
			if ((chatter.getVerbose() == true) && (listener.getVerbose() == true)) {
				if (player.getServer().getInfo().getName() != sender.getServer().getInfo().getName()) {
					if (chatters.contains(listener)) {
						player.sendMessage(FontFormat.translateString(message));
					}
				}
			}
		}
	}

}
