package xyz.pugly.slimeSkyblock.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.configuration.file.YamlConfiguration;
import xyz.pugly.slimeSkyblock.island.Island;
import xyz.pugly.slimeSkyblock.SlimeSkyblock;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class Lang {

    private static final HashMap<String, String> lang = new HashMap<>();
    private static final SlimeSkyblock plugin = SlimeSkyblock.get();
    private static final MiniMessage mm = MiniMessage.miniMessage();

    public static void load(String language) {
        File langFolder = new File(plugin.getDataFolder().getPath() + File.separator + "lang");

        //TODO remove this when the plugin is released
        plugin.saveResource("lang" + File.separator + "en-US.yml", true);

        if (!langFolder.exists()) {
            SlimeSkyblock.info("Creating lang folder...");
            plugin.saveResource("lang" + File.separator + "en-US.yml", true);
        }

        SlimeSkyblock.info("Loading language file " + language + ".yml");
        File langFile = new File(langFolder.getPath() + File.separator + language + ".yml");

        if (!langFile.exists()) {
            SlimeSkyblock.warn("Language file " + language + " does not exist! Defaulting to en-US.yml");
            plugin.saveResource("lang" + File.separator + "en-US.yml", false);
            langFile = new File(plugin.getDataFolder().getPath() + "/lang/en-US.yml");
        }

        YamlConfiguration lang = new YamlConfiguration();
        try {
            lang.load(langFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (String key : lang.getKeys(false)) {

            if (lang.isList(key)) {
                List<String> text = lang.getStringList(key);
                StringBuilder builder = new StringBuilder();
                builder.append(text.get(0));
                for (int i = 1; i < text.size(); i++) {
                    builder.append("<br><reset>").append(text.get(i));
                }
                Lang.lang.put(key, builder.toString());
            }
            else {
                Lang.lang.put(key, lang.getString(key));
            }
        }
    }

    public static Component get(String key) {
        key = key.toLowerCase();
        if (!lang.containsKey(key)) {
            SlimeSkyblock.warn("Language key " + key + " not found!");
            return Component.text("Language key " + key + " not found!");
        }

        String text = lang.get(key);

        return mm.deserialize(text,
                Placeholder.parsed("prefix", lang.get("prefix")));
    }

    public static Component get(String key, Island island) {
        return mm.deserialize(lang.get(key),
                Placeholder.component("gen-tier", Component.text(island.getGeneratorTier())));
    }
}
