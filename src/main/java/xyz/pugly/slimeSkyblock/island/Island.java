package xyz.pugly.slimeSkyblock.island;

import com.infernalsuite.aswm.api.AdvancedSlimePaperAPI;
import com.infernalsuite.aswm.api.world.SlimeWorld;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import xyz.pugly.slimeSkyblock.SlimeSkyblock;
import xyz.pugly.slimeSkyblock.events.IslandPermissionCheckEvent;
import xyz.pugly.slimeSkyblock.island.flags.IslandFlag;
import xyz.pugly.slimeSkyblock.island.flags.IslandFlagHolder;
import xyz.pugly.slimeSkyblock.island.permissions.IslandPermission;
import xyz.pugly.slimeSkyblock.island.permissions.IslandPermissionHolder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class Island {

    private final UUID id;
    private SlimeWorld world;
    private boolean loaded = false;
    private final OfflinePlayer owner;

    private int xp = 0;
    private int hoppers = 0;
    private boolean isPrivate = false;
    private int generatorTier = 0;

    private HashMap<UUID, IslandRoles> members = new HashMap<>();
    private HashSet<UUID> invites = new HashSet<>();
    private HashSet<UUID> bans = new HashSet<>();

    private IslandPermissionHolder permissions;
    private IslandFlagHolder flags;

    // Constructor

    public Island(UUID id, Player owner) {
        this.id = id;
        this.owner = owner;
        permissions = new IslandPermissionHolder();
        flags = new IslandFlagHolder();
    }

    public Island(UUID id, YamlConfiguration islandInfo) {
        this.id = id;

        this.owner = Bukkit.getOfflinePlayer(UUID.fromString(islandInfo.getString("owner")));
        this.xp = islandInfo.getInt("xp");
        this.hoppers = islandInfo.getInt("hoppers");
        this.isPrivate = islandInfo.getBoolean("private");
        this.generatorTier = islandInfo.getInt("generatorTier");

        ConfigurationSection members = islandInfo.getConfigurationSection("members");
        for (String member : members.getKeys(false)) {
            this.members.put(UUID.fromString(member), IslandRoles.valueOf(members.getString(member)));
        }

        for (String invite : islandInfo.getStringList("invites")) {
            invites.add(UUID.fromString(invite));
        }

        for (String ban : islandInfo.getStringList("bans")) {
            bans.add(UUID.fromString(ban));
        }

        if (AdvancedSlimePaperAPI.instance().getLoadedWorld(id.toString()) != null) {
            world = AdvancedSlimePaperAPI.instance().getLoadedWorld(id.toString());
            loaded = true;
        }

        if (islandInfo.contains("permissions"))
            permissions = new IslandPermissionHolder(islandInfo.getConfigurationSection("permissions"));
        else
            permissions = new IslandPermissionHolder();

        if (islandInfo.contains("flags"))
            flags = new IslandFlagHolder(islandInfo.getConfigurationSection("flags"));
        else
            flags = new IslandFlagHolder();
    }

    // World management

    public void load(SlimeWorld world) {
        if (!AdvancedSlimePaperAPI.instance().worldLoaded(world))
            AdvancedSlimePaperAPI.instance().loadWorld(world, true);
        loaded = true;
        this.world = world;
        SlimeSkyblock.info("Loaded island for " + owner.getName());
    }

    public void cloneWorld(SlimeWorld world) {
        try {
            SlimeWorld cloned = world.clone(id.toString(), IslandManager.instance().getLoader());
            load(cloned);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Saving

    public void saveData() {
        File islandFolder = new File(SlimeSkyblock.get().getDataFolder().getPath() + "/islands");
        if (!islandFolder.exists()) {
            islandFolder.mkdirs();
        }

        YamlConfiguration islandInfo = new YamlConfiguration();

        islandInfo.set("owner", owner.getUniqueId().toString());
        islandInfo.set("xp", xp);
        islandInfo.set("hoppers", hoppers);
        islandInfo.set("private", isPrivate);
        islandInfo.set("generatorTier", generatorTier);

        YamlConfiguration members = new YamlConfiguration();
        for (UUID member : this.members.keySet()) {
            members.set(member.toString(), this.members.get(member).name());
        }
        islandInfo.set("members", members);

        HashSet<String> inviteStrings = new HashSet<>();
        for (UUID invite : invites) {
            inviteStrings.add(invite.toString());
        }
        islandInfo.set("invites", inviteStrings);

        HashSet<String> banStrings = new HashSet<>();
        for (UUID ban : bans) {
            banStrings.add(ban.toString());
        }
        islandInfo.set("bans", banStrings);

        islandInfo.set("permissions", permissions.serialize());
        islandInfo.set("flags", flags.serialize());

        try {
            islandInfo.save(new File(islandFolder, id.toString() + ".yml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        saveData();

        if (!loaded)
            return;

        Bukkit.getScheduler().runTaskAsynchronously(SlimeSkyblock.get(), () -> {
            try {
                AdvancedSlimePaperAPI.instance().saveWorld(world);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void forceSave() {

        saveData();

        if (loaded) {
            try {
                AdvancedSlimePaperAPI.instance().saveWorld(world);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Unloading

    public void unloaded() {
        loaded = false;
    }

    // Actual Island Stuff

    public void teleport(Player player) {
        player.teleport(Bukkit.getWorld(id.toString()).getSpawnLocation());
    }

    public void setHome(Location l) {
        if (!loaded) {
            return;
        }
        Bukkit.getWorld(id.toString()).setSpawnLocation(l);
    }

    public boolean promoteMember(UUID member, Player player) {
        if (members.get(member).getLevel() < members.get(player.getUniqueId()).getLevel()) {
            members.put(member, IslandRoles.getRoleByLevel(members.get(member).getLevel() + 1));
            return true;
        }
        return false;
    }

    public boolean demoteMember(UUID member, Player player) {
        if (members.get(member).getLevel() < members.get(player.getUniqueId()).getLevel()) {
            members.put(member, IslandRoles.getRoleByLevel(members.get(member).getLevel() - 1));
            return true;
        }
        return false;
    }

    public boolean kick(UUID member, Player player) {
        if (members.get(member).getLevel() < members.get(player.getUniqueId()).getLevel()) {
            members.remove(member);
            return true;
        }
        return false;
    }

    public boolean hasPermission(IslandPermission permission, Player player) {
        boolean allowed = player.hasPermission("slimeskyblock.admin") || (owner.getUniqueId() == player.getUniqueId()) || permissions.hasPermission(permission, members.getOrDefault(player.getUniqueId(), IslandRoles.GUEST));
        IslandPermissionCheckEvent e = new IslandPermissionCheckEvent(player, permission, this, allowed);
        Bukkit.getPluginManager().callEvent(e);
        return e.isAllowed();
    }

    public boolean checkFlag(IslandFlag flag) {
        return flags.checkFlag(flag);
    }

    public boolean hasInvite(UUID player) {
        return invites.contains(player);
    }

    // Getters and setters

    public UUID getId() {
        return id;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public HashSet<UUID> getMembers() {
        return (HashSet<UUID>) members.keySet();
    }

    public void addMember(UUID member) {
        members.put(member, IslandRoles.MEMBER);
    }

    public void removeMember(UUID member) {
        members.remove(member);
    }

    public boolean isMember(Player player) {
        return members.containsKey(player.getUniqueId()) || player.getUniqueId().equals(owner.getUniqueId());
    }

    public HashSet<UUID> getInvites() {
        return invites;
    }

    public void setInvites(HashSet<UUID> invites) {
        this.invites = invites;
    }

    public void addInvite(UUID invite) {
        invites.add(invite);
    }

    public void removeInvite(UUID invite) {
        invites.remove(invite);
    }

    public HashSet<UUID> getBans() {
        return bans;
    }

    public void setBans(HashSet<UUID> bans) {
        this.bans = bans;
    }

    public void addBan(UUID ban) {
        bans.add(ban);
    }

    public void removeBan(UUID ban) {
        bans.remove(ban);
    }

    public OfflinePlayer getOwner() {
        return owner;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public void addXp(int xp) {
        this.xp += xp;
    }

    public int getHoppers() {
        return hoppers;
    }

    public void setHoppers(int hoppers) {
        this.hoppers = hoppers;
    }

    public void addHopper() {
        hoppers++;
    }

    public void removeHopper() {
        hoppers--;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public int getGeneratorTier() {
        return generatorTier;
    }

    public void setGeneratorTier(int generatorTier) {
        this.generatorTier = generatorTier;
    }

    public SlimeWorld getWorld() {
        return world;
    }

    public boolean getLoaded() {
        return loaded;
    }
}
