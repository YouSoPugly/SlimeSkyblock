package xyz.pugly.slimeSkyblock.listeners.permissions;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.pugly.slimeSkyblock.events.PluginInitializeEvent;
import xyz.pugly.slimeSkyblock.island.Island;
import xyz.pugly.slimeSkyblock.island.IslandManager;
import xyz.pugly.slimeSkyblock.island.permissions.IslandPermission;
import xyz.pugly.slimeSkyblock.utils.Lang;

public class InteractPermission implements Listener {

    private static IslandPermission permission;

    @EventHandler
    public void onPluginInit(PluginInitializeEvent event) {
        IslandPermission.register("INTERACT");
        permission = IslandPermission.getByName("INTERACT");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) return;

        if (!e.getAction().equals(Action.LEFT_CLICK_BLOCK)) return;

        Material type = e.getClickedBlock().getType();
        boolean interactable = type.isInteractable() &&
                !(
                Tag.STAIRS.isTagged(type) ||
                Tag.FENCES.isTagged(type) ||
                type.equals(Material.MOVING_PISTON)
                );

        if (!interactable) return;

        Island is = IslandManager.instance().getIsland(e.getClickedBlock().getLocation());
        if (is == null) return;

        if (!is.hasPermission(permission, e.getPlayer())) {
            e.getPlayer().sendMessage(Lang.get(permission.getName() + "-DENY"));
            e.setCancelled(true);
        }
    }

}
