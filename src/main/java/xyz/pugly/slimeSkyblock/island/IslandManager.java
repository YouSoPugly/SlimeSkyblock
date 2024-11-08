package xyz.pugly.slimeSkyblock.island;

import com.infernalsuite.aswm.api.AdvancedSlimePaperAPI;
import com.infernalsuite.aswm.api.loaders.SlimeLoader;
import com.infernalsuite.aswm.api.world.SlimeWorld;
import com.infernalsuite.aswm.api.world.properties.SlimeProperties;
import com.infernalsuite.aswm.api.world.properties.SlimePropertyMap;
import com.infernalsuite.aswm.loaders.file.FileLoader;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import xyz.pugly.slimeSkyblock.SlimeSkyblock;
import xyz.pugly.slimeSkyblock.events.IslandCreateEvent;
import xyz.pugly.slimeSkyblock.player.PlayerManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class IslandManager {

    //TODO: make this a static class

    // Instance of the IslandManager

    private static IslandManager instance;

    public static IslandManager instance() {
        if (instance == null) {
            instance = new IslandManager();
        }
        return instance;
    }

    // Island Manager

    private static AdvancedSlimePaperAPI asp = AdvancedSlimePaperAPI.instance();

    private HashMap<UUID, Island> islands;
    private final SlimeLoader loader;

    public IslandManager() {
        instance = this;
        islands = new HashMap<>();

        File islandFolder = new File(SlimeSkyblock.get().getDataFolder().getPath() + "/islands");
        if (!islandFolder.exists()) {
            islandFolder.mkdirs();
        }

        for (File file : islandFolder.listFiles()) {
            if (file.getName().endsWith(".yml")) {
                String id = file.getName().replace(".yml", "");
                YamlConfiguration islandInfo = new YamlConfiguration();
                try {
                    islandInfo.load(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                islands.put(UUID.fromString(id), new Island(UUID.fromString(id), islandInfo));
            }
        }

        SlimeSkyblock.info("Loaded " + islands.size() + " islands.");

        loader = new FileLoader(new File("islands"));
    }

    public SlimeLoader getLoader() {
        return loader;
    }

    public void forceSaveAll() {
        for (Island island : instance.islands.values()) {
            forceSaveIsland(island);
        }
    }

    public void saveAll() {
        for (Island island : instance.islands.values()) {
            saveIsland(island);
        }
    }
    public void createIsland(Player owner) {
        IslandCreateEvent event = new IslandCreateEvent(owner);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        islands.put(owner.getUniqueId(), new Island(owner.getUniqueId(), owner));
        PlayerManager.getSPlayer(owner).setMainIsland(owner.getUniqueId());
        loadIsland(owner.getUniqueId());
    }

    public Island getIsland(UUID id) {
        return islands.get(id);
    }

    public Island getIsland(World world) {
        try {
            UUID.fromString(world.getName());
        } catch (IllegalArgumentException e) {
            return null;
        }
        return islands.get(UUID.fromString(world.getName()));
    }

    public Island getIsland(Location location) {
        return getIsland(location.getWorld());
    }

    public void deleteIsland(UUID id) {
        islands.remove(id);
    }

    public void saveIsland(UUID id) {
        saveIsland(islands.get(id));
    }

    public void saveIsland(Island island) {
        SlimeSkyblock.getSaver().saveIsland(island);

        Bukkit.getScheduler().runTaskAsynchronously(SlimeSkyblock.get(), () -> {
            try {
                AdvancedSlimePaperAPI.instance().saveWorld(island.getWorld());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void forceSaveIsland(UUID id) {
        forceSaveIsland(islands.get(id));
    }

    public void forceSaveIsland(Island island) {
        saveIsland(island);

        if (island.isLoaded()) {
            try {
                AdvancedSlimePaperAPI.instance().saveWorld(island.getWorld());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean islandExists(UUID id) {
        return islands.containsKey(id);
    }

    public boolean inIsland(Player p) {
        return getIsland(p.getUniqueId()) != null;
    }

    public void loadIsland(UUID id) {

        if (islands.get(id).getLoaded()) {
            SlimeSkyblock.info("Island with id " + id + " is already loaded.");
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(SlimeSkyblock.get(), () -> {
            Island island = getIsland(id);
            if (island == null) {
                SlimeSkyblock.warn("Tried loading island with id " + id + " but it hasn't been created.");
                return;
            }

            SlimePropertyMap properties = new SlimePropertyMap();
            properties.setValue(SlimeProperties.PVP, false);
            properties.setValue(SlimeProperties.DIFFICULTY, "hard");
            properties.setValue(SlimeProperties.ALLOW_ANIMALS, true);
            properties.setValue(SlimeProperties.ALLOW_MONSTERS, true);
            properties.setValue(SlimeProperties.ENVIRONMENT, "normal");
            properties.setValue(SlimeProperties.WORLD_TYPE, "default");
            properties.setValue(SlimeProperties.DEFAULT_BIOME, "minecraft:plains");

            try {
                if (loader.worldExists(id.toString())) {
                    SlimeSkyblock.info("Loader found island with id " + id);
                    try {
                        SlimeWorld world = asp.readWorld(loader, id.toString(), false, properties);
                        Bukkit.getScheduler().runTask(SlimeSkyblock.get(), () -> {
                            island.load(world);
                        });
                        SlimeSkyblock.info("Loaded island with id " + id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    SlimeSkyblock.info("Loader didn't find island with id " + id + " cloning template world...");
                    try {
                        SlimeWorld world = asp.getLoadedWorld("template");
                        Bukkit.getScheduler().runTask(SlimeSkyblock.get(), () -> {
                            island.cloneWorld(world);
                        });
                        SlimeSkyblock.info("Cloned template world for island with id " + id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
