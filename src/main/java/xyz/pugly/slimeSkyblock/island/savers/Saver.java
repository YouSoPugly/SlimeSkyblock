package xyz.pugly.slimeSkyblock.island.savers;

import xyz.pugly.slimeSkyblock.island.Island;

import java.util.Set;

public abstract class Saver {

    public abstract void saveIsland(Island island);

    public abstract Island loadIsland(String islandID);

    public abstract Set<String> getIslandIDs();

}
