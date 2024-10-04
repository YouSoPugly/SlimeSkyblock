package xyz.pugly.slimeSkyblock.listeners.flags;

import org.bukkit.Tag;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;
import xyz.pugly.slimeSkyblock.events.PluginInitializeEvent;
import xyz.pugly.slimeSkyblock.island.Island;
import xyz.pugly.slimeSkyblock.island.IslandManager;
import xyz.pugly.slimeSkyblock.island.flags.IslandFlag;

public class LeafDecayFlag implements Listener {

    private static IslandFlag leafDecay;

    @EventHandler
    public static void onPluginInit(PluginInitializeEvent event) {
        IslandFlag.register("LEAF-DECAY");
        leafDecay = IslandFlag.getByName("LEAF-DECAY");
    }

    @EventHandler
    public static void onLeafDecay(BlockFadeEvent e) {
        if (!Tag.LEAVES.isTagged(e.getBlock().getType()))
            return;

        Island is = IslandManager.instance().getIsland(e.getBlock().getLocation());

        if (!is.checkFlag(leafDecay))
            e.setCancelled(true);
    }

}
