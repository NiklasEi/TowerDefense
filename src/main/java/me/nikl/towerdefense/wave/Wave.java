package me.nikl.towerdefense.wave;

import me.nikl.towerdefense.Main;
import me.nikl.towerdefense.arena.Arena;
import me.nikl.towerdefense.npc.TDnpc;
import me.nikl.towerdefense.npc.monster.Monster;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Created by Niklas on 18.09.2017.
 *
 *
 */
public class Wave {

    LinkedList<Monster> monsters;
    LinkedList<TDnpc> npcs = new LinkedList<>();
    int index;
    private Arena arena;

    private BukkitTask timer;

    public Wave(Arena arena, LinkedList<Monster> monsters){
        this.arena = arena;
        this.monsters = monsters;
    }

    public void start() {
        index = 0;
        timer = new BukkitRunnable(){
            @Override
            public void run() {
                spawnNext();
                index++;

                if(index == monsters.size()){
                    Main.debug("All monsters were spawned");
                    cancel();
                } else {
                    Main.debug("A monster was spawned");
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, 15);
    }

    private void spawnNext() {
        npcs.add(monsters.get(index).getNPC(arena).start());
    }

    public void shutDown(){
        if(timer != null) timer.cancel();

        for(TDnpc tDnpc : npcs){
            if(tDnpc.isSpawned()) tDnpc.despawn();
        }
    }

    public boolean isMonster(NPC npc) {
        for(TDnpc currentNpc : npcs){
            if(currentNpc.getNPC().equals(npc)){
                return true;
            }
        }
        return false;
    }

    public boolean setNextPath(NPC npc){
        for(TDnpc tdNPC : npcs){
            if(!npc.equals(tdNPC.getNPC())) continue;

            return tdNPC.nextPath();
        }
        Main.debug("A NPC was not found in its wave!");
        return false;
    }

    public boolean despawn(NPC npc) {
        Iterator<TDnpc> iterator = npcs.iterator();
        while(iterator.hasNext()){
            TDnpc tdNPC = iterator.next();
            if(!npc.equals(tdNPC.getNPC())) continue;

            iterator.remove();
            tdNPC.despawn();
            if(npcs.isEmpty()){
                arena.nextWave();
            }
            return true;
        }
        Main.debug("A NPC was not found in its wave!");
        return false;
    }
}
