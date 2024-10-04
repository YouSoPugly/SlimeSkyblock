package xyz.pugly.slimeSkyblock.island.flags;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import xyz.pugly.slimeSkyblock.SlimeSkyblock;
import xyz.pugly.slimeSkyblock.island.IslandRoles;

import java.util.HashMap;

public class IslandFlagHolder {

    private HashMap<IslandFlag, Boolean> flags = new HashMap<>();

    public IslandFlagHolder(ConfigurationSection flagSection) {
        for (String f : flagSection.getKeys(false)) {
            IslandFlag flag = IslandFlag.getByName(f);
            if (flag == null) {
                continue;
            }
            flags.put(flag, flagSection.getBoolean(f));
        }

        fillDefaults();
    }

    public IslandFlagHolder() {
        fillDefaults();
    }

    private void fillDefaults() {
        ConfigurationSection cs = SlimeSkyblock.get().getConfig().getConfigurationSection("default-flags");

        for (String f : cs.getKeys(false)) {
            if (flags.containsKey(f)) {
                continue;
            }
            IslandFlag flag = IslandFlag.getByName(f);
            if (flag == null) {
                continue;
            }
            flags.put(flag, cs.getBoolean(f));
        }

        for (String key : IslandFlag.getFlags().keySet()) {
            if (!flags.containsKey(key)) {
                flags.put(IslandFlag.getByName(key), false);
            }
        }
    }

    public boolean checkFlag(IslandFlag flag) {
        return flags.get(flag);
    }

    public ConfigurationSection serialize() {
        ConfigurationSection cs = new YamlConfiguration();

        for (IslandFlag flag : flags.keySet()) {
            cs.set(flag.getName(), flags.get(flag));
        }

        return cs;
    }

}
