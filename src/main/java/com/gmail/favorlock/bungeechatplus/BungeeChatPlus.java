package com.gmail.favorlock.bungeechatplus;

import java.util.Collection;
import java.util.logging.Level;

import com.gmail.favorlock.bungeechatplus.cmd.CommandHandler;
import com.gmail.favorlock.bungeechatplus.cmd.commands.BCP;
import com.gmail.favorlock.bungeechatplus.cmd.commands.Create;
import com.gmail.favorlock.bungeechatplus.cmd.commands.Delete;
import com.gmail.favorlock.bungeechatplus.cmd.commands.Focus;
import com.gmail.favorlock.bungeechatplus.cmd.commands.Join;
import com.gmail.favorlock.bungeechatplus.cmd.commands.Leave;
import com.gmail.favorlock.bungeechatplus.cmd.commands.List;
import com.gmail.favorlock.bungeechatplus.cmd.commands.PrivateMessage;
import com.gmail.favorlock.bungeechatplus.cmd.commands.Verbose;
import com.gmail.favorlock.bungeechatplus.config.BungeeChatPlusConfig;
import com.gmail.favorlock.bungeechatplus.listeners.ChatListener;
import com.gmail.favorlock.bungeechatplus.listeners.PluginMessageListener;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class BungeeChatPlus extends Plugin {
	
	private final CommandHandler commandHandler = new CommandHandler(this);
	private ChannelManager channelManager;
	private ChatterManager chatterManager;
	private BungeeChatPlusConfig config;
	private CommandSender console;
	private RegexManager regex;
	
	public void onEnable() {
		try {
			// Initialize Config
			config = new BungeeChatPlusConfig(this);
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
		
		channelManager = new ChannelManager(this);
		chatterManager = new ChatterManager(this);
		// Check if regex is enabled
		if (getConfig().Settings_EnableRegex) {
			// Initialize RegexManager
			regex = new RegexManager(this);
			// Load Regex Rules
			regex.loadRules();
		}
		// Register Channels
		registerChannels();
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
	
	public BungeeChatPlusConfig getConfig() {
		return config;
	}
	
	private void registerListeners() {
		getProxyServer().getPluginManager().registerListener(new ChatListener(this));
		getProxyServer().getPluginManager().registerListener(new PluginMessageListener(this));
	}
	
	private void registerCommands() {
		getProxyServer().getPluginManager().registerCommand(new BCP(this));
		getCommandHandler().addCommand(new Verbose(this));
		getCommandHandler().addCommand(new Create(this));
		getCommandHandler().addCommand(new Delete(this));
		getCommandHandler().addCommand(new Focus(this));
		getCommandHandler().addCommand(new Join(this));
		getCommandHandler().addCommand(new Leave(this));
		getCommandHandler().addCommand(new List(this));
		getCommandHandler().addCommand(new PrivateMessage(this));
	}
	
	private void registerChannels() {
		getProxyServer().registerChannel("BungeeChatPlus");
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
	
	public ChannelManager getChannelManager() {
		return this.channelManager;
	}
	
	public ChatterManager getChatterManager() {
		return this.chatterManager;
	}
	
	public CommandHandler getCommandHandler() {
		return this.commandHandler;
	}
	
	public CommandSender getConsole() {
		return this.console;
	}
	
	public Collection<ProxiedPlayer> getPlayers() {
		return getProxyServer().getPlayers();
	}
}