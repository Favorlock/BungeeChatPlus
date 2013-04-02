package com.gmail.favorlock.bungeechatplus.entities;

import java.util.List;

import com.gmail.favorlock.bungeechatplus.config.ChatterStorage;

public class Chatter {
	
	private final ChatterStorage storage;
	private final String name;
	private boolean verbose;
	private Channel activeChannel;
	private List<Channel> channels;
	private String prefix;
	private String suffix;
	
	public Chatter(String name, ChatterStorage storage) {
		this.storage = storage;
		this.name = name;
		this.activeChannel = storage.getPlugin().getChannelManager().getChannel(storage.ActiveChannel);
		if (!storage.Channels.isEmpty()) {
			this.channels = storage.getPlugin().getChannelManager().getChannels(storage.Channels);
		} else {
			setActiveChannel(storage.getPlugin().getChannelManager().defaultChannel);
		}
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
	
	public boolean setActiveChannel(Channel channel) {
		if (this.activeChannel != channel) {
				this.activeChannel = channel;
		
			if (!this.channels.contains(channel)) {
				this.channels.add(channel);
				channel.addChatter(this);
			}
			return true;
		}
		return false;
	}
	
	public boolean addChannel(Channel channel) {
		if (!channels.contains(channel)) {
			channels.add(channel);
			channel.addChatter(this);
			return true;
		}
		return false;
	}
	
	public boolean removeChannel(Channel channel) {
		if (channels.contains(channel)) {
			channels.remove(channel);
			
			if (channel.equals(activeChannel)) {
				if (channels.size() > 0) {
					setActiveChannel(channels.get(0));
				} else {
					setActiveChannel(storage.getPlugin().getChannelManager().defaultChannel);
					addChannel(storage.getPlugin().getChannelManager().defaultChannel);
				}
			}
			return true;
		}
		return false;
	}
	
	public Channel getActiveChannel() {
		return this.activeChannel;
	}
	
	public void setChannels(List<Channel> channels) {
		this.channels = channels;
	}
	
	public List<Channel> getChannels() {
		return this.channels;
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
	
	public ChatterStorage getChatterStorage() {
		return this.storage;
	}

}
