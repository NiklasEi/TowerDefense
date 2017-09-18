package me.nikl.towerdefense.arena;

import me.nikl.towerdefense.Main;

import java.util.HashMap;

/**
 * Created by Niklas on 18.09.2017.
 */
public class ArenaManager {

    private Main plugin;

    private HashMap<String, Arena> arenas = new HashMap<>();

    public ArenaManager(Main plugin){
        this.plugin = plugin;
    }

    public boolean createArena(String arenaName){
        if(arenas.containsKey(arenaName)) return false;

        arenas.put(arenaName, new Arena(plugin, arenaName));
        return true;
    }

    public Arena getArena(String arenaName){
        return arenas.get(arenaName);
    }

    public void shutDown() {
        for(Arena arena : arenas.values()){
            arena.shutDown();
        }
        // ToDo: save arenas
    }
}
