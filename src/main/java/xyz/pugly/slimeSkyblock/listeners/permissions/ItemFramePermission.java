package xyz.pugly.slimeSkyblock.listeners.permissions;

import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import xyz.pugly.slimeSkyblock.events.PluginInitializeEvent;
import xyz.pugly.slimeSkyblock.island.Island;
import xyz.pugly.slimeSkyblock.island.IslandManager;
import xyz.pugly.slimeSkyblock.island.permissions.IslandPermission;
import xyz.pugly.slimeSkyblock.utils.Lang;

public class ItemFramePermission implements Listener {

    private static IslandPermission permission;

    @EventHandler
    public void onPluginInit(PluginInitializeEvent event) {
        IslandPermission.register("ITEM-FRAME");
        permission = IslandPermission.getByName("ITEM-FRAME");
    }

    @EventHandler
    public void onInteractt(PlayerInteractAtEntityEvent e) {
        if (!(e.getRightClicked() instanceof ItemFrame)) return;

        Island is = IslandManager.instance().getIsland(e.getPlayer().getLocation());
        if (is == null) return;

        if (!is.hasPermission(permission, e.getPlayer())) {
            e.getPlayer().sendMessage(Lang.get(permission.getName() + "-DENY"));
            e.setCancelled(true);
        }
    }

}
