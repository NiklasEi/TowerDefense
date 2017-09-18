package me.nikl.towerdefense.util;

import org.bukkit.ChatColor;
import org.bukkit.Location;

/**
 * Created by Niklas on 18.09.2017.
 */
public class StringUtil {

    public static String color(String string){
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String serializeLoc(Location loc){
        return loc.getWorld().getName() + ":" + String.format("%.2f", loc.getX()) +
                ":" + String.format("%.2f", loc.getY()) + ":" + String.format("%.2f", loc.getZ()) +
                ":" + String.format("%.2f", loc.getPitch()) + ":" + String.format("%.2f", loc.getYaw());
    }

    public static Location deSerializeLoc(String locationString){
        return null;
    }


}
