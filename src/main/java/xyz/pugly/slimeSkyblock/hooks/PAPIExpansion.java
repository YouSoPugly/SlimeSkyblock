package xyz.pugly.slimeSkyblock.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.pugly.slimeSkyblock.SlimeSkyblock;
import xyz.pugly.slimeSkyblock.island.Island;
import xyz.pugly.slimeSkyblock.island.IslandManager;
import xyz.pugly.slimeSkyblock.utils.StringUtils;

import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

public class PAPIExpansion extends PlaceholderExpansion {

    private final SlimeSkyblock plugin;

    private final HashMap<String, BiFunction<Player, Island, String>> placeholders = new HashMap<>();
    private final HashMap<String, String> defaults = new HashMap<>();

    public PAPIExpansion(SlimeSkyblock plugin) {
        this.plugin = plugin;

        registerPlaceholder("island_xp", "0", (Player p, Island i) -> String.valueOf(i.getXp()));
        registerPlaceholder("island_hoppers", "0", (Player p, Island i) -> String.valueOf(i.getHoppers()));
        registerPlaceholder("island_private", "Private", (Player p, Island i) -> i.isPrivate() ? "Private" : "Public");
        registerPlaceholder("island_generator_tier", "0", (Player p, Island i) -> String.valueOf(i.getGeneratorTier()));
        registerPlaceholder("island_member_count", "0", (Player p, Island i) -> String.valueOf(i.getMembers().size()));
        registerPlaceholder("island_invite_count", "0", (Player p, Island i) -> String.valueOf(i.getInvites().size()));
        registerPlaceholder("island_ban_count", "0", (Player p, Island i) -> String.valueOf(i.getBans().size()));
        registerPlaceholder("island_role", "None", (Player p, Island i) -> StringUtils.camelCase(i.getRole(p.getUniqueId()).toString()));
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

    public void registerPlaceholder(@NotNull String identifier, String defaultValue, @NotNull BiFunction<Player, Island, String> getter) {
        placeholders.put(identifier, getter);
        defaults.put(identifier, defaultValue);
    }

    public String onPlaceholderRequest(Player player, String identifier) {

        Island island = IslandManager.instance().getIsland(player.getLocation());
        if (island != null) {
            return getIslandPlaceholder(player, island, identifier);
        }

        island = IslandManager.instance().getIsland(player.getUniqueId());
        if (island != null) {
            return getIslandPlaceholder(player, island, identifier);
        }

        return defaults.get(identifier);
    }

    public String getIslandPlaceholder(Player player, Island island, String identifier) {
        return placeholders.getOrDefault(identifier, (p, i) -> null).apply(player, island);
    }
}
