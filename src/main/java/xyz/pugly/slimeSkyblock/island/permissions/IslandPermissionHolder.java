package xyz.pugly.slimeSkyblock.island.permissions;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import xyz.pugly.slimeSkyblock.SlimeSkyblock;
import xyz.pugly.slimeSkyblock.island.IslandRoles;

import java.util.HashMap;

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

        fillDefaults();
    }

    public IslandPermissionHolder() {
        fillDefaults();
    }

    private void fillDefaults() {
        ConfigurationSection cs = SlimeSkyblock.get().getConfig().getConfigurationSection("default-permissions");

        for (String perm : cs.getKeys(false)) {
            if (permissions.containsKey(perm)) {
                continue;
            }
            IslandPermission permission = IslandPermission.getByName(perm);
            if (permission == null) {
                continue;
            }
            permissions.put(permission, IslandRoles.valueOf(cs.getString(perm)));
        }

        for (String key : IslandPermission.getPermissions().keySet()) {
            if (!permissions.containsKey(key)) {
                permissions.put(IslandPermission.getByName(key), IslandRoles.OWNER);
            }
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
