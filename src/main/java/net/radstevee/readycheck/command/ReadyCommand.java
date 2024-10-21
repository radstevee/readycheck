package net.radstevee.readycheck.command;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.radstevee.readycheck.ReadyCheckPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class ReadyCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (!ReadyCheckPlugin.state().isReadyCheckOngoing()) {
                sender.spigot().sendMessage(
                        new ComponentBuilder()
                                .append("There is no ready check ongoing!")
                                .color(ChatColor.RED)
                                .build()
                );

                return true;
            }

            ReadyCheckPlugin.instance().playerReady(player);
            return true;
        }

        sender.spigot().sendMessage(
                new ComponentBuilder()
                        .append("Only players can execute this command!")
                        .color(ChatColor.RED)
                        .build()
        );

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return List.of();
    }
}
