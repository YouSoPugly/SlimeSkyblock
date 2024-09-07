package xyz.pugly.slimeSkyblock.listeners.permissions;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.pugly.slimeSkyblock.events.PluginInitializeEvent;
import xyz.pugly.slimeSkyblock.island.Island;
import xyz.pugly.slimeSkyblock.island.IslandManager;
import xyz.pugly.slimeSkyblock.island.IslandPermission;
import xyz.pugly.slimeSkyblock.utils.Lang;

public class CropTramplePermission implements Listener {

    private static IslandPermission permission;

    @EventHandler
    public void onPluginInit(PluginInitializeEvent event) {
        IslandPermission.register("CROP_TRAMPLE");
        permission = IslandPermission.getByName("CROP_TRAMPLE");
    }

    @EventHandler
    public void onTrample(PlayerInteractEvent event) {
        if (!event.hasBlock())
            return;

        if (!event.getAction().equals(Action.PHYSICAL))
            return;

        if (!event.getClickedBlock().getType().equals(Material.FARMLAND))
            return;

        Island is = IslandManager.instance().getIsland(event.getClickedBlock().getLocation());
        if (is == null)
            return;

        if (!is.hasPermission(permission, event.getPlayer())) {
            event.getPlayer().sendMessage(Lang.get(permission.getName() + "_DENY"));
            event.setCancelled(true);
        }
    }

}
