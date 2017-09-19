package me.nikl.towerdefense.npc;

import me.nikl.towerdefense.Main;
import net.citizensnpcs.Citizens;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.npc.CitizensNPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedList;
import java.util.UUID;

/**
 * Created by Niklas on 18.09.2017.
 */
public abstract class TDnpc {

    private NPC monster;

    protected LinkedList<Location> path = new LinkedList<>();
    protected int currentIndex = 1;

    public TDnpc(NPC monster){
        this.monster = monster;

        monster.getDefaultGoalController().clear();
        monster.getNavigator().getLocalParameters().distanceMargin(0.01);
        monster.getNavigator().getLocalParameters().baseSpeed(1f);
        monster.getNavigator().getLocalParameters().range(50f);

        monster.data().set(NPC.DEFAULT_PROTECTED_METADATA, false);
    }

    public NPC getNPC(){
        return monster;
    }

    public UUID getUuid(){
        if(monster.getBukkitEntity() == null) return null;
        return monster.getBukkitEntity().getUniqueId();
    }


    public TDnpc start(){
        monster.spawn(path.get(0));
        monster.getNavigator().setTarget(path.get(currentIndex));

        return this;
    }

    public boolean nextPath(){
        currentIndex++;
        if(currentIndex >= path.size()) return false;

        monster.getNavigator().setTarget(path.get(currentIndex));
        return true;
    }

    public void spawn(Location location){
        monster.spawn(location);
    }

    public void despawn(){
        new BukkitRunnable(){

            @Override
            public void run() {
                Main.debug("despawning npc...");
                Citizens citizens = (Citizens) Bukkit.getPluginManager().getPlugin("Citizens");
                //monster.getNavigator().cancelNavigation();
                //Main.debug("canceled nav");
                //monster.despawn();
                citizens.getNPCRegistry().deregister(monster);
                Main.debug("deregistered");
            }
        }.runTaskLater(Main.getInstance(), 2);
    }

    public boolean isSpawned() {
        return monster.isSpawned();
    }
}
