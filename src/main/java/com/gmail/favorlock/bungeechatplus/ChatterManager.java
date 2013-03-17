package com.gmail.favorlock.bungeechatplus;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import com.gmail.favorlock.bungeechatplus.config.ChatterStorage;
import com.gmail.favorlock.bungeechatplus.entities.Chatter;

public class ChatterManager {
	
	public final BungeeChatPlus plugin;
	public Map<String, Chatter> chatters;
	
	public ChatterManager(BungeeChatPlus plugin) {
		this.plugin = plugin;
		this.chatters = new HashMap<String, Chatter>();
	}
	
	public Chatter loadChatter(String name) {
		Chatter chatter = this.chatters.get(name);
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
		chatter.setVerbose(storage.verbose);
		this.chatters.put(name, chatter);
		
		return chatter;
	}
	
	public Chatter getChatter(String name) {
		return this.chatters.get(name);
	}

}
