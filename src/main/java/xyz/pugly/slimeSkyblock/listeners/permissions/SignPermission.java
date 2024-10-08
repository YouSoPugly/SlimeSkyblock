package xyz.pugly.slimeSkyblock.listeners.permissions;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import xyz.pugly.slimeSkyblock.events.PluginInitializeEvent;
import xyz.pugly.slimeSkyblock.island.Island;
import xyz.pugly.slimeSkyblock.island.IslandManager;
import xyz.pugly.slimeSkyblock.island.permissions.IslandPermission;
import xyz.pugly.slimeSkyblock.utils.Lang;

public class SignPermission implements Listener {

    private static IslandPermission permission;

    @EventHandler
    public void onPluginInit(PluginInitializeEvent event) {
        IslandPermission.register("SIGN-EDIT");
        permission = IslandPermission.getByName("SIGN-EDIT");
    }

    @EventHandler
    public void onSignEdit(SignChangeEvent e) {
        Island is = IslandManager.instance().getIsland(e.getBlock().getLocation());
        if (is == null)
            return;

        if (!is.hasPermission(permission, e.getPlayer())) {
            e.getPlayer().sendMessage(Lang.get(permission.getName() + "-DENY"));
            e.setCancelled(true);
        }
    }


}
