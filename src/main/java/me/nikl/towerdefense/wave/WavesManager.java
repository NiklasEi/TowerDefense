package me.nikl.towerdefense.wave;

import me.nikl.towerdefense.Main;
import me.nikl.towerdefense.arena.Arena;
import net.citizensnpcs.api.ai.event.NavigationCompleteEvent;
import net.citizensnpcs.api.event.NPCCombustByBlockEvent;
import net.citizensnpcs.api.event.NPCCombustByEntityEvent;
import net.citizensnpcs.api.event.NPCCombustEvent;
import net.citizensnpcs.api.event.NPCDamageEvent;
import net.citizensnpcs.api.event.NPCDeathEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;

/**
 * Created by Niklas on 18.09.2017.
 */
public class WavesManager implements Listener{

    private Main plugin;

    private HashMap<Arena, Wave> currentWaves = new HashMap<>();

    public WavesManager(Main plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void addWave(Arena arena, Wave wave){
        currentWaves.put(arena, wave);
    }

    public void removeWave(Arena arena){
        currentWaves.remove(arena);
    }

    @EventHandler
    public void onNavigationDone(NavigationCompleteEvent event){
        Wave wave;
        for(Arena arena : currentWaves.keySet()){
            wave = currentWaves.get(arena);
            if(!wave.isMonster(event.getNPC())) continue;

            if(!wave.setNextPath(event.getNPC())){
                // monster is at the final point of arena
                wave.despawn(event.getNPC());
            }
        }
    }


    @EventHandler
    public void onNPCCombustB(NPCCombustByBlockEvent event) {
        Main.debug("NPCCombustByBlockEvent");
    }

    @EventHandler
    public void onNPCCombustE(NPCCombustByEntityEvent event) {
        Main.debug("NPCCombustByEntityEvent");
    }

    @EventHandler
    public void onNPCDamage(NPCDamageEvent event) {
        Main.debug("NPCDamageEvent");
    }

    @EventHandler
    public void onNPCDeath(NPCDeathEvent event) {
        Main.debug("NPCDeathEvent");
        Wave wave = getWaveByNPC(event.getNPC());

        if(wave == null) return;

        wave.remove(event.getNPC());
    }

    private Wave getWaveByNPC(NPC npc){
        Wave wave;
        for(Arena arena : currentWaves.keySet()) {
            wave = currentWaves.get(arena);
            if (!wave.isMonster(npc)) continue;
            return wave;
        }
        return null;
    }
}
