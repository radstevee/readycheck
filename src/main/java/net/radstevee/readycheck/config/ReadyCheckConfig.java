package net.radstevee.readycheck.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("FieldCanBeLocal") // modified by configurate
@ConfigSerializable
public class ReadyCheckConfig {
    @Comment("The teams which the plugin should use the players from.")
    private List<String> teamNames = List.of();

    @Comment("Whether the ready check only includes online players.")
    private boolean onlyOnlinePlayers = true;

    @Comment("The command that should be executed when everybody is ready.")
    private String commandOnReady = "tellraw @a {\"text\": \"All online players are ready!\", \"color\": \"green\"}";

    @Comment("The format of the ready message of one player. Provided variables: ready, max, name")
    private String playerIsReadyMessageFormat = "{ready}/{max}, {name} is ready!";

    @Comment("The format of the ready check message. Provided variables: max. The message will be clickable.")
    private String readyCheckMessageFormat = "Click here to ready up ({max} players)";

    @Comment("The format of the message that gets displayed when all players are ready. Can be none. Provided variables: max")
    private @Nullable String readyMessageFormat = "{max} players are ready!";

    public List<String> teamNames() {
        return this.teamNames;
    }

    public boolean onlyOnlinePlayers() {
        return this.onlyOnlinePlayers;
    }

    public String commandOnReady() {
        return this.commandOnReady;
    }

    public String playerIsReadyMessageFormat() {
        return this.playerIsReadyMessageFormat;
    }

    public String readyCheckMessageFormat() {
        return this.readyCheckMessageFormat;
    }

    @Nullable
    public String readyMessageFormat() {
        return this.readyMessageFormat;
    }
}
