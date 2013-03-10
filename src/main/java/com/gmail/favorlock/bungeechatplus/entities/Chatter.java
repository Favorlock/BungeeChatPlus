package com.gmail.favorlock.bungeechatplus.entities;

import com.gmail.favorlock.bungeechatplus.BungeeChatPlus;

public class Chatter {
	
	private String name;
	private boolean verbose;
	private String prefix;
	private String suffix;
	
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
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public String getPrefix() {
		return this.prefix;
	}
	
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
	public String getSuffix() {
		return this.suffix;
	}

}
