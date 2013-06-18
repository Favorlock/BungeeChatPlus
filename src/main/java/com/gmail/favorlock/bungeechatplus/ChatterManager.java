package com.gmail.favorlock.bungeechatplus;

import com.gmail.favorlock.bungeechatplus.config.ChatterStorage;
import com.gmail.favorlock.bungeechatplus.entities.Channel;
import com.gmail.favorlock.bungeechatplus.entities.Chatter;

import java.util.*;
import java.util.logging.Level;

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
			if (channel != null) {
				channel.addChatter(chatter);
			}
		}
		
		this.chatters.put(name, chatter);
		
		return chatter;
	}
	
	public void update(String name) {
		Chatter chatter = getChatter(name);

        if (chatter == null) {
            plugin.getLogger().log(Level.INFO, "Logging chatters map to plugin.log!");
            plugin.logToFile("Chatter instance for " + name + " is null!");
            plugin.logToFile("Chatters map will now be displayed:");
            for (String user : chatters.keySet()) {
                boolean value = false;
                if (chatters.get(user) == null) {
                    value = true;
                }
                plugin.logToFile("User: " + user + " | Chatter Null: " + value);
            }
            plugin.getLogger().log(Level.INFO, "Logging complete!\nPlease send plugin.log to Favorlock!");
            return;
        }

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
		chatter = null;
		try {
			storage.save();
			storage = null;
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
