package xyz.pugly.slimeSkyblock.commands;

import dev.jorel.commandapi.annotations.Alias;
import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Permission;
import dev.jorel.commandapi.annotations.Subcommand;
import dev.jorel.commandapi.annotations.arguments.ALiteralArgument;
import dev.jorel.commandapi.annotations.arguments.AMultiLiteralArgument;
import dev.jorel.commandapi.annotations.arguments.APlayerArgument;
import dev.jorel.commandapi.arguments.OfflinePlayerArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.pugly.slimeSkyblock.island.Island;
import xyz.pugly.slimeSkyblock.island.IslandManager;
import xyz.pugly.slimeSkyblock.island.flags.IslandFlag;
import xyz.pugly.slimeSkyblock.island.permissions.IslandPermission;
import xyz.pugly.slimeSkyblock.utils.Lang;

@Command("island")
@Alias("is")
public class IslandCommand {

    @Default
    public static void island(CommandSender sender) {
        sender.sendMessage(Lang.get("island-help"));
    }

    @Subcommand("create")
    @Permission("slimeskyblock.island.create")
    public static void create(CommandSender sender) {
        if (!(sender instanceof Player))
            return;

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
    public static void home(Player player) {
        IslandManager im = IslandManager.instance();
        Island is = im.getIsland(player.getUniqueId());

        if (is == null) {
            player.sendMessage(Lang.get("island-not-found"));
            return;
        }

        player.sendMessage(Lang.get("island-teleporting"));
        is.teleport(player);
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

        im.getIsland(((Player) sender).getLocation()).setHome(((Player) sender).getLocation());
        sender.sendMessage(Lang.get("island-home-set"));
    }

    @Subcommand({"invite", "add"})
    @Permission("slimeskyblock.island.invite")
    public static void invite(Player player, @APlayerArgument OfflinePlayer target) {
        Island is = IslandManager.instance().getIsland(player.getLocation());
        if (is == null) {
            player.sendMessage(Lang.get("island-not-found"));
            return;
        }

        if (!is.hasPermission(IslandPermission.getByName("INVITE"), player)) {
            player.sendMessage(Lang.get("invite-deny"));
            return;
        }

        is.addInvite(target.getUniqueId());
        if (target.hasPlayedBefore() && target.getPlayer().isOnline())
            target.getPlayer().sendMessage(Lang.get("island-invited", is));
    }

    @Subcommand({"uninvite"})
    @Permission("slimeskyblock.island.invite")
    public static void uninvite(Player player, @APlayerArgument OfflinePlayer target) {
        Island is = IslandManager.instance().getIsland(player.getLocation());
        if (is == null) {
            player.sendMessage(Lang.get("island-not-found"));
            return;
        }

        if (!is.hasPermission(IslandPermission.getByName("INVITE"), player)) {
            player.sendMessage(Lang.get("uninvite-deny"));
            return;
        }

        is.removeInvite(target.getUniqueId());
        if (target.hasPlayedBefore() && target.getPlayer().isOnline())
            target.getPlayer().sendMessage(Lang.get("island-uninvited", is));
    }

    @Subcommand("join")
    @Permission("slimeskyblock.island.join")
    public static void join(Player player, @APlayerArgument OfflinePlayer target) {
        Island is = IslandManager.instance().getIsland(target.getUniqueId());
        if (is == null) {
            player.sendMessage(Lang.get("island-not-found"));
            return;
        }

        if (!is.hasInvite(player.getUniqueId())) {
            player.sendMessage(Lang.get("join-deny"));
            return;
        }

        is.addMember(player.getUniqueId());
        player.sendMessage(Lang.get("island-joined", is));
    }

    @Subcommand("promote")
    @Permission("slimeskyblock.island.promote")
    public static void promote(Player player, @APlayerArgument OfflinePlayer target) {
        Island is = IslandManager.instance().getIsland(player.getLocation());
        if (is == null) {
            player.sendMessage(Lang.get("island-not-found"));
            return;
        }

        if (!is.hasPermission(IslandPermission.getByName("PROMOTE"), player)) {
            player.sendMessage(Lang.get("promote-deny"));
            return;
        }

        if (target.getPlayer() == null || !is.isMember(target.getPlayer())) {
            player.sendMessage(Lang.get("promote-not-member"));
            return;
        }

        if (is.promoteMember(target.getUniqueId(), player)) {
            player.sendMessage(Lang.get("promote-success", target));
        } else {
            player.sendMessage(Lang.get("promote-fail"));
        }
    }

    @Subcommand("demote")
    @Permission("slimeskyblock.island.demote")
    public static void demote(Player player, @APlayerArgument OfflinePlayer target) {
        Island is = IslandManager.instance().getIsland(player.getLocation());
        if (is == null) {
            player.sendMessage(Lang.get("island-not-found"));
            return;
        }

        if (!is.hasPermission(IslandPermission.getByName("DEMOTE"), player)) {
            player.sendMessage(Lang.get("demote-deny"));
            return;
        }

        if (target.getPlayer() == null || !is.isMember(target.getPlayer())) {
            player.sendMessage(Lang.get("demote-not-member"));
            return;
        }

        if (is.demoteMember(target.getUniqueId(), player)) {
            player.sendMessage(Lang.get("demote-success", target));
        } else {
            player.sendMessage(Lang.get("demote-fail"));
        }
    }

    @Subcommand("kick")
    @Permission("slimeskyblock.island.kick")
    public static void kick(Player player, @APlayerArgument OfflinePlayer target) {
        Island is = IslandManager.instance().getIsland(player.getLocation());
        if (is == null) {
            player.sendMessage(Lang.get("island-not-found"));
            return;
        }

        if (!is.hasPermission(IslandPermission.getByName("KICK"), player)) {
            player.sendMessage(Lang.get("kick-deny"));
            return;
        }

        if (target.getPlayer() == null || !is.isMember(target.getPlayer())) {
            player.sendMessage(Lang.get("kick-not-member"));
            return;
        }

        if (is.kick(target.getUniqueId(), player)) {
            player.sendMessage(Lang.get("kick-success", target));
        } else {
            player.sendMessage(Lang.get("kick-fail"));
        }
    }

    @Subcommand("leave")
    @Permission("slimeskyblock.island.leave")
    public static void leave(Player player) {
        Island is = IslandManager.instance().getIsland(player.getLocation());
        if (is == null) {
            player.sendMessage(Lang.get("island-not-found"));
            return;
        }

        if (!is.isMember(player)) {
            player.sendMessage(Lang.get("leave-not-member"));
            return;
        }

        if (is.getOwner().equals(player.getUniqueId())) {
            player.sendMessage(Lang.get("leave-owner"));
            return;
        }

        is.removeMember(player.getUniqueId());
        player.sendMessage(Lang.get("island-left"));
    }

    @Subcommand({"visit", "tp"})
    @Permission("slimeskyblock.island.visit")
    public static void visit(Player player, @APlayerArgument OfflinePlayer target) {
        Island is = IslandManager.instance().getIsland(target.getUniqueId());
        if (is == null) {
            player.sendMessage(Lang.get("island-not-found"));
            return;
        }

        if (is.isPrivate() || !is.isMember(player)) {
            player.sendMessage(Lang.get("island-locked-visit"));
            return;
        }

        player.sendMessage(Lang.get("island-teleporting"));
        is.teleport(player);
    }

    @Subcommand({"flag", "flags"})
    @Permission("slimeskyblock.island.flag")
    public static void flag(CommandSender sender) {
        sender.sendMessage(Lang.get("flag-help"));
    }

    @Subcommand({"perms", "permissions", "perm", "permission"})
    @Permission("slimeskyblock.island.permission")
    public static void permission(CommandSender sender) {
        sender.sendMessage(Lang.get("permission-help"));
    }

    @Subcommand("help")
    public static void help(CommandSender sender) {
        island(sender);
    }
}
