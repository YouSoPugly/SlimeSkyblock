package xyz.pugly.slimeSkyblock.island;

public enum IslandRoles {

    OWNER(4),
    COLEADER(3),
    OFFICER(2),
    MEMBER(1),
    GUEST(0);

    private final int level;

    IslandRoles(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public static IslandRoles getRoleByLevel(int level) {
        for (IslandRoles role : IslandRoles.values()) {
            if (role.getLevel() == level) {
                return role;
            }
        }
        return null;
    }

    public static IslandRoles getRoleByName(String name) {
        for (IslandRoles role : IslandRoles.values()) {
            if (role.name().equalsIgnoreCase(name)) {
                return role;
            }
        }
        return null;
    }

    public static IslandRoles getRoleByString(String string) {
        try {
            return IslandRoles.valueOf(string.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
