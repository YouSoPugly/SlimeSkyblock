package xyz.pugly.slimeSkyblock.listeners.flags;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import xyz.pugly.slimeSkyblock.events.PluginInitializeEvent;
import xyz.pugly.slimeSkyblock.island.Island;
import xyz.pugly.slimeSkyblock.island.IslandManager;
import xyz.pugly.slimeSkyblock.island.flags.IslandFlag;

public class VineGrowthFlag implements Listener {

    private static IslandFlag vineGrowth;

    @EventHandler
    public static void onPluginInit(PluginInitializeEvent event) {
        IslandFlag.register("VINE-GROWTH");
        vineGrowth = IslandFlag.getByName("VINE-GROWTH");
    }

    @EventHandler
    public static void onVineGrowth(BlockGrowEvent e) {
        if (!e.getBlock().getType().equals(Material.VINE))
            return;

        Island is = IslandManager.instance().getIsland(e.getBlock().getLocation());

        if (!is.checkFlag(vineGrowth))
            e.setCancelled(true);
    }

}
