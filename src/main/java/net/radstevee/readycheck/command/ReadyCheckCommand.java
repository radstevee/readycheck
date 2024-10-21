package net.radstevee.readycheck.command;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.radstevee.readycheck.Permissions;
import net.radstevee.readycheck.ReadyCheck;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.List;
import java.util.Objects;

public class ReadyCheckCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(Permissions.READY_CHECK_PERMISSION)) {
            Permissions.sendInsufficientPermissionMessage(sender);
            return true;
        }

        if (args.length > 0) {
            if (Objects.equals(args[0], "end")) {
                ReadyCheck.instance().endReadyCheck(false);
                sender.spigot().sendMessage(
                        new ComponentBuilder()
                                .append("Ended the ready check")
                                .build()
                );
            }
        } else {
            if (ReadyCheck.state().participatingPlayers().isEmpty()) {
                sender.spigot().sendMessage(
                        new ComponentBuilder()
                                .append("No players found")
                                .color(ChatColor.RED)
                                .build()
                );

                return true;
            }

            ReadyCheck.instance().executeReadyCheck();
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return List.of("end");
    }
}
