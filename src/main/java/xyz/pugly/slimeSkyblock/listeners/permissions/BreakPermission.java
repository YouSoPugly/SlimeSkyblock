package xyz.pugly.slimeSkyblock.listeners.permissions;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import xyz.pugly.slimeSkyblock.events.PluginInitializeEvent;
import xyz.pugly.slimeSkyblock.island.Island;
import xyz.pugly.slimeSkyblock.island.IslandManager;
import xyz.pugly.slimeSkyblock.island.IslandPermission;
import xyz.pugly.slimeSkyblock.utils.Lang;

public class BreakPermission implements Listener {

    private static IslandPermission permission;

    @EventHandler
    public void onPluginInit(PluginInitializeEvent event) {
        IslandPermission.register("BREAK");
        permission = IslandPermission.getByName("BREAK");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Island is = IslandManager.instance().getIsland(event.getBlock().getLocation());
        if (is == null) {
            return;
        }

        if (!is.hasPermission(permission, event.getPlayer())) {
            event.getPlayer().sendMessage(Lang.get(permission.getName() + "_DENY"));
            event.setCancelled(true);
        }
    }
}
