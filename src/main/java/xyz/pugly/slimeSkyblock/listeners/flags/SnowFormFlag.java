package xyz.pugly.slimeSkyblock.listeners.flags;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;
import xyz.pugly.slimeSkyblock.events.PluginInitializeEvent;
import xyz.pugly.slimeSkyblock.island.Island;
import xyz.pugly.slimeSkyblock.island.IslandManager;
import xyz.pugly.slimeSkyblock.island.flags.IslandFlag;

public class SnowFormFlag implements Listener {

    private static IslandFlag snowForm;

    @EventHandler
    public static void onPluginInit(PluginInitializeEvent event) {
        IslandFlag.register("SNOW-FORM");
        snowForm = IslandFlag.getByName("SNOW-FORM");
    }

    @EventHandler
    public static void onSnowForm(BlockFormEvent e) {
        if (!e.getNewState().getType().equals(Material.SNOW))
            return;

        Island is = IslandManager.instance().getIsland(e.getBlock().getLocation());

        if (!is.checkFlag(snowForm))
            e.setCancelled(true);
    }

}
