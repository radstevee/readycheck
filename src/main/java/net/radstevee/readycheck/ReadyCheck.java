package net.radstevee.readycheck;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.radstevee.readycheck.command.ReadyCheckCommand;
import net.radstevee.readycheck.command.ReadyCommand;
import net.radstevee.readycheck.command.RecollectPlayers;
import net.radstevee.readycheck.config.ReadyCheckConfig;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class ReadyCheck extends JavaPlugin {
    private static ReadyCheck instance;
    private static ReadyCheckState state;

    private ReadyCheckConfig config;

    public static ReadyCheck instance() {
        return instance;
    }

    public static ReadyCheckState state() {
        return state;
    }

    public ReadyCheckConfig config() {
        return this.config;
    }

    private ReadyCheckConfig initConfig() {
        File configFile = new File(getDataFolder(), "config.yml");

        YamlConfigurationLoader configurationLoader = YamlConfigurationLoader.builder()
                .path(configFile.toPath())
                .build();

        CommentedConfigurationNode node;
        try {
            node = configurationLoader.load();
        } catch (ConfigurateException exception) {
            getLogger().log(Level.SEVERE, "Error parsing configuration, see message below:");
            throw new RuntimeException(exception);
        }

        if (!configFile.exists()) {
            try {
                configFile.getParentFile().mkdirs();
                node.set(ReadyCheckConfig.class, new ReadyCheckConfig());
                configurationLoader.save(node);
            } catch (ConfigurateException e) {
                getLogger().log(Level.SEVERE, "Failed writing default configuration, see message below:");
                throw new RuntimeException(e);
            }
        }

        ReadyCheckConfig config;
        try {
            config = node.get(ReadyCheckConfig.class);
        } catch (SerializationException e) {
            getLogger().log(Level.SEVERE, "Failed serialising configuration, see message below:");
            throw new RuntimeException(e);
        }

        return config;
    }

    public List<UUID> collectParticipatingPlayers() {
        List<UUID> players = new ArrayList<>();
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();

        for (String teamName : config.teamNames()) {
            Team team = board.getTeam(teamName);

            if (team == null) {
                getLogger().log(Level.SEVERE, String.format("Could not find team with name %s", teamName));
                continue;
            }

            for (String entry : team.getEntries()) {
                @SuppressWarnings("deprecation") // Teams use player names because they're ancient
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(entry);

                if (config.onlyOnlinePlayers()) {
                    if (!offlinePlayer.isOnline()) continue;
                }

                players.add(offlinePlayer.getUniqueId());
            }
        }

        return players;
    }

    public void executeReadyCheck() {
        state.readyCheckOngoing(true);
        state.readyPlayers(new ArrayList<>());

        BaseComponent component = TextComponent.fromLegacy(
                config.readyCheckMessageFormat()
                        .replace("{max}", String.valueOf(state.participatingPlayers().size()))
        );
        component.setClickEvent(
                new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ready")
        );
        System.out.println(component.getClickEvent());

        Bukkit.spigot().broadcast(component);
    }

    public void playerReady(Player player) {
        state.readyPlayer(player.getUniqueId());

        Bukkit.broadcastMessage(
                config.playerIsReadyMessageFormat()
                        .replace("{ready}", String.valueOf(state.readyPlayers().size()))
                        .replace("{max}", String.valueOf(state.participatingPlayers().size()))
                        .replace("{name}", player.getDisplayName())
        );

        if (state.readyPlayers().size() == state.participatingPlayers().size()) {
            endReadyCheck(true);
        }
    }

    public void endReadyCheck(boolean allReady) {
        state.readyPlayers(new ArrayList<>());
        state.readyCheckOngoing(false);

        if (!allReady) return;

        @Nullable String readyMessageFormat = config.readyMessageFormat();
        if (readyMessageFormat != null) {
            Bukkit.broadcastMessage(
                    readyMessageFormat.replace("{max}", String.valueOf(state.participatingPlayers().size()))
            );
        }

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), config.commandOnReady());
    }

    @Override
    public void onEnable() {
        instance = this;

        config = initConfig();

        ReadyCheckState initialState = new ReadyCheckState();
        initialState.participatingPlayers(collectParticipatingPlayers());

        state = initialState;

        new ChatListener();

        getCommand("ready-check").setExecutor(new ReadyCheckCommand());
        getCommand("ready").setExecutor(new ReadyCommand());
        getCommand("recollect-players").setExecutor(new RecollectPlayers());
    }
}
