package xyz.pugly.slimeSkyblock.listeners.flags;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import xyz.pugly.slimeSkyblock.events.PluginInitializeEvent;
import xyz.pugly.slimeSkyblock.island.Island;
import xyz.pugly.slimeSkyblock.island.IslandManager;
import xyz.pugly.slimeSkyblock.island.flags.IslandFlag;

public class ExplosionFlag implements Listener {

    private static IslandFlag explosion;

    @EventHandler
    public static void onPluginInit(PluginInitializeEvent event) {
        IslandFlag.register("EXPLOSION");
        explosion = IslandFlag.getByName("EXPLOSION");
    }

    @EventHandler
    public static void onExplosion(EntityExplodeEvent e) {
        Island is = IslandManager.instance().getIsland(e.getLocation());

        if (!is.checkFlag(explosion))
            e.setCancelled(true);
    }

}
