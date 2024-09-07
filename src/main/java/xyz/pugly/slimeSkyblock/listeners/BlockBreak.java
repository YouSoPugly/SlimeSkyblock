package xyz.pugly.slimeSkyblock.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import xyz.pugly.slimeSkyblock.island.Island;
import xyz.pugly.slimeSkyblock.island.IslandManager;
import xyz.pugly.slimeSkyblock.utils.Lang;

public class BlockBreak implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {

        // Check if player is in an island
        IslandManager im = IslandManager.instance();
        Island island = im.getIsland(e.getBlock().getLocation());
        if (island == null) return;

        if (e.getBlock().getType().equals(Material.HOPPER))
            island.removeHopper();
    }
}
