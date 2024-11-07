package xyz.pugly.slimeSkyblock.utils;

public class StringUtils {

    public static String camelCase(String s) {
        String[] parts = s.split(" ");
        StringBuilder camelCaseString = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            camelCaseString.append(parts[i].substring(0, 1).toUpperCase()).append(parts[i].substring(1));
        }
        return camelCaseString.toString();
    }

}
