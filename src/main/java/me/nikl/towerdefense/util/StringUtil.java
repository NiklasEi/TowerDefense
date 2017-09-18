package me.nikl.towerdefense.util;

import org.bukkit.ChatColor;

/**
 * Created by Niklas on 18.09.2017.
 */
public class StringUtil {

    public static String color(String string){
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
