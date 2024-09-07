package xyz.pugly.slimeSkyblock.listeners.permissions;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import xyz.pugly.slimeSkyblock.events.PluginInitializeEvent;
import xyz.pugly.slimeSkyblock.island.Island;
import xyz.pugly.slimeSkyblock.island.IslandManager;
import xyz.pugly.slimeSkyblock.island.permissions.IslandPermission;
import xyz.pugly.slimeSkyblock.utils.Lang;

public class FlyPermission implements Listener {

    private static IslandPermission permission;

    @EventHandler
    public void onPluginInit(PluginInitializeEvent event) {
        IslandPermission.register("FLY");
        permission = IslandPermission.getByName("FLY");
    }

    @EventHandler
    public void onFly(PlayerToggleFlightEvent event) {
        Island is = IslandManager.instance().getIsland(event.getPlayer().getLocation());
        if (is == null) return;

        if (!is.hasPermission(permission, event.getPlayer())) {
            event.setCancelled(true);
            event.getPlayer().setAllowFlight(false);
            event.getPlayer().sendMessage(Lang.get(permission.getName() + "-DENY"));
        }
    }


}
