package xyz.pugly.slimeSkyblock.player;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SPlayer {

    private final Player player;

    private final HashSet<UUID> islands = new HashSet<>();
    private UUID mainIsland;

    public SPlayer(Player player) {
        this.player = player;
        this.mainIsland = null;
    }

    public SPlayer(Player player, UUID mainIsland, Set<UUID> islands) {
        this.player = player;
        this.mainIsland = mainIsland;
        this.islands.addAll(islands);
    }

    public Player getPlayer() {
        return player;
    }

    public HashSet<UUID> getIslands() {
        return islands;
    }

    public void addIsland(UUID island) {
        islands.add(island);
    }

    public void removeIsland(UUID island) {
        islands.remove(island);
    }

    public UUID getMainIsland() {
        return mainIsland;
    }

    public void setMainIsland(UUID mainIsland) {
        this.mainIsland = mainIsland;
    }

}
