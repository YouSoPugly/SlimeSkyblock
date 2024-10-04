package xyz.pugly.slimeSkyblock.listeners.flags;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockSpreadEvent;
import xyz.pugly.slimeSkyblock.events.PluginInitializeEvent;
import xyz.pugly.slimeSkyblock.island.Island;
import xyz.pugly.slimeSkyblock.island.IslandManager;
import xyz.pugly.slimeSkyblock.island.flags.IslandFlag;

public class FireSpreadFlag implements Listener {

    private static IslandFlag fireSpread;

    @EventHandler
    public static void onPluginInit(PluginInitializeEvent event) {
        IslandFlag.register("FIRE-SPREAD");
        fireSpread = IslandFlag.getByName("FIRE-SPREAD");
    }

    @EventHandler
    public static void onFireSpread(BlockSpreadEvent e) {
        if (!e.getSource().getType().equals(Material.FIRE))
            return;

        Island is = IslandManager.instance().getIsland(e.getBlock().getLocation());

        if (!is.checkFlag(fireSpread))
            e.setCancelled(true);
    }

}
