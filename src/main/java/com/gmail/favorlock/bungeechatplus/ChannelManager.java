package com.gmail.favorlock.bungeechatplus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.gmail.favorlock.bungeechatplus.config.ChannelStorage;
import com.gmail.favorlock.bungeechatplus.entities.Channel;

public class ChannelManager {
	
	private final File directory;
	public final BungeeChatPlus plugin;
	public Channel defaultChannel;
	public Map<String, Channel> channels;
	
	public ChannelManager(BungeeChatPlus plugin) {
		this.directory = new File("plugins" + File.separator + plugin.getDescription().getName() +
				File.separator + "channels");
		this.plugin = plugin;
		channels = new HashMap<String, Channel>();
		loadChannels();
	}
	
	public void loadChannels() {
		String name = plugin.getConfig().Settings_DefaultChannel;
		name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
		File def = new File(directory, name + ".yml");

		ChannelStorage storage = new ChannelStorage(plugin, def.getName());
		try {
			storage.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Channel channel = new Channel(storage);
		this.defaultChannel = channel;
		this.channels.put(storage.Name.toLowerCase(), channel);
		
		for (File file : directory.listFiles()) {
			ChannelStorage storage2 = new ChannelStorage(plugin, file.getName());
			if (!storage2.Name.toLowerCase().equals(defaultChannel.getName().toLowerCase())) {
				try {
					storage2.init();
				} catch (Exception e) {
					e.printStackTrace();
				}
				this.channels.put(storage2.Name.toLowerCase(), new Channel(storage2));
			}
		}
	}
	
	public boolean addChannel(String name) {
		if (!channels.containsKey(name.toLowerCase())) { 
			name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
			ChannelStorage storage = new ChannelStorage(plugin, name + ".yml");
			try {
				storage.init();
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.channels.put(name.toLowerCase(), new Channel(storage));
			return true;
		}
		return false;
	}
	
	public Channel getChannel(String name) {
		return this.channels.get(name.toLowerCase());
	}
	
	public List<Channel> getChannels(List<String> channels) {
		if (channels == null) {
			return new ArrayList<Channel>();
		}
		List<Channel> list = new ArrayList<Channel>();
		for (String channel : channels) {
			list.add(getChannel(channel.toLowerCase()));
		}
		return list;
	}
	
	public boolean removeChannel(String name) {
		if (channels.containsKey(name.toLowerCase())) {
			this.channels.remove(name.toLowerCase());
			name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
			File file = new File(directory, name + ".yml");
			file.delete();
			return true;
		}
		return false;
	}
	
	public void listChannels() {
		for (String channel : channels.keySet()) {
			// Do something
		}
	}

}
