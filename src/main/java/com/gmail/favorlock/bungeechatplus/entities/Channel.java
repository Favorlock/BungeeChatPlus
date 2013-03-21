package com.gmail.favorlock.bungeechatplus.entities;

import java.util.ArrayList;
import java.util.List;

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
	private String password;
	private int maxChatters;
	private ArrayList<Chatter> chatters;
	
	public Channel(ChannelStorage storage) {
		this.storage = storage;
		this.name = storage.Name;
		this.nick = storage.Nick;
		this.format = storage.Format;
		this.password = storage.Password;
		this.maxChatters = storage.MaxChatters;
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
	
	public String getPassword() {
		return this.password;
	}
	
	public int getMaxChatters() {
		return this.maxChatters;
	}
	
	public List<Chatter> getChatters() {
		return this.chatters;
	}
	
	public void addChatter(Chatter chatter) {
		this.chatters.add(chatter);
	}
	
	public void removeChatter(Chatter chatter) {
		this.chatters.remove(chatter);
	}
	
	public void sendMessage(ChatEvent event, String message) {
		ProxiedPlayer sender = (ProxiedPlayer) event.getSender();
		
		if (sender.hasPermission("bungeechat.channels.*") || 
				sender.hasPermission("bungeechat.channels." + this.getName().toLowerCase())) {
		
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
					if (storage.getPlugin().getConfig().Settings_SendToLocalChat) {
						if (player.getServer().getInfo().getName() == sender.getServer().getInfo().getName()) {
							player.sendMessage(FontFormat.translateString(message));
						}
					}
				}
			}
		} else {
			sender.sendMessage(FontFormat.translateString("&4You do not have permission to speak in &7" +
					this.getName()));
		}
	}

}
