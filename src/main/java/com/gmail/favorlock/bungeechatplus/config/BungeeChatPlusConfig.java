package com.gmail.favorlock.bungeechatplus.config;

import java.io.File;
import java.util.ArrayList;

import com.gmail.favorlock.bungeechatplus.BungeeChatPlus;

import net.craftminecraft.bungee.bungeeyaml.supereasyconfig.Config;

public class BungeeChatPlusConfig extends Config {
	
	public BungeeChatPlusConfig(BungeeChatPlus plugin) {
		CONFIG_FILE = new File("plugins" + File.separator + plugin.getDescription().getName(), "config.yml");
		CONFIG_HEADER = "BungeeChatPlus - By Favorlock";
	}
	
	public String Settings_DefaultChannel = "Global";
	public boolean Settings_GlobalChatOnLogin = true;
	public boolean Settings_EnableRegex = true;
	public boolean Settings_EnableRegexLog = true;
	public String Settings_Messages_warnmsg = "&4[&6BungeeChat+&4] &4Warned by BungeeChat+!";
	public String Settings_Messages_kickmsg = "&4[&6BungeeChat+&4] &4Kicked by BungeeChat+!";
	public ArrayList<String> FactionServers = new ArrayList<String>();

}
