package com.gmail.favorlock.bungeechatplus;

import java.io.File;
import java.util.ArrayList;


import net.craftminecraft.bungee.bungeeyaml.supereasyconfig.Config;

public class NoSwearConfig extends Config {
	
	public NoSwearConfig(BungeeChatPlus plugin) {
		CONFIG_FILE = new File("plugins" + File.separator + plugin.getDescription().getName(), "config.yml");
		CONFIG_HEADER = "BungeeNoSwear - By Favorlock";
	}
	
	public boolean Settings_GlobalChatOnLogin = true;
	public boolean Settings_EnableLog = true;
	public String Settings_warnmsg = "&4[&6PwnFilter&4] &4Warned by PwnFilter!";
	public String Settings_kickmsg = "&4[&6PwnFilter&4] &4Kicked by PwnFilter!";
/*	public ArrayList<String> Filter = new ArrayList<String>(){{
		add("BadWordOne");
		add("BadWordTwo");
		add("BadWordThree");
	}}; */

}
