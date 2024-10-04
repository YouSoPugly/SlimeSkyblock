package xyz.pugly.slimeSkyblock.listeners.flags;

import org.bukkit.entity.Animals;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import xyz.pugly.slimeSkyblock.events.PluginInitializeEvent;
import xyz.pugly.slimeSkyblock.island.Island;
import xyz.pugly.slimeSkyblock.island.IslandManager;
import xyz.pugly.slimeSkyblock.island.flags.IslandFlag;

public class SpawnFlags implements Listener {

    private static IslandFlag spawnMobs;
    private static IslandFlag spawnAnimals;

    @EventHandler
    public static void onPluginInit(PluginInitializeEvent event) {
        IslandFlag.register("SPAWN-MOBS");
        IslandFlag.register("SPAWN-ANIMALS");
        spawnMobs = IslandFlag.getByName("SPAWN-MOBS");
        spawnAnimals = IslandFlag.getByName("SPAWN-ANIMALS");
    }

    @EventHandler
    public static void onSpawn(CreatureSpawnEvent e) {
        if (!e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL)
        && !e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.NETHER_PORTAL)
        && !e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.VILLAGE_INVASION)
        && !e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.DEFAULT)
        && !e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.PATROL)) {
            return;
        }

        Island is = IslandManager.instance().getIsland(e.getLocation());

        if (e.getEntity() instanceof Animals) {
            if (!is.checkFlag(spawnAnimals)) {
                e.setCancelled(true);
            }
        } else if (e.getEntity() instanceof Monster) {
            if (!is.checkFlag(spawnMobs)) {
                e.setCancelled(true);
            }
        }
    }


}
