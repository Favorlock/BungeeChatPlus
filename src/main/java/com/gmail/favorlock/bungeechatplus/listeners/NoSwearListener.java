package com.gmail.favorlock.bungeechatplus.listeners;

import com.gmail.favorlock.bungeechatplus.BungeeChatPlus;
import com.gmail.favorlock.bungeechatplus.entities.Chatter;
import com.gmail.favorlock.bungeechatplus.utils.FontFormat;
import com.google.common.eventbus.Subscribe;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;

public class NoSwearListener implements Listener {
	
	BungeeChatPlus plugin;
	
	public NoSwearListener(BungeeChatPlus plugin) {
		this.plugin = plugin;
	}
	
	@Subscribe
	public void onPlayerMessage(ChatEvent event) {
		if (!(event.getSender() instanceof ProxiedPlayer)) {
			return;
		}
		
		plugin.getRegexManager().filterChat(event);
		
		String message = event.getMessage();
		ProxiedPlayer sender = (ProxiedPlayer)event.getSender();
		Chatter chatter = plugin.getChatter(sender.getName());
		
/*		for (String word : plugin.getConfig().Filter) {
			String message2 = message.toLowerCase();
			if (message2.contains(word.toLowerCase())) {
				message = message.replaceAll("^(?!.*("+word+")).*$", "****");
				message = message.replace(word, "****");
			}
		}
*/
		
		for (ProxiedPlayer player : plugin.getPlayers()) {
			Chatter listener = plugin.getChatter(player.getName());
			if ((chatter.getVerbose() == true) && (listener.getVerbose() == true)) {
				if (player.getServer().getInfo().getName() != sender.getServer().getInfo().getName()) {
					player.sendMessage(FontFormat.translateString("&7<&2" + sender.getServer().getInfo().getName() +
							"&8-&2" + sender.getDisplayName() + "&7> &r" + message));
				}
			}
		}
	}
	
	@Subscribe
	public void onPlayerLogin(LoginEvent event) {
		plugin.addChatter(event);
	}

}
