package xyz.pugly.slimeSkyblock.listeners.permissions;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import xyz.pugly.slimeSkyblock.events.PluginInitializeEvent;
import xyz.pugly.slimeSkyblock.island.Island;
import xyz.pugly.slimeSkyblock.island.IslandManager;
import xyz.pugly.slimeSkyblock.island.IslandPermission;
import xyz.pugly.slimeSkyblock.utils.Lang;

public class SpawnerPermissions implements Listener {

    private static IslandPermission breakSpawner;
    private static IslandPermission placeSpawner;

    @EventHandler
    public void onPluginInit(PluginInitializeEvent event) {
        IslandPermission.register("BREAK_SPAWNER");
        IslandPermission.register("PLACE_SPAWNER");

        breakSpawner = IslandPermission.getByName("BREAK_SPAWNER");
        placeSpawner = IslandPermission.getByName("PLACE_SPAWNER");
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        // Check if player is in an island
        IslandManager im = IslandManager.instance();
        Island island = im.getIsland(e.getBlock().getLocation());
        if (island == null) return;

        if (e.getBlock().getType().equals(Material.SPAWNER) && !island.hasPermission(breakSpawner, e.getPlayer())) {
            e.getPlayer().sendMessage(Lang.get(breakSpawner.getName() + "_DENY"));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        // Check if player is in an island
        IslandManager im = IslandManager.instance();
        Island island = im.getIsland(e.getBlock().getLocation());
        if (island == null) return;

        if (e.getBlock().getType().equals(Material.SPAWNER) && !island.hasPermission(placeSpawner, e.getPlayer())) {
            e.getPlayer().sendMessage(Lang.get(placeSpawner.getName() + "_DENY"));
            e.setCancelled(true);
        }
    }


}
