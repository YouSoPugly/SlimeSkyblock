package xyz.pugly.slimeSkyblock.commands;

import dev.jorel.commandapi.annotations.Alias;
import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Permission;
import dev.jorel.commandapi.annotations.Subcommand;
import org.bukkit.command.CommandSender;
import xyz.pugly.slimeSkyblock.SlimeSkyblock;
import xyz.pugly.slimeSkyblock.utils.Lang;

@Command("slimeskyblock")
@Alias("ssb")
@Permission("slimeskyblock.admin")
public class SlimeSkyblockCommand {

    @Default
    public static void slimeskyblock(CommandSender sender) {
        sender.sendMessage(Lang.get("slime-skyblock-help"));
    }

    @Subcommand("reload")
    public static void reload(CommandSender sender) {
        sender.sendMessage(Lang.get("slime-skyblock-reloading"));
        SlimeSkyblock.get().reload();
        sender.sendMessage(Lang.get("slime-skyblock-reloaded"));
    }

}
