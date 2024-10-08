package xyz.pugly.slimeSkyblock.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import xyz.pugly.slimeSkyblock.SlimeSkyblock;

public class PluginInitializeEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public PluginInitializeEvent() {
        super(false);
    }
}
