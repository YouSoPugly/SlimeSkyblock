package xyz.pugly.slimeSkyblock.island.flags;

import xyz.pugly.slimeSkyblock.SlimeSkyblock;

import java.util.HashMap;

public class IslandFlag {

    // Flag

    private String name;

    private IslandFlag(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // Static Methods

    private static HashMap<String, IslandFlag> flags = new HashMap<>();

    public static void register(String name) {
        flags.put(name, new IslandFlag(name));
    }

    public static HashMap<String, IslandFlag> getFlags() {
        return flags;
    }

    public static IslandFlag getByName(String name) {
        if (!flags.containsKey(name)) {
            SlimeSkyblock.warn("Island flag " + name + " does not exist.");
            return null;
        }
        return flags.get(name);
    }

}
