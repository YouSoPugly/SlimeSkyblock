package xyz.pugly.slimeSkyblock.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.pugly.slimeSkyblock.SlimeSkyblock;
import xyz.pugly.slimeSkyblock.player.PlayerManager;

public class PlayerLeave implements Listener {

    private static final long FIVE_MINUTES = 20 * 60 * 5;

    @EventHandler
    public static void onLeave(PlayerQuitEvent e) {
        Bukkit.getScheduler().runTaskLater(SlimeSkyblock.get(), () -> {
            if (!e.getPlayer().isOnline()) PlayerManager.removeSPlayer(e.getPlayer());
        }, FIVE_MINUTES
        );
    }

}
