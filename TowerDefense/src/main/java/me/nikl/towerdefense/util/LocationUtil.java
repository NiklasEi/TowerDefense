package me.nikl.towerdefense.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Locale;

/**
 * Created by Niklas on 18.09.2017.
 */
public class LocationUtil {

    public static String serializeLoc(Location loc){
        if(loc == null) return "null";
        return loc.getWorld().getName() + ":" + String.format(Locale.US,"%.2f", loc.getX()) +
                ":" + String.format(Locale.US,"%.2f", loc.getY()) + ":" + String.format(Locale.US,"%.2f", loc.getZ()) +
                ":" + String.format(Locale.US,"%.2f", loc.getPitch()) + ":" + String.format(Locale.US,"%.2f", loc.getYaw());
    }

    public static Location deSerializeLoc(String locationString){
        String[] locSplit = locationString.split(":");

        if(locSplit.length != 6) return null;

        World world = Bukkit.getWorld(locSplit[0]);

        double x,y,z;
        float pitch, yaw;

        try {
            x = Double.valueOf(locSplit[1]);
            y = Double.valueOf(locSplit[2]);
            z = Double.valueOf(locSplit[3]);

            pitch = Float.valueOf(locSplit[4]);
            yaw = Float.valueOf(locSplit[5]);

            return new Location(world, x, y, z, pitch, yaw);

        } catch (NumberFormatException exception){
            exception.printStackTrace();
        }

        return null;
    }
}
