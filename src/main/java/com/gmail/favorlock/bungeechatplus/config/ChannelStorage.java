package com.gmail.favorlock.bungeechatplus.config;

import java.io.File;
import net.craftminecraft.bungee.bungeeyaml.supereasyconfig.Config;

import com.gmail.favorlock.bungeechatplus.BungeeChatPlus;

public class ChannelStorage extends Config {
	
	private final BungeeChatPlus plugin;
	
	public ChannelStorage(BungeeChatPlus plugin, String name) {
		this.plugin = plugin;
		CONFIG_FILE = new File("plugins" + File.separator + plugin.getDescription().getName() +
								File.separator + "channels", name);
		this.Name = name.replace(".yml", "");
		this.Nick = name.substring(0, 1).toUpperCase();
		this.Format = "&8[&2%nick&8] %prefix&6%player&7%suffix: %message";
		this.Server = "";
		this.Password = "";
		this.MaxChatters = -1;
	}
	
	public String Name;
	public String Nick;
	public String Format;
	public String Server;
	public String Password;
	public int MaxChatters;
	
	public BungeeChatPlus getPlugin() {
		return this.plugin;
	}

}
