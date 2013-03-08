package com.gmail.favorlock.bungeechatplus.entities;

import com.gmail.favorlock.bungeechatplus.BungeeChatPlus;

public class Chatter {
	
	private String name;
	private boolean verbose;
	
	public Chatter(String name, BungeeChatPlus plugin) {
		this.name = name;
		verbose = plugin.getConfig().Settings_GlobalChatOnLogin;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setVerbose() {
		this.verbose = !verbose;
	}
	
	public boolean getVerbose() {
		return this.verbose;
	}

}
