package me.nikl.towerdefense.command;

import me.nikl.towerdefense.Main;
import net.citizensnpcs.Citizens;
import net.citizensnpcs.api.ai.AttackStrategy;
import net.citizensnpcs.api.ai.Goal;
import net.citizensnpcs.api.ai.GoalSelector;
import net.citizensnpcs.api.ai.event.NavigationCompleteEvent;
import net.citizensnpcs.api.event.NPCDamageEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Iterator;

/**
 * Created by Niklas on 18.09.2017.
 */
public class Testing implements CommandExecutor, Listener {

    private Main plugin;


    private NPC npc;
    Location loc1 = new Location(Bukkit.getWorld("world"), 188.5, 99, 233.5);
    Location loc2 = new Location(Bukkit.getWorld("world"), 198.5, 99, 233.5);
    Location loc3 = new Location(Bukkit.getWorld("world"), 198.5, 99, 223.5);
    Location loc4 = new Location(Bukkit.getWorld("world"), 191.5, 99, 223.5);

    Citizens citizens;

    public Testing(Main plugin){
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        citizens = (Citizens) plugin.getServer().getPluginManager().getPlugin("Citizens");
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        // no argument
        if(strings.length == 0) {
            citizens.getNPCRegistry().deregisterAll();
            npc = citizens.getNPCRegistry().createNPC(EntityType.ZOMBIE, "Boy");
            //npc.getDefaultGoalController().clear();
            npc.getNavigator().getLocalParameters().distanceMargin(0.01);
            npc.getNavigator().getLocalParameters().baseSpeed(1f);
            npc.getNavigator().getLocalParameters().range(50f);

            npc.spawn(loc1);
            npc.getNavigator().setTarget(loc2);
            plugin.debug("navigating: "+npc.getNavigator().isNavigating());
        }

        // one argument
        else if(strings.length == 1 && strings[0].equalsIgnoreCase("removeall")){
            citizens.getNPCRegistry().deregisterAll();
        } else if(strings.length == 1 && strings[0].equalsIgnoreCase("remove")){
            citizens.getNPCRegistry().deregister(npc);
        }

        return true;
    }



    @EventHandler
    public void onNavigationComplete(NavigationCompleteEvent event){
        if(event.getNPC() != npc) return;

        Main.debug("nv complete...");

        npc.getNavigator().setTarget(loc3);
    }

    @EventHandler
    public void onDamage(NPCDamageEvent event){
        if(event.getNPC() != npc) return;
        Main.debug("damage event");
    }
}
