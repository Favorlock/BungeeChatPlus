package com.gmail.favorlock.bungeechatplus.cmd;

import net.md_5.bungee.api.CommandSender;

public abstract class BaseCommand implements ICommand {

	private final String name;
	private String description = "", usage = "", permission = "";
	private String[] notes = new String[0], identifiers = new String[0];
	private int minArguments = 0, maxArguments = 0;

	public BaseCommand(String name) {
		this.name = name;
	}

	public void cancelInteraction(CommandSender executor) {
	}

	public String getDescription() {
		return this.description;
	}

	public String[] getIdentifiers() {
		return this.identifiers;
	}

	public int getMaxArguments() {
		return this.maxArguments;
	}

	public int getMinArguments() {
		return this.minArguments;
	}

	public String getName() {
		return this.name;
	}

	public String[] getNotes() {
		return this.notes;
	}

	public String getPermission() {
		return this.permission;
	}

	public String getUsage() {
		return this.usage;
	}

	public boolean isIdentifier(CommandSender executor, String input) {
		for (String identifier : this.identifiers) {
			if (input.equalsIgnoreCase(identifier)) {
				return true;
			}
		}
		return false;
	}

	public boolean isInProgress(CommandSender executor) {
		return false;
	}

	public boolean isInteractive() {
		return false;
	}

	public boolean isShownOnHelpMenu() {
		return true;
	}

	public void setArgumentRange(int min, int max) {
		this.minArguments = min;
		this.maxArguments = max;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setIdentifiers(String[] identifiers) {
		this.identifiers = identifiers;
	}

	public void setNotes(String[] notes) {
		this.notes = notes;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

}
