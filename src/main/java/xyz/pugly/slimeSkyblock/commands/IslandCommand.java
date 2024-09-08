package xyz.pugly.slimeSkyblock.commands;

import dev.jorel.commandapi.annotations.Alias;
import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Permission;
import dev.jorel.commandapi.annotations.Subcommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.pugly.slimeSkyblock.island.IslandManager;
import xyz.pugly.slimeSkyblock.island.permissions.IslandPermission;
import xyz.pugly.slimeSkyblock.utils.Lang;

@Command("island")
@Alias("is")
public class IslandCommand {

    //TODO deal with island permissions :sob:

    @Default
    public static void island(CommandSender sender) {
        // Display help message
        sender.sendMessage(Lang.get("island-help"));
    }

    @Subcommand("create")
    @Permission("slimeskyblock.island.create")
    public static void create(CommandSender sender) {
        if (!(sender instanceof Player))
            return;

        // Create island
        IslandManager im = IslandManager.instance();

        if (im.inIsland((Player) sender)) {
            sender.sendMessage(Lang.get("island-exists"));
            return;
        }

        im.createIsland((Player) sender);
        sender.sendMessage(Lang.get("island-created"));
    }

    @Subcommand({"go", "home"})
    @Permission("slimeskyblock.island.home")
    public static void home(CommandSender sender) {
        if (!(sender instanceof Player))
            return;

        IslandManager im = IslandManager.instance();

        if (!im.inIsland((Player) sender)) {
            sender.sendMessage(Lang.get("island-not-found"));
            return;
        }

        // Teleport to island
        sender.sendMessage(Lang.get("island-teleporting"));
        im.getIsland(((Player) sender).getUniqueId()).teleport((Player) sender);
    }

    @Subcommand("lock")
    @Permission("slimeskyblock.island.lock")
    public static void lock(CommandSender sender) {
        if (!(sender instanceof Player))
            return;

        IslandManager im = IslandManager.instance();

        if (!im.inIsland((Player) sender)) {
            sender.sendMessage(Lang.get("island-not-found"));
            return;
        }

        if (!im.getIsland(((Player) sender).getLocation()).hasPermission(IslandPermission.getByName("LOCK"), (Player) sender)) {
            sender.sendMessage(Lang.get("lock-deny"));
            return;
        }

        // Lock island
        im.getIsland(((Player) sender).getLocation()).setPrivate(true);
        sender.sendMessage(Lang.get("island-locked"));
    }

    @Subcommand("unlock")
    @Permission("slimeskyblock.island.unlock")
    public static void unlock(CommandSender sender) {
        if (!(sender instanceof Player))
            return;

        IslandManager im = IslandManager.instance();

        if (!im.inIsland((Player) sender)) {
            sender.sendMessage(Lang.get("island-not-found"));
            return;
        }

        if (!im.getIsland(((Player) sender).getLocation()).hasPermission(IslandPermission.getByName("LOCK"), (Player) sender)) {
            sender.sendMessage(Lang.get("unlock-deny"));
            return;
        }

        // Unlock island
        im.getIsland(((Player) sender).getLocation()).setPrivate(false);
        sender.sendMessage(Lang.get("island-unlocked"));
    }

    @Subcommand("sethome")
    @Permission("slimeskyblock.island.sethome")
    public static void sethome(CommandSender sender) {
        if (!(sender instanceof Player))
            return;

        IslandManager im = IslandManager.instance();

        if (!im.inIsland((Player) sender)) {
            sender.sendMessage(Lang.get("island-not-found"));
            return;
        }

        if (!im.getIsland(((Player) sender).getLocation()).hasPermission(IslandPermission.getByName("SETHOME"), (Player) sender)) {
            sender.sendMessage(Lang.get("sethome-deny"));
            return;
        }

        // Set home
        im.getIsland(((Player) sender).getLocation()).setHome(((Player) sender).getLocation());
        sender.sendMessage(Lang.get("island-home-set"));
    }

    @Subcommand("help")
    public static void help(CommandSender sender) {
        island(sender);
    }

}
