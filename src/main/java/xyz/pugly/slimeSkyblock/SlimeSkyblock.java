package xyz.pugly.slimeSkyblock;

import com.infernalsuite.aswm.api.AdvancedSlimePaperAPI;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import mc.obliviate.inventory.InventoryAPI;
import mc.obliviate.inventory.configurable.ConfigurableGuiCache;
import mc.obliviate.inventory.configurable.GuiConfigurationTable;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.pugly.slimeSkyblock.commands.IslandCommand;
import xyz.pugly.slimeSkyblock.commands.SlimeSkyblockCommand;
import xyz.pugly.slimeSkyblock.events.PluginInitializeEvent;
import xyz.pugly.slimeSkyblock.hooks.PAPIExpansion;
import xyz.pugly.slimeSkyblock.island.IslandManager;
import xyz.pugly.slimeSkyblock.island.permissions.IslandPermission;
import xyz.pugly.slimeSkyblock.listeners.BlockBreak;
import xyz.pugly.slimeSkyblock.listeners.BlockForm;
import xyz.pugly.slimeSkyblock.listeners.BlockPlace;
import xyz.pugly.slimeSkyblock.listeners.PlayerJoin;
import xyz.pugly.slimeSkyblock.listeners.WorldUnload;
import xyz.pugly.slimeSkyblock.listeners.flags.SpawnFlags;
import xyz.pugly.slimeSkyblock.listeners.permissions.AttackPermissions;
import xyz.pugly.slimeSkyblock.listeners.permissions.BreakPermission;
import xyz.pugly.slimeSkyblock.listeners.permissions.BuildPermission;
import xyz.pugly.slimeSkyblock.listeners.permissions.CropTramplePermission;
import xyz.pugly.slimeSkyblock.listeners.permissions.FlyPermission;
import xyz.pugly.slimeSkyblock.listeners.permissions.InteractPermission;
import xyz.pugly.slimeSkyblock.listeners.permissions.ItemFramePermission;
import xyz.pugly.slimeSkyblock.listeners.permissions.ItemPermissions;
import xyz.pugly.slimeSkyblock.listeners.permissions.LeashPermission;
import xyz.pugly.slimeSkyblock.listeners.permissions.NameEntityPermission;
import xyz.pugly.slimeSkyblock.listeners.permissions.SignPermission;
import xyz.pugly.slimeSkyblock.listeners.permissions.SpawnerPermissions;
import xyz.pugly.slimeSkyblock.listeners.permissions.UsePermissions;
import xyz.pugly.slimeSkyblock.utils.Lang;

import java.io.File;

public final class SlimeSkyblock extends JavaPlugin {

    private static SlimeSkyblock instance;

    private BlockForm blockForm;

    private static boolean PAPI = false;

    @Override
    public void onEnable() {
        instance = this;

        // TODO temp delete old config
        File f = new File(getDataFolder(), "config.yml");
        f.delete();

        saveDefaultConfig();

        // Load the AdvancedSlimePaperAPI
        try {
            AdvancedSlimePaperAPI api = AdvancedSlimePaperAPI.instance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // PAPI
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            PAPI = true;
            new PAPIExpansion(this).register();
        }

        // Obliviate GUI
        new InventoryAPI(this).init();
        ConfigurableGuiCache.resetCaches();
        GuiConfigurationTable.setDefaultConfigurationTable(new GuiConfigurationTable(getConfig()));

        // Command API
        CommandAPI.onEnable();
        CommandAPI.registerCommand(IslandCommand.class);
        CommandAPI.registerCommand(SlimeSkyblockCommand.class);

        // Listeners
        islandPermissions();
        islandFlags();
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new WorldUnload(), this);
        getServer().getPluginManager().registerEvents(new BlockPlace(), this);
        getServer().getPluginManager().registerEvents(new BlockBreak(), this);
        BlockForm blockForm = new BlockForm(getConfig().getBoolean("custom-generator"), getConfig().getConfigurationSection("generator-tiers"));
        getServer().getPluginManager().registerEvents(blockForm, this);

        // Send plugin init event
        PluginInitializeEvent event = new PluginInitializeEvent();
        getServer().getPluginManager().callEvent(event);

        // Plugin startup logic
        IslandManager.instance();
        Lang.load(getConfig().getString("language"));

        getLogger().info("SlimeSkyblock has been enabled!");
    }

    @Override
    public void onLoad() {
        // Plugin load logic
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        IslandManager.instance().forceSaveAll();
        getLogger().info("SlimeSkyblock has been disabled!");
    }

    public static SlimeSkyblock get() {
        return instance;
    }

    public static void info(String message) {
        instance.getLogger().info(message);
    }

    public static void warn(String message) {
        instance.getLogger().warning(message);
    }

    public void reload() {
        reloadConfig();
        Lang.load(getConfig().getString("language"));
        blockForm.reload(getConfig().getBoolean("custom-generator"), getConfig().getConfigurationSection("generator-tiers"));
    }

    private void islandFlags() {
        getServer().getPluginManager().registerEvents(new SpawnFlags(), this);
    }

    private void islandPermissions() {
        getServer().getPluginManager().registerEvents(new AttackPermissions(), this);
        getServer().getPluginManager().registerEvents(new BreakPermission(), this);
        getServer().getPluginManager().registerEvents(new BuildPermission(), this);
        getServer().getPluginManager().registerEvents(new CropTramplePermission(), this);
        getServer().getPluginManager().registerEvents(new FlyPermission(), this);
        getServer().getPluginManager().registerEvents(new InteractPermission(), this);
        getServer().getPluginManager().registerEvents(new ItemFramePermission(), this);
        getServer().getPluginManager().registerEvents(new ItemPermissions(), this);
        getServer().getPluginManager().registerEvents(new LeashPermission(), this);
        getServer().getPluginManager().registerEvents(new NameEntityPermission(), this);
        getServer().getPluginManager().registerEvents(new SignPermission(), this);
        getServer().getPluginManager().registerEvents(new SpawnerPermissions(), this);
        getServer().getPluginManager().registerEvents(new UsePermissions(), this);

        IslandPermission.register("LOCK");
        IslandPermission.register("SETHOME");
    }

    public boolean usePAPI() {
        return PAPI;
    }
}
