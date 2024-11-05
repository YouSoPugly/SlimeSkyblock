package xyz.pugly.slimeSkyblock.island.savers;

import org.bukkit.configuration.file.YamlConfiguration;
import xyz.pugly.slimeSkyblock.SlimeSkyblock;
import xyz.pugly.slimeSkyblock.island.Island;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class YMLSaver extends Saver {

    private String folder;

    public YMLSaver(String folder) {
        this.folder = folder;

        File islandFolder = new File(SlimeSkyblock.get().getDataFolder().getPath() + "/" + folder);
        if (!islandFolder.exists()) {
            islandFolder.mkdirs();
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
        File islandFolder = new File(SlimeSkyblock.get().getDataFolder().getPath() + "/" + folder);

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
        File islandFolder = new File(SlimeSkyblock.get().getDataFolder().getPath() + "/" + folder);

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
        File islandFolder = new File(SlimeSkyblock.get().getDataFolder().getPath() + "/" + folder);

        Set<String> islandIDs = new HashSet<>();
        for (File file : islandFolder.listFiles()) {
            if (file.getName().endsWith(".yml")) {
                islandIDs.add(file.getName().replace(".yml", ""));
            }
        }

        return islandIDs;
    }
}
