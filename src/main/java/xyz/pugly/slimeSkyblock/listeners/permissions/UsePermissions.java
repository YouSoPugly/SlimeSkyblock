package xyz.pugly.slimeSkyblock.listeners.permissions;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import xyz.pugly.slimeSkyblock.events.PluginInitializeEvent;
import xyz.pugly.slimeSkyblock.island.Island;
import xyz.pugly.slimeSkyblock.island.IslandManager;
import xyz.pugly.slimeSkyblock.island.permissions.IslandPermission;
import xyz.pugly.slimeSkyblock.utils.Lang;

public class UsePermissions implements Listener {

    private static IslandPermission fishingPerm;
    private static IslandPermission pearlPerm;
    private static IslandPermission usePerm;

    @EventHandler
    public void onPluginInit(PluginInitializeEvent event) {
        IslandPermission.register("FISHING");
        IslandPermission.register("ENDER-PEARL");
        IslandPermission.register("USE");
        fishingPerm = IslandPermission.getByName("FISHING");
        pearlPerm = IslandPermission.getByName("ENDER-PEARL");
        usePerm = IslandPermission.getByName("USE");
    }

    // Rod or pearl
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getItem() == null) return;
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getItem().getType() != Material.ENDER_PEARL && e.getItem().getType() != Material.FISHING_ROD) return;


        Island is = IslandManager.instance().getIsland(e.getPlayer().getLocation());
        if (is == null) return;

        if (e.getItem().getType().equals(Material.FISHING_ROD) && !is.hasPermission(fishingPerm, e.getPlayer())) {
            e.getPlayer().sendMessage(Lang.get(fishingPerm.getName() + "-DENY"));
            e.setCancelled(true);
        }
        else if (e.getItem().getType().equals(Material.ENDER_PEARL) && !is.hasPermission(pearlPerm, e.getPlayer())) {
            e.getPlayer().sendMessage(Lang.get(pearlPerm.getName() + "-DENY"));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onUse(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) return;
        if (e.getAction() != Action.PHYSICAL && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Material type = e.getClickedBlock().getType();
        boolean isUseable = Tag.PRESSURE_PLATES.isTagged(type)
                || Tag.BUTTONS.isTagged(type)
                || type.equals(Material.LEVER)
                || type.equals(Material.TRIPWIRE_HOOK)
                || type.equals(Material.REPEATER)
                || type.equals(Material.COMPARATOR)
                || type.equals(Material.DAYLIGHT_DETECTOR);

        if (!isUseable) return;

        Island is = IslandManager.instance().getIsland(e.getPlayer().getLocation());
        if (is == null) return;

        if (!is.hasPermission(usePerm, e.getPlayer())) {
            e.getPlayer().sendMessage(Lang.get(usePerm.getName() + "-DENY"));
            e.setCancelled(true);
        }
    }


}
