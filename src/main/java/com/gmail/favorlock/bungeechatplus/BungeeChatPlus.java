package com.gmail.favorlock.bungeechatplus;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import com.gmail.favorlock.bungeechatplus.commands.Verbose;
import com.gmail.favorlock.bungeechatplus.entities.Chatter;
import com.gmail.favorlock.bungeechatplus.listeners.NoSwearListener;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeChatPlus extends Plugin {
	
	private NoSwearConfig config;
	private Map<String,Chatter> chatters;
	
	public void onEnable() {
		try {
			 config = new NoSwearConfig(this);
			 config.init();
		} catch(Exception ex) {
			 getProxyServer().getLogger().log(Level.SEVERE, "FAILED TO LOAD CONFIG!!!", ex);
			 return;
		}
		chatters = new HashMap<String,Chatter>();
		registerListeners();
		registerCommands();
	}
	
	public NoSwearConfig getConfig() {
		return config;
	}
	
	private void registerListeners() {
		getProxyServer().getPluginManager().registerListener(new NoSwearListener(this));
	}
	
	private void registerCommands() {
		getProxyServer().getPluginManager().registerCommand(new Verbose(this));
	}
	
	public Collection<ProxiedPlayer> getPlayers() {
		return getProxyServer().getPlayers();
	}
	
	public ProxyServer getProxyServer() {
		return ProxyServer.getInstance();
	}
	
	public Chatter getChatter(String name) {
		return this.chatters.get(name);
	}
	
	public void addChatter(LoginEvent event) {
		this.chatters.put(event.getConnection().getName(), new Chatter(event.getConnection().getName(), this));
	}

}
