package xyz.pugly.slimeSkyblock.island;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import xyz.pugly.slimeSkyblock.SlimeSkyblock;

import java.util.HashMap;
import java.util.HashSet;

public class IslandPermissionHolder {

    private HashMap<IslandPermission, IslandRoles> permissions = new HashMap<>();

    public IslandPermissionHolder(ConfigurationSection perms) {
        for (String perm : perms.getKeys(false)) {
            IslandPermission permission = IslandPermission.getByName(perm);
            if (permission == null) {
                continue;
            }
            permissions.put(permission, IslandRoles.valueOf(perms.getString(perm)));
        }
    }

    public IslandPermissionHolder() {
        ConfigurationSection cs = SlimeSkyblock.get().getConfig().getConfigurationSection("default-permissions");

        for (String perm : cs.getKeys(false)) {
            IslandPermission permission = IslandPermission.getByName(perm);
            if (permission == null) {
                continue;
            }
            permissions.put(permission, IslandRoles.valueOf(cs.getString(perm)));
        }
    }

    public boolean hasPermission(IslandPermission permission, IslandRoles role) {
        return permissions.get(permission).getLevel() <= role.getLevel();
    }

    public ConfigurationSection serialize() {
        ConfigurationSection cs = new YamlConfiguration();

        for (IslandPermission perm : permissions.keySet()) {
            cs.set(perm.getName(), permissions.get(perm).name());
        }

        return cs;
    }

}
