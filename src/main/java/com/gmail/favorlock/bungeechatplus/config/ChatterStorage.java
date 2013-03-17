package com.gmail.favorlock.bungeechatplus.config;

import java.io.File;

import com.gmail.favorlock.bungeechatplus.BungeeChatPlus;

import net.craftminecraft.bungee.bungeeyaml.supereasyconfig.Config;

public class ChatterStorage extends Config {
	
	public ChatterStorage(BungeeChatPlus plugin, String name) {
		CONFIG_FILE = new File("plugins" + File.separator + plugin.getDescription().getName() +
								File.separator + "chatters" + File.separator + 
								name.substring(0, 1).toLowerCase(), name + ".yml");
		this.name = name;
		this.verbose = plugin.getConfig().Settings_GlobalChatOnLogin;
	}
	
	public String name;
	public boolean verbose;

}
