package xyz.pugly.slimeSkyblock.island.savers;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import xyz.pugly.slimeSkyblock.SlimeSkyblock;
import xyz.pugly.slimeSkyblock.island.Island;
import xyz.pugly.slimeSkyblock.player.SPlayer;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class YMLSaver extends Saver {

    //TODO: Async file management

    private String folder;

    public YMLSaver(String folder) {
        this.folder = folder;

        File dataFolder = new File(SlimeSkyblock.get().getDataFolder().getPath() + "/" + folder);
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        File islandFolder = new File(SlimeSkyblock.get().getDataFolder().getPath() + "/islands");
        if (!islandFolder.exists()) {
            islandFolder.mkdirs();
        }

        File playerFolder = new File(SlimeSkyblock.get().getDataFolder().getPath() + "/players");
        if (!playerFolder.exists()) {
            playerFolder.mkdirs();
        }

    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    @Override
    public void saveIsland(Island island) {
        File islandFolder = new File(SlimeSkyblock.get().getDataFolder().getPath() + "/" + folder + "/islands");

        YamlConfiguration islandInfo = new YamlConfiguration();

        islandInfo.set("owner", island.getOwner().getUniqueId().toString());
        islandInfo.set("xp", island.getXp());
        islandInfo.set("hoppers", island.getHoppers());
        islandInfo.set("private", island.isPrivate());
        islandInfo.set("generatorTier", island.getGeneratorTier());

        YamlConfiguration members = new YamlConfiguration();
        for (UUID member : island.getMembers()) {
            members.set(member.toString(), island.getRole(member).toString());
        }
        islandInfo.set("members", members);

        HashSet<String> inviteStrings = new HashSet<>();
        for (UUID invite : island.getInvites()) {
            inviteStrings.add(invite.toString());
        }
        islandInfo.set("invites", inviteStrings);

        HashSet<String> banStrings = new HashSet<>();
        for (UUID ban : island.getBans()) {
            banStrings.add(ban.toString());
        }
        islandInfo.set("bans", banStrings);

        islandInfo.set("permissions", island.getPermissions().serialize());
        islandInfo.set("flags", island.getFlags().serialize());

        try {
            islandInfo.save(new File(islandFolder, island.getId().toString() + ".yml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TODO: This method is never called, the Island class & manager need to be adjusted to make this work.
    @Override
    public Island loadIsland(String islandID) {
        File islandFolder = new File(SlimeSkyblock.get().getDataFolder().getPath() + "/" + folder + "/islands");

        File islandFile = new File(islandFolder, islandID + ".yml");
        if (!islandFile.exists()) {
            return null;
        }

        YamlConfiguration islandInfo = new YamlConfiguration();
        try {
            islandInfo.load(islandFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Island(UUID.fromString(islandID), islandInfo);
    }

    @Override
    public Set<String> getIslandIDs() {
        File islandFolder = new File(SlimeSkyblock.get().getDataFolder().getPath() + "/" + folder + "/islands");

        Set<String> islandIDs = new HashSet<>();
        for (File file : islandFolder.listFiles()) {
            if (file.getName().endsWith(".yml")) {
                islandIDs.add(file.getName().replace(".yml", ""));
            }
        }

        return islandIDs;
    }

    @Override
    public void savePlayer(SPlayer player) {
        File playerFolder = new File(SlimeSkyblock.get().getDataFolder().getPath() + "/" + folder + "/players");

        YamlConfiguration playerInfo = new YamlConfiguration();

        playerInfo.set("islands", player.getIslands());

        try {
            playerInfo.save(new File(playerFolder, player.getPlayer().getUniqueId() + ".yml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public SPlayer loadPlayer(String playerID) {

        File playerFolder = new File(SlimeSkyblock.get().getDataFolder().getPath() + "/" + folder + "/players");

        File playerFile = new File(playerFolder, playerID + ".yml");
        if (!playerFile.exists()) {
            return null;
        }

        YamlConfiguration playerInfo = new YamlConfiguration();
        try {
            playerInfo.load(playerFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Player player = Bukkit.getPlayer(UUID.fromString(playerID));

        UUID mainIsland = playerInfo.getString("mainIsland") != null ? UUID.fromString(playerInfo.getString("mainIsland")) : null;

        HashSet<UUID> islands = new HashSet<>();
        islands = (HashSet<UUID>) playerInfo.get("islands", islands);

        return new SPlayer(player, mainIsland, islands);
    }

    @Override
    public Set<String> getPlayerIDs() {
        File playerFolder = new File(SlimeSkyblock.get().getDataFolder().getPath() + "/" + folder + "/players");

        Set<String> playerIDs = new HashSet<>();
        for (File file : playerFolder.listFiles()) {
            if (file.getName().endsWith(".yml")) {
                playerIDs.add(file.getName().replace(".yml", ""));
            }
        }

        return playerIDs;
    }
}
