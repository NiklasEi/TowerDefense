package me.nikl.towerdefense.wave;

import me.nikl.towerdefense.Main;
import me.nikl.towerdefense.arena.Arena;
import net.citizensnpcs.api.ai.event.NavigationCompleteEvent;
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
}
