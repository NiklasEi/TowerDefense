package me.nikl.towerdefense.arena;

import me.nikl.towerdefense.Main;
import me.nikl.towerdefense.util.LocationUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.logging.Level;

/**
 * Created by Niklas on 18.09.2017.
 */
public class ArenaManager {

    private Main plugin;

    private HashMap<String, Arena> arenas = new HashMap<>();

    public ArenaManager(Main plugin){
        this.plugin = plugin;

        loadArenas();
    }

    private void loadArenas() {
        File arenaFolder = new File(plugin.getDataFolder().toString() + File.separatorChar + "arenas");
        if(!arenaFolder.exists()){
            arenaFolder.mkdirs();
        }

        String[] split;
        for(File file : arenaFolder.listFiles()){
            split = file.getName().split("\\.");

            if(!split[split.length - 1].equals("yml")) continue;

            loadArena(file, split[0]);
        }
    }

    private void loadArena(File file, String arenaID) {
        Arena arena = new Arena(plugin, arenaID);

        FileConfiguration save;
        try {
            save = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        if(!save.isConfigurationSection("loc")){
            plugin.getLogger().log(Level.WARNING, " Failed to load the arena " + arenaID);
            return;
        }

        for(String index : save.getConfigurationSection("loc").getKeys(false)){
            arena.addLocationToPath(LocationUtil.deSerializeLoc(save.getString("loc." + index)));
        }

        arena.initialize();

        arenas.put(arenaID, arena);
    }

    public boolean createArena(String arenaID){
        if(arenas.containsKey(arenaID)) return false;

        arenas.put(arenaID, new Arena(plugin, arenaID));
        return true;
    }

    public Arena getArena(String arenaID){
        return arenas.get(arenaID);
    }

    public void shutDown() {
        for(Arena arena : arenas.values()){
            arena.shutDown();
        }
        // ToDo: save arenas
    }
}
