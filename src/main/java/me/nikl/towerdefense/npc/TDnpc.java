package me.nikl.towerdefense.npc;

import me.nikl.towerdefense.Main;
import net.citizensnpcs.Citizens;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.npc.CitizensNPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;

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

        monster.data().set(NPC.DEFAULT_PROTECTED_METADATA, true);
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
        Main.debug("despawning npc...");
        Main.debug("Entity null? " + (monster.getBukkitEntity() == null));
        Citizens citizens = (Citizens) Bukkit.getPluginManager().getPlugin("Citizens");
        monster.getNavigator().cancelNavigation();
        //monster.despawn();
        citizens.getNPCRegistry().deregister(monster);
        Main.debug("Entity null? " + (monster.getBukkitEntity() == null));
    }

    public boolean isSpawned() {
        return monster.isSpawned();
    }
}
