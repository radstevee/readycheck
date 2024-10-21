package net.radstevee.readycheck;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    private final ReadyCheckState state;

    public ChatListener() {
        final ReadyCheckPlugin pluginInstance = ReadyCheckPlugin.instance();
        Bukkit.getPluginManager().registerEvents(this, pluginInstance);
        state = ReadyCheckPlugin.state();
    }

    @EventHandler
    private void on(AsyncPlayerChatEvent event) {
        if (state.isReadyCheckOngoing()) {
            event.setCancelled(true);
        }
    }
}
