package xyz.pugly.slimeSkyblock.listeners.permissions;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import xyz.pugly.slimeSkyblock.events.PluginInitializeEvent;
import xyz.pugly.slimeSkyblock.island.Island;
import xyz.pugly.slimeSkyblock.island.IslandManager;
import xyz.pugly.slimeSkyblock.island.permissions.IslandPermission;
import xyz.pugly.slimeSkyblock.utils.Lang;

public class ItemPermissions implements Listener {

    private static IslandPermission dropPerm;
    private static IslandPermission pickupPerm;

    @EventHandler
    public void onPluginInit(PluginInitializeEvent event) {
        IslandPermission.register("ITEM-DROP");
        IslandPermission.register("ITEM-PICKUP");
        dropPerm = IslandPermission.getByName("ITEM-DROP");
        pickupPerm = IslandPermission.getByName("ITEM-PICKUP");
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent e) {
        if (!(e.getEntity() instanceof Player)) return;

        Player player = (Player) e.getEntity();
        Island is = IslandManager.instance().getIsland(player.getLocation());
        if (is == null) return;

        if (!is.hasPermission(pickupPerm, player)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        Island is = IslandManager.instance().getIsland(player.getLocation());
        if (is == null) return;

        if (!is.hasPermission(dropPerm, player)) {
            e.getPlayer().sendMessage(Lang.get(dropPerm.getName() + "-DENY"));
            e.setCancelled(true);
        }
    }

}
