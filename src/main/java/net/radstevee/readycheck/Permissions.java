package net.radstevee.readycheck;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.CommandSender;

public final class Permissions {
    private Permissions() {}

    public static final String READY_CHECK_PERMISSION = "readycheck";
    public static final String RECOLLECT_PLAYERS_PERMISSION = "readycheck.recollect_players";

    public static void sendInsufficientPermissionMessage(CommandSender sender) {
        sender.spigot().sendMessage(
                new ComponentBuilder()
                        .append("You do not have sufficient permissions to execute this command.")
                        .color(ChatColor.RED)
                        .build()
        );
    }
}
