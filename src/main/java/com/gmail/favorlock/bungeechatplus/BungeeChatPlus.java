package com.gmail.favorlock.bungeechatplus;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import com.gmail.favorlock.bungeechatplus.commands.Verbose;
import com.gmail.favorlock.bungeechatplus.entities.Chatter;
import com.gmail.favorlock.bungeechatplus.listeners.NoSwearListener;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class BungeeChatPlus extends Plugin {
	
	private NoSwearConfig config;
	private Map<String,Chatter> chatters;
	private CommandSender console;
	private RegexManager regex;
	
	public void onEnable() {
		try {
			// Initialize Config
			config = new NoSwearConfig(this);
			config.init();
		} catch(Exception ex) {
			getProxyServer().getLogger().log(Level.SEVERE, "FAILED TO LOAD CONFIG!!!", ex);
			return;
		}
		try {
			// Initialize Console CommandSender
			console = (CommandSender) Class.forName("net.md_5.bungee.command.ConsoleCommandSender").getDeclaredMethod("getInstance").invoke(null);
		} catch ( Exception ex ) {
			ex.printStackTrace();
		}
		// Initialize Chatters
		chatters = new HashMap<String,Chatter>();
		// Initialize RegexManager
		regex = new RegexManager(this);
		// Load Regex Rules
		regex.loadRules();
		// Load Listeners
		registerListeners();
		// Load Commands
		registerCommands();
	}
	
	public void onDisable() {
		// Clear Regex Rules and Patterns
		regex.getRules().clear();
		regex.getPatterns().clear();
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
	
	public ProxyServer getProxyServer() {
		return ProxyServer.getInstance();
	}
	
	public PluginManager getPluginManager() {
		return ProxyServer.getInstance().getPluginManager();
	}
	
	public RegexManager getRegexManager() {
		return this.regex;
	}
	
	public CommandSender getConsole() {
		return this.console;
	}
	
	public Collection<ProxiedPlayer> getPlayers() {
		return getProxyServer().getPlayers();
	}
	
	public Chatter getChatter(String name) {
		return this.chatters.get(name);
	}
	
	public void addChatter(LoginEvent event) {
		this.chatters.put(event.getConnection().getName(), new Chatter(event.getConnection().getName(), this));
	}
}