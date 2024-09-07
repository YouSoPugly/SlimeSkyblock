package xyz.pugly.slimeSkyblock.island;

import xyz.pugly.slimeSkyblock.SlimeSkyblock;

import java.util.HashMap;
import java.util.HashSet;

public class IslandPermission {

    // Permission

    private String name;

    private IslandPermission(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // Static Methods

    private static HashMap<String, IslandPermission> permissions = new HashMap<>();

    public static void register(String name) {
        permissions.put(name, new IslandPermission(name));
    }

    public static HashMap<String, IslandPermission> getPermissions() {
        return permissions;
    }

    public static IslandPermission getByName(String name) {
        if (!permissions.containsKey(name)) {
            SlimeSkyblock.warn("Island permission " + name + " does not exist.");
            return null;
        }
        return permissions.get(name);
    }

}
