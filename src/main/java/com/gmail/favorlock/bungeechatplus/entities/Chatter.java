package com.gmail.favorlock.bungeechatplus.entities;

import com.gmail.favorlock.bungeechatplus.config.ChatterStorage;

public class Chatter {
	
	private final ChatterStorage storage;
	private String name;
	private boolean verbose;
	private String prefix;
	private String suffix;
	
	public Chatter(String name, ChatterStorage storage) {
		this.storage = storage;
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setVerbose() {
		this.verbose = !verbose;
	}
	
	public void setVerbose(boolean bool) {
		this.verbose = bool;
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
