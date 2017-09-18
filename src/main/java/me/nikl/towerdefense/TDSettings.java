package me.nikl.towerdefense;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * Created by Niklas on 18.09.2017.
 *
 */
public class TDSettings {
    public static boolean bStats = true;

    public static boolean econEnabled = true;

    public static void loadSettings(Main plugin){
        FileConfiguration config = plugin.getConfig();

        bStats = config.getBoolean("bStats", true);
        econEnabled = config.getBoolean("econ.enabled", true);
    }
}
