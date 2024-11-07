package xyz.pugly.slimeSkyblock.island.savers;

import xyz.pugly.slimeSkyblock.island.Island;
import xyz.pugly.slimeSkyblock.player.SPlayer;

import java.util.Set;

public abstract class Saver {

    public abstract void saveIsland(Island island);

    public abstract Island loadIsland(String islandID);

    public abstract Set<String> getIslandIDs();

    public abstract void savePlayer(SPlayer player);

    public abstract SPlayer loadPlayer(String playerID);

    public abstract Set<String> getPlayerIDs();

}
