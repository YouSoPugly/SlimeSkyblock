package xyz.pugly.slimeSkyblock.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import xyz.pugly.slimeSkyblock.island.Island;
import xyz.pugly.slimeSkyblock.island.IslandPermission;

public class IslandPermissionCheckEvent extends Event {

    private static HandlerList HANDLERS = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    private boolean allowed;
    private IslandPermission permission;
    private Player player;
    private Island island;

    public IslandPermissionCheckEvent(Player player, IslandPermission permission, Island island, boolean allowed) {
        this.player = player;
        this.permission = permission;
        this.island = island;
        this.allowed = allowed;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    public IslandPermission getPermission() {
        return permission;
    }

    public Player getPlayer() {
        return player;
    }

    public Island getIsland() {
        return island;
    }
}
