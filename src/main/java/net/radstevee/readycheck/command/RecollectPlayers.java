package net.radstevee.readycheck.command;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.radstevee.readycheck.Permissions;
import net.radstevee.readycheck.ReadyCheck;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.List;
import java.util.UUID;

public class RecollectPlayers implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(Permissions.RECOLLECT_PLAYERS_PERMISSION)) {
            Permissions.sendInsufficientPermissionMessage(sender);
            return true;
        }

        List<UUID> players = ReadyCheck.instance().collectParticipatingPlayers();
        ReadyCheck.state().participatingPlayers(players);

        sender.spigot().sendMessage(
                new ComponentBuilder()
                        .append(String.format("Found %s players", players.size()))
                        .build()
        );

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return List.of();
    }
}
