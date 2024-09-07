package xyz.pugly.slimeSkyblock.listeners;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;
import xyz.pugly.slimeSkyblock.island.Island;
import xyz.pugly.slimeSkyblock.island.IslandManager;
import xyz.pugly.slimeSkyblock.SlimeSkyblock;

import java.util.ArrayList;
import java.util.HashMap;

public class BlockForm implements Listener {

    private final ArrayList<HashMap<Material, Double>> tiers = new ArrayList<>();
    private boolean enabled = true;

    public BlockForm(boolean enabled, ConfigurationSection config) {
        reload(enabled, config);
    }

    public void reload(boolean enabled, ConfigurationSection config) {
        this.enabled = enabled;
        tiers.clear();
        for (String key : config.getKeys(false)) {

            HashMap<Material, Double> tier = new HashMap<>();
            ConfigurationSection section = config.getConfigurationSection(key);

            double total = 0;
            for (String material : section.getKeys(false)) {

                if (Material.getMaterial(material.toUpperCase()) == null) {
                    SlimeSkyblock.warn("Invalid material " + material + " in generator tier " + key + ". Skipping.");
                    continue;
                }

                tier.put(Material.getMaterial(material.toUpperCase()), section.getDouble(material));

                total += section.getDouble(material);
            }

            // I hate dealing with floating point numbers
            if (!(total >= .9999 && total <= 1.0001)) {
                SlimeSkyblock.warn("Generator tier " + key + " adds up to " + total + ". This may cause issues.");
            }

            tiers.add(tier);
        }

        SlimeSkyblock.info("Loaded " + tiers.size() + " generator tiers.");
    }

    @EventHandler
    public void onBlockGenerate(BlockFormEvent e) {
        if (!enabled)
            return;

        if (!(e.getNewState().getType() == Material.STONE || e.getNewState().getType() == Material.COBBLESTONE))
            return;

        Island island = IslandManager.instance().getIsland(e.getBlock().getLocation());
        if (island == null)
            return;

        int tier = island.getGeneratorTier();

        if (tier >= tiers.size()) {
            tier = 0;
            SlimeSkyblock.warn("Island " + island.getId() + " has an invalid generator tier. Defaulting to bottom tier.");
        }

        double random = Math.random();

        for (Material material : tiers.get(tier).keySet()) {
            random -= tiers.get(tier).get(material);
            if (random <= 0) {
                e.getNewState().setType(material);
                return;
            }
        }
    }

}
