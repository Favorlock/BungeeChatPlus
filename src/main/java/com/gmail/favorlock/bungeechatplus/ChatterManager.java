package com.gmail.favorlock.bungeechatplus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.gmail.favorlock.bungeechatplus.config.ChatterStorage;
import com.gmail.favorlock.bungeechatplus.entities.Channel;
import com.gmail.favorlock.bungeechatplus.entities.Chatter;

public class ChatterManager {
	
	public final BungeeChatPlus plugin;
	public Map<String, Chatter> chatters;
	
	public ChatterManager(BungeeChatPlus plugin) {
		this.plugin = plugin;
		this.chatters = new HashMap<String, Chatter>();
	}
	
	public Chatter loadChatter(String name) {
		Chatter chatter = getChatter(name);
		if (chatter != null) {
			return chatter;
		}
		
		ChatterStorage storage = new ChatterStorage(plugin, name);
		try {
			storage.init();
		} catch (Exception e) {
			plugin.getProxyServer().getLogger().log(Level.SEVERE, "Failed to load user file", e);
		}
		chatter = new Chatter(name, storage);
		chatter.setVerbose(storage.Verbose);
		
		for (Channel channel : chatter.getChannels()) {
			channel.addChatter(chatter);
		}
		
		this.chatters.put(name, chatter);
		
		return chatter;
	}
	
	public void update(String name) {
		Chatter chatter = getChatter(name);
		ChatterStorage storage = chatter.getChatterStorage();
		List<Channel> channels = chatter.getChannels();
		ArrayList<String> channels2 = new ArrayList<String>();
		
		for (Channel channel : channels) {
			channels2.add(channel.getName());
			channel.removeChatter(chatter);
		}
		
		storage.Verbose = chatter.getVerbose();
		storage.ActiveChannel = chatter.getActiveChannel().getName();
		storage.Channels = channels2;
		
		removeChatter(name);
		try {
			storage.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Chatter getChatter(String name) {
		return this.chatters.get(name);
	}
	
	public void removeChatter(String name) {
		this.chatters.remove(name);
	}

}
