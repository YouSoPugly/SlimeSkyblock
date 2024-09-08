package xyz.pugly.slimeSkyblock.listeners.permissions;

import io.papermc.paper.event.player.PlayerNameEntityEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.pugly.slimeSkyblock.events.PluginInitializeEvent;
import xyz.pugly.slimeSkyblock.island.Island;
import xyz.pugly.slimeSkyblock.island.IslandManager;
import xyz.pugly.slimeSkyblock.island.permissions.IslandPermission;
import xyz.pugly.slimeSkyblock.utils.Lang;

public class NameEntityPermission implements Listener {

    private static IslandPermission permission;

    @EventHandler
    public void onPluginInit(PluginInitializeEvent event) {
        IslandPermission.register("NAME-ENTITY");
        permission = IslandPermission.getByName("NAME-ENTITY");
    }

    @EventHandler
    public void onNameEntity(PlayerNameEntityEvent e) {
        Island is = IslandManager.instance().getIsland(e.getPlayer().getLocation());
        if (is == null) return;

        if (!is.hasPermission(permission, e.getPlayer())) {
            e.getPlayer().sendMessage(Lang.get(permission.getName() + "-DENY"));
            e.setCancelled(true);
        }
    }

}
