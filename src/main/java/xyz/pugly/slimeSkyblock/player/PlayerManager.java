package xyz.pugly.slimeSkyblock.player;

import org.bukkit.entity.Player;
import xyz.pugly.slimeSkyblock.SlimeSkyblock;

import java.util.HashMap;

public class PlayerManager {

    private static final HashMap<Player, SPlayer> players = new HashMap<>();

    public static SPlayer getSPlayer(Player player) {
        if (!players.containsKey(player)) {
            loadSPlayer(player);
        }
        return players.get(player);
    }

    public static void addSPlayer(Player player) {
        loadSPlayer(player);
    }

    public static void removeSPlayer(Player player) {
        unloadSPlayer(player);
    }

    private static void loadSPlayer(Player player) {
        if (players.containsKey(player)) {
            return;
        }

        if (!SlimeSkyblock.getSaver().getPlayerIDs().contains(player.getUniqueId().toString())) {
            players.put(player, new SPlayer(player));
            return;
        }

        players.put(player, SlimeSkyblock.getSaver().loadPlayer(player.getUniqueId().toString()));
    }

    private static void unloadSPlayer(Player player) {
        SlimeSkyblock.getSaver().savePlayer(players.get(player));
        players.remove(player);
    }

    public static void saveAllPlayers() {
        for (Player player : players.keySet()) {
            SlimeSkyblock.getSaver().savePlayer(players.get(player));
        }
    }

    public static void unloadAllPlayers() {
        for (Player player : players.keySet()) {
            SlimeSkyblock.getSaver().savePlayer(players.get(player));
        }
        players.clear();
    }
}
