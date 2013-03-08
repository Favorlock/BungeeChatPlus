package com.gmail.favorlock.bungeechatplus.commands;

import com.gmail.favorlock.bungeechatplus.BungeeChatPlus;
import com.gmail.favorlock.bungeechatplus.entities.Chatter;
import com.gmail.favorlock.bungeechatplus.utils.FontFormat;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Verbose extends Command {
	
	BungeeChatPlus plugin;

	public Verbose(BungeeChatPlus plugin) {
		super("gcv");
		this.plugin = plugin;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer)) {
			return;
		}
		Chatter chatter = this.plugin.getChatter(sender.getName());
		chatter.setVerbose();
		if(chatter.getVerbose() == true) {
			sender.sendMessage(FontFormat.translateString("&7You are now talking in &2Global Chat"));
			return;
		}
		sender.sendMessage(FontFormat.translateString("&7You are no longer talking in &2Global Chat"));
	}

}
