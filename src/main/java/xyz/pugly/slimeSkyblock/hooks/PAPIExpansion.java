package xyz.pugly.slimeSkyblock.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.pugly.slimeSkyblock.SlimeSkyblock;
import xyz.pugly.slimeSkyblock.island.Island;
import xyz.pugly.slimeSkyblock.island.IslandManager;

public class PAPIExpansion extends PlaceholderExpansion {

    private final SlimeSkyblock plugin;

    public PAPIExpansion(SlimeSkyblock plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "slimeskyblock";
    }

    @Override
    public @NotNull String getAuthor() {
        return "pugly";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    public boolean persist() {
        return true;
    }

    public boolean canRegister() {
        return true;
    }

    public String onPlaceholderRequest(Player player, String identifier) {

        Island island = IslandManager.instance().getIsland(player.getLocation());
        if (island != null) {
            return getIslandPlaceholder(island, identifier);
        }

        island = IslandManager.instance().getIsland(player.getUniqueId());
        if (island != null) {
            return getIslandPlaceholder(island, identifier);
        }

        switch (identifier) {
            case "island_xp":
                return "0";
            case "island_hoppers":
                return "0";
            case "island_members":
                return "0";
            case "island_invites":
                return "0";
            case "island_bans":
                return "0";
            case "island_generator_tier":
                return "0";
            case "island_role":
                return "NONE";
        }

        return null;
    }

    public String getIslandPlaceholder(Island island, String identifier) {
        switch (identifier) {
            case "island_xp":
                return String.valueOf(island.getXp());
            case "island_hoppers":
                return String.valueOf(island.getHoppers());
            case "island_members":
                return String.valueOf(island.getMembers().size());
            case "island_invites":
                return String.valueOf(island.getInvites().size());
            case "island_bans":
                return String.valueOf(island.getBans().size());
            case "island_generator_tier":
                return String.valueOf(island.getGeneratorTier());
            case "island_role":
                return island.getRole(island.getOwner().getUniqueId()).toString();
        }

        return null;
    }
}
