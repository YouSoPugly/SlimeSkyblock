package xyz.pugly.slimeSkyblock.listeners.flags;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import xyz.pugly.slimeSkyblock.events.PluginInitializeEvent;
import xyz.pugly.slimeSkyblock.island.Island;
import xyz.pugly.slimeSkyblock.island.IslandManager;
import xyz.pugly.slimeSkyblock.island.flags.IslandFlag;

public class MobGriefFlag implements Listener {

    private static IslandFlag mobGrief;

    @EventHandler
    public static void onPluginInit(PluginInitializeEvent event) {
        IslandFlag.register("MOB-GRIEF");
        mobGrief = IslandFlag.getByName("MOB-GRIEF");
    }

    @EventHandler
    public static void onMobGrief(EntityChangeBlockEvent e) {
        Island is = IslandManager.instance().getIsland(e.getBlock().getLocation());

        if (!is.checkFlag(mobGrief))
            e.setCancelled(true);
    }

}
