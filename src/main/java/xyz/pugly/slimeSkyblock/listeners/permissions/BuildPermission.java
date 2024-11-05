package xyz.pugly.slimeSkyblock.listeners.permissions;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import xyz.pugly.slimeSkyblock.events.PluginInitializeEvent;
import xyz.pugly.slimeSkyblock.island.Island;
import xyz.pugly.slimeSkyblock.island.IslandManager;
import xyz.pugly.slimeSkyblock.island.permissions.IslandPermission;
import xyz.pugly.slimeSkyblock.utils.Lang;

public class BuildPermission implements Listener {

    private static IslandPermission permission;

    @EventHandler
    public void onPluginInit(PluginInitializeEvent event) {
        IslandPermission.register("BUILD");
        permission = IslandPermission.getByName("BUILD");
    }

    @EventHandler
    public void onBlockBUILD(BlockPlaceEvent event) {
        Island is = IslandManager.instance().getIsland(event.getBlock().getLocation());
        if (is == null) {
            return;
        }

        if (!is.hasPermission(permission, event.getPlayer())) {
            event.getPlayer().sendMessage(Lang.getPermission(permission.getName()));
            event.setCancelled(true);
        }
    }
    
}
