package net.radstevee.readycheck;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReadyCheckState {
    private boolean isReadyCheckOngoing = false;
    private List<UUID> participatingPlayers = new ArrayList<>();
    private List<UUID> readyPlayers = new ArrayList<>();

    public List<UUID> participatingPlayers() {
        return this.participatingPlayers;
    }

    public void participatingPlayers(List<UUID> participatingPlayers) {
        this.participatingPlayers = participatingPlayers;
    }

    public boolean isReadyCheckOngoing() {
        return this.isReadyCheckOngoing;
    }

    public void readyCheckOngoing(boolean readyCheckOngoing) {
        this.isReadyCheckOngoing = readyCheckOngoing;
    }

    public List<UUID> readyPlayers() {
        return this.readyPlayers;
    }

    public void readyPlayers(List<UUID> readyPlayers) {
        this.readyPlayers = readyPlayers;
    }

    public void readyPlayer(UUID player) {
        this.readyPlayers.add(player);
    }
}
