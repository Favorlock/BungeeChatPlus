package com.gmail.favorlock.bungeechatplus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;

public class RegexManager {
	
	BungeeChatPlus plugin;
	private CopyOnWriteArrayList<String> rules;
	private ConcurrentHashMap<String, Pattern> patterns;
	
	public RegexManager(BungeeChatPlus plugin) {
		this.plugin = plugin;
		this.rules = new CopyOnWriteArrayList<String>();
		this.patterns = new ConcurrentHashMap<String, Pattern>();
	}
	
	public void loadRules() {
		File directory = new File("plugins" + File.separator + plugin.getDescription().getName());
		File file = new File(directory, "rules.txt");
		
		if (!directory.exists()) {
			directory.mkdir();
		}
		
		if(!file.exists()) {
			BufferedWriter output;
			
			try {
				output = new BufferedWriter(new FileWriter(file));
				output.write("# BungeeChatPlus rules.txt - Define Regular Expression Rules\n");
				output.write("# SAMPLE RULES\n");
				output.write("# NOTE: ALL MATCHES AUTOMATICALLY IGNORE CASE\n");
				output.write("# Each rule must have one 'match' statement and atleast one 'then' statement\n");
				output.write("# match <regular expression>\n");
				output.write("# ignore|require <user|permission|string> *(optional)\n");				
				output.write("# then <replace|rewrite|warn|log|deny|debug|kick|console> <string>\n");
				output.write("\n");
				output.write("# EXAMPLES\n");
				output.write("\n");
				output.write("# Replace F Bomb variants with fudge. Also catches ffffuuuccckkk\n");
				output.write("match f+u+c+k+|f+u+k+|f+v+c+k+|f+u+q+\n");
				output.write("then replace fudge\n");
				output.write("then warn Watch your language please\n");
				output.write("then log\n");
				output.write("\n");
				output.write("# Replace a list of naughty words with random word! Let a certain permission swear.\n");
				output.write("match cunt|whore|fag|slut|queer|bitch|bastard\n");
				output.write("ignore permission permission.node\n");
				output.write("then randrep meep|beep|bleep|herp|derp\n");
				output.write("\n");
				output.write("# Fun: rewrite tremor with pretty colors. Only let player tremor77 use it\n");
				output.write("match \\btremor+\\b|\\btrem+\\b\n");
				output.write("require user tremor77\n");
				output.write("then rewrite &bt&cREM&bor&f\n");
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    	try {
        	BufferedReader input =  new BufferedReader(new FileReader(file));
    		String line = null;
    		while (( line = input.readLine()) != null) {
    			line = line.trim();
    			if (!line.matches("^#.*") && !line.matches("")) {
    				rules.add(line);
    				if (line.startsWith("match ") || line.startsWith("catch ") || line.startsWith("replace ") || line.startsWith("rewrite ")) {
    					String[] parts = line.split(" ", 2);
    					compilePattern(parts[1]);
    				}
    			}
    		}
    		input.close();
    	}
    	catch (FileNotFoundException e) {
    		plugin.getProxyServer().getLogger().warning("Error reading config file '" + file + "': " + e.getLocalizedMessage());
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
	}
	
    private void compilePattern(String re) {
    	// Do not re-compile if we already have this pattern 
    	if (patterns.get(re) == null) {
    		try {
    			Pattern pattern = Pattern.compile(re, Pattern.CASE_INSENSITIVE);
    			patterns.put(re, pattern);
    			plugin.getProxyServer().getLogger().fine("Successfully compiled regex: " + re);
    		}
    		catch (PatternSyntaxException e) {
    			plugin.getProxyServer().getLogger().warning("Failed to compile regex: " + re);
    			plugin.getProxyServer().getLogger().warning(e.getMessage());
    		}
    		catch (Exception e) {
    			plugin.getProxyServer().getLogger().severe("Unexpected error while compiling expression '" + re + "'");
    			e.printStackTrace();
    		}
    	}
    }
    
    public Boolean matchPattern(String msg, String re_from) {
    	Pattern pattern_from = patterns.get(re_from);
    	if (pattern_from == null) {
    		// Pattern failed to compile, ignore
    		logToFile("ignoring invalid regex: " + re_from);
    		return false;
    	}
    	Matcher matcher = pattern_from.matcher(msg);
    	return matcher.find();
    }
    
    public String replacePattern(String msg, String re_from, String to) {
    	Pattern pattern_from = patterns.get(re_from);
    	if (pattern_from == null) {
    		// Pattern failed to compile, ignore
    		return msg;
    	}
    	Matcher matcher = pattern_from.matcher(msg);
    	return matcher.replaceAll(to);
    }

    public String replacePatternLower(String msg, String re_from) {
    	String text = msg;
    	Matcher m = Pattern.compile(re_from).matcher(text);
    	
    	StringBuilder sb = new StringBuilder();
    	int last = 0;
    	while (m.find()) {
    		sb.append(text.substring(last, m.start()));
    		sb.append(m.group(0).toLowerCase());
    		last = m.end();
    	}
    	sb.append(text.substring(last));
    	return sb.toString();
    }

    public String replacePatternRandom(String msg, String re_from, String to) {
    	Pattern pattern_from = patterns.get(re_from);
    	if (pattern_from == null) {
    		// Pattern failed to compile, ignore
    		return msg;
    	}
    	Matcher matcher = pattern_from.matcher(msg);
    	
    	String[] toRand = to.split("\\|");
    	
		Random random = new Random();
		int randomInt = random.nextInt(toRand.length);
    	return matcher.replaceAll(toRand[randomInt]);
    }
    
    public void filterChat(ChatEvent event) {
        String message = event.getMessage();
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        String pname = player.getName();

        // Permissions Check, if player has bypass permissions, then skip everything.
        if (!(player.hasPermission("BungeeChatPlus.bypass"))) {
	    	// Booleans
        	Boolean cancel = false;
	    	Boolean kick = false;
	    	Boolean warn = false;
	    	Boolean console = false;
	    	Boolean consolechain = false;
	    	Boolean matched = false;
	    	Boolean log = false;
	    	Boolean aborted = false;
	    	Boolean valid;	

	    	// Strings
	    	String consolecmd = "";
	    	String regex = "";
	    	String matched_msg = "";
	    	String matchLogMsg = "";
	    	
	    	// More Strings (for warns, kick, etc)
	    	String warnmsg = plugin.getConfig().Settings_warnmsg;
	    	String kickmsg = plugin.getConfig().Settings_kickmsg;
	    	
	    	// Apply rules 
	    	for (String line : this.rules) {
	    		if (aborted) { break; } 
	    		valid = false;
	    		    
	    		if (line.startsWith("match ")) {
	    			regex = line.substring(6); 			
	    			matched = this.matchPattern(com.gmail.favorlock.bungeechatplus.utils.FontFormat.stripColor(message.replaceAll("&([0-9a-fk-or])", "\u00A7$1")), regex); 			
	    			if (matched) {
	    				matched_msg = message;
	    				matchLogMsg = "MATCH <"+player.getName() + "> " + event.getMessage();
	    			}
	    			valid = true;
	    		}    		
	    		if (matched) {  
	    			// Check for any ignore statements, made faster by grouping together v2.1.1
	    			if (line.startsWith("ignore")) {		
		        		if (line.startsWith("ignore user ")) {
		        			String users = line.substring(12);
		    				valid = true;
		        			for (String check : users.split(" ")) {
		        				if (pname.equalsIgnoreCase(check)) {
		        					matched = false;
		        					break;
		        				}
		        			}
		        		}
		        		if (line.startsWith("ignore permission ")) {
		        			String perms = line.substring(18);
		    				valid = true;
		    				for (String check : perms.split(" ")) {
		    					if (player.hasPermission(check)) {
			        				matched = false;
			        				break;
		    					}
		        			}
		        		} 		
		        		if (line.startsWith("ignore string ")) {
		        			String ignorestring = line.substring(14);
		    				valid = true;
		    				for (String check : ignorestring.split("\\|")) {
		    					if (com.gmail.favorlock.bungeechatplus.utils.FontFormat.stripColor(message.replaceAll("&([0-9a-fk-or])", "\u00A7$1")).toUpperCase().indexOf(check.toUpperCase()) != -1) {
			        				matched = false;
			        				break;
		                        }	
		        			}
		        		}  
	    			}
	    			// Check for any require statements
	    			if (line.startsWith("require")) {
		        		if (line.startsWith("require user ")) {
		        			String users = line.substring(13);
		    				valid = true;
		    				Boolean found = false;
		        			for (String check : users.split(" ")) {
		        				if (pname.equalsIgnoreCase(check)) {
		        					found = true;
		        					break;
		        				}
		        			}
		        			matched = found;
		        		}
		        		if (line.startsWith("require permission ")) {
		        			String perms = line.substring(19);
		    				valid = true;
		    				Boolean found = false;
		    				for (String check : perms.split(" ")) {
			        			if (player.hasPermission(check)) {
			        				found = true;
			        				break;
			        			}
		    				}
		        			matched = found;
		        		}
	    			}
	    			// Finally check for any then statements
	    			if (line.startsWith("then")) {
						if (line.startsWith("then replace ")) {	
							message = com.gmail.favorlock.bungeechatplus.utils.FontFormat.stripColor(message.replaceAll("&([0-9a-fk-or])", "\u00A7$1"));
							message = this.replacePattern(message, regex, line.substring(13));
							message = message.replaceAll("&([0-9a-fk-or])", "\u00A7$1");						
			    			valid = true;
						}
						if (line.matches("then replace")) {	
							message = com.gmail.favorlock.bungeechatplus.utils.FontFormat.stripColor(message.replaceAll("&([0-9a-fk-or])", "\u00A7$1"));						
							message = this.replacePattern(message, regex, "");						
							message = message.replaceAll("&([0-9a-fk-or])", "\u00A7$1");
							valid = true;
						}
						if (line.startsWith("then rewrite ")) {									
							message = this.replacePattern(message, regex, line.substring(13));
							message = message.replaceAll("&([0-9a-fk-or])", "\u00A7$1");
			    			valid = true;
						}
						if (line.matches("then rewrite")) {
							message = this.replacePattern(message, regex, "");
							message = message.replaceAll("&([0-9a-fk-or])", "\u00A7$1");
							valid = true;
						}							
						if (line.startsWith("then randrep ")) {	
                            message = com.gmail.favorlock.bungeechatplus.utils.FontFormat.stripColor(message.replaceAll("&([0-9a-fk-or])", "\u00A7$1"));
							message = this.replacePatternRandom(message, regex, line.substring(13));
							message = message.replaceAll("&([0-9a-fk-or])", "\u00A7$1");
			    			valid = true;
						}							
						if (line.startsWith("then lower")) {
                            message = com.gmail.favorlock.bungeechatplus.utils.FontFormat.stripColor(message.replaceAll("&([0-9a-fk-or])", "\u00A7$1"));							
							message = this.replacePatternLower(message, regex);							
							message = message.replaceAll("&([0-9a-fk-or])", "\u00A7$1");
							valid = true;
						}
						if (line.startsWith("then deny")) {
							cancel = true;
			    			valid = true;
						}
						
						// aliasing, command and console
						if (line.startsWith("then console ")) {
							consolecmd = line.substring(13);
							consolechain = true;
							valid = true;
						}
						if (line.startsWith("then conchain ")) {
							consolecmd = line.substring(13);
							consolechain = true;
							valid = true;
						}
						
						// cancel
						if (line.startsWith("then cancel ")) {
							event.setCancelled(true);
						}
						
						// Punishment stuffs start here
						if (line.startsWith("then warn ")) {
							warnmsg = line.substring(10);
							warn = true;
			    			valid = true;
						}
						if (line.matches("then warn")) {
							warn = true;
			    			valid = true;
						}						
						if (line.startsWith("then kick ")) {
							kickmsg = line.substring(10);
							kick = true;
			    			valid = true;
						}
						if (line.matches("then kick")) {
							kick = true;
			    			valid = true;
						}	
						
						// abort, log, debug stuff
						if (line.startsWith("then abort")) {
							aborted = true;
			    			valid = true;
						}
						if (line.matches("then log")) {
							log = true;
			    			valid = true;
						}	
						if (line.matches("then debug")) {
							System.out.println("[PwnFilter] Debug match: " + regex);
							System.out.println("[PwnFilter] Debug original: " + event.getMessage());
							System.out.println("[PwnFilter] Debug matched: " + matched_msg);
							System.out.println("[PwnFilter] Debug current: " + message);
							System.out.println("[PwnFilter] Debug log: " + (log?"yes":"no"));
							System.out.println("[PwnFilter] Debug deny: " + (cancel?"yes":"no"));
			    			valid = true;
						}						
	    			}
		    		if (valid == false) {
		    			plugin.getProxyServer().getLogger().warning("Ignored syntax error in rules.txt: " + line);    			
		    		}	    		
	    		}
	    	}	
	    	// Perform flagged actions
	    	if (log) {
	    		logToFile(matchLogMsg);
	    		if (cancel){
	    			logToFile("SENT <"+player.getName() + "> message cancelled by deny rule.");	
	    		}
	    		logToFile("SENT <"+player.getName() + "> " + message);
	    	}	
	    	if (cancel) {
	    		event.setCancelled(true);
	    	}   	
	    	// why is this here and not at the end, any particular reason?
	    	else {
				event.setMessage(message);
				plugin.getProxyServer().getLogger().log(Level.SEVERE, "" + event.isCancelled());
			}
	    	if (console) {
	            consolecmd = consolecmd.replaceAll("&player", player.getName());
	            consolecmd = consolecmd.replaceAll("&string", message);
	            logToFile("Sending console command: " + consolecmd);
	            plugin.getPluginManager().dispatchCommand(plugin.getConsole(), consolecmd);
	    	}
	    	if (consolechain) {
	    		consolecmd = consolecmd.replaceAll("&player", player.getName());
	    		consolecmd = consolecmd.replaceAll("&string", message);           
	            String conchain[] = consolecmd.split("\\|");
	            for (String cmds : conchain) {
		            logToFile("Sending console command: " + cmds);
		    		plugin.getPluginManager().dispatchCommand(plugin.getConsole(), cmds);
	            }
			}
	    	if (warn) {
				warnmsg = warnmsg.replaceAll("&([0-9a-fk-or])", "\u00A7$1");
	    		final ProxiedPlayer fplayer = player;
	    		final String fwarning = warnmsg;
	    		logToFile("Warned " + fplayer.getName() + ": " + fwarning);
	    		fplayer.sendMessage(fwarning);	 
	    	}	    	
	    	if (kick) {	
				kickmsg = kickmsg.replaceAll("&([0-9a-fk-or])", "\u00A7$1");	    		
	    		final ProxiedPlayer fplayer = player;
	    		final String freason = kickmsg;
	    		logToFile("Kicked " + fplayer.getName() + ": " + freason);
	    		fplayer.disconnect(freason);	    		
	    	} 		    	
        }   	
    }
    
    public void logToFile(String message) {   
    	// send to the console as info any logTofiles
    	plugin.getProxyServer().getLogger().info(message);  	
    	Boolean logEnabled = plugin.getConfig().Settings_EnableLog;
    	if (logEnabled) {	
	    	try {
	    		File directory = new File("plugins" + File.separator + plugin.getDescription().getName());
	    		File file = new File(directory, "BungeeChatPlus.log");
	    		
	    		if (!directory.exists()) {
	    			directory.mkdir();
	    		}
	    		
			    if (!file.exists())  {
			    	file.createNewFile();
			    }
			    
			    FileWriter fw = new FileWriter(file, true);
			    PrintWriter pw = new PrintWriter(fw);
			    pw.println(getDate() +" "+ message);
			    pw.flush();
			    pw.close();
		    } 
		    catch (IOException e) {
		    	e.printStackTrace();
		    }
    	}
    }
    
    public String getDate() {
    	  String s;
    	  Format formatter;
    	  Date date = new Date(); 
    	  formatter = new SimpleDateFormat("[yyyy/MM/dd HH:mm:ss]");
    	  s = formatter.format(date);
    	  return s;
    }
    
    public CopyOnWriteArrayList<String> getRules() {
    	return this.rules;
    }
    
    public ConcurrentHashMap<String, Pattern> getPatterns() {
    	return this.patterns;
    }

}
