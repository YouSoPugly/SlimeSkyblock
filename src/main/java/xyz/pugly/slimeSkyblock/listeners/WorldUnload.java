package xyz.pugly.slimeSkyblock.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldUnloadEvent;
import xyz.pugly.slimeSkyblock.island.IslandManager;

public class WorldUnload implements Listener {

    @EventHandler
    public void onWorldUnload(WorldUnloadEvent e) {
        IslandManager.instance().getIsland(e.getWorld()).unloaded();
    }

}
