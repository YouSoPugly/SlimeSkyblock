package xyz.pugly.slimeSkyblock.listeners.permissions;

import org.bukkit.entity.Animals;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import xyz.pugly.slimeSkyblock.events.PluginInitializeEvent;
import xyz.pugly.slimeSkyblock.island.Island;
import xyz.pugly.slimeSkyblock.island.IslandManager;
import xyz.pugly.slimeSkyblock.island.permissions.IslandPermission;
import xyz.pugly.slimeSkyblock.utils.Lang;

public class AttackPermissions implements Listener {

    private static IslandPermission animalAttack;
    private static IslandPermission monsterAttack;

    @EventHandler
    public void onPluginInit(PluginInitializeEvent event) {
        IslandPermission.register("ANIMAL-ATTACK");
        IslandPermission.register("MONSTER-ATTACK");
        animalAttack = IslandPermission.getByName("ANIMAL-ATTACK");
        monsterAttack = IslandPermission.getByName("MONSTER-ATTACK");
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) return;

        if (!(e.getEntity() instanceof Creature)) return;

        Player player = (Player) e.getDamager();

        Island is = IslandManager.instance().getIsland(player.getLocation());
        if (is == null) return;

        if (e.getEntity() instanceof Animals) {
            if (!is.hasPermission(animalAttack, player)) {
                e.getDamager().sendMessage(Lang.get(animalAttack.getName() + "-DENY"));
                e.setCancelled(true);
            }
        } else if (e.getEntity() instanceof Monster){
            if (!is.hasPermission(monsterAttack, player)) {
                e.getDamager().sendMessage(Lang.get(monsterAttack.getName() + "-DENY"));
                e.setCancelled(true);
            }
        }

    }

}
