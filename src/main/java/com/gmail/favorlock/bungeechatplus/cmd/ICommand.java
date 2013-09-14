package com.gmail.favorlock.bungeechatplus.cmd;

import net.md_5.bungee.api.CommandSender;

public abstract interface ICommand {

	public abstract void cancelInteraction(CommandSender sender);

	public abstract boolean execute(CommandSender sender, String identifier, String[] args);

	public abstract String getDescription();

	public abstract String[] getIdentifiers();

	public abstract int getMaxArguments();

	public abstract int getMinArguments();

	public abstract String getName();

	public abstract String[] getNotes();

	public abstract String getPermission();

	public abstract String getUsage();

	public abstract boolean isIdentifier(CommandSender paramCommandSender, String paramString);

	public abstract boolean isInProgress(CommandSender paramCommandSender);

	public abstract boolean isInteractive();

	public abstract boolean isShownOnHelpMenu();

}
