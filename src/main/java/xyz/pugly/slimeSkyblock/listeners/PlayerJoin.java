package xyz.pugly.slimeSkyblock.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.pugly.slimeSkyblock.island.IslandManager;
import xyz.pugly.slimeSkyblock.player.PlayerManager;

import java.util.UUID;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        PlayerManager.addSPlayer(e.getPlayer());


        IslandManager im = IslandManager.instance();
        UUID id = e.getPlayer().getUniqueId();

        if (!im.islandExists(id))
            return;

        im.loadIsland(id);
    }

}
