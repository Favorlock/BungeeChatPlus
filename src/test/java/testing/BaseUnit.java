package testing;

import com.gmail.favorlock.bungeechatplus.BungeeChatPlus;
import com.gmail.favorlock.bungeechatplus.ChannelManager;
import com.gmail.favorlock.bungeechatplus.ChatterManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.PluginDescription;

import static org.mockito.Mockito.mock;

public abstract class BaseUnit {
	
	public PluginDescription pdf;
	public BungeeChatPlus plugin;
	public ChatterManager chatterManager;
    public ChannelManager channelManager;
	public ProxiedPlayer player;
	
	public BaseUnit() {
		pdf = mock(PluginDescription.class);
		plugin = mock(BungeeChatPlus.class);
		chatterManager = mock(ChatterManager.class);
        channelManager = mock(ChannelManager.class);
		player = mock(ProxiedPlayer.class);
	}

}
