package me.nikl.towerdefense.arena;

import me.nikl.towerdefense.Main;
import me.nikl.towerdefense.util.StringUtil;
import me.nikl.towerdefense.wave.WavesManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * Created by Niklas on 18.09.2017.
 */
public class ArenaManager {

    private Main plugin;

    private WavesManager wavesManager;

    private HashMap<String, Arena> arenas = new HashMap<>();

    public ArenaManager(Main plugin){
        this.plugin = plugin;
        this.wavesManager = new WavesManager(plugin);

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

    private void loadArena(File file, String arenaName) {
        Arena arena = new Arena(plugin, arenaName);

        FileConfiguration save;
        try {
            save = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        for(String index : save.getConfigurationSection("loc").getKeys(false)){
            arena.addLocationToPath(StringUtil.deSerializeLoc(save.getString("loc." + index)));

            Main.debug(StringUtil.serializeLoc(StringUtil.deSerializeLoc(save.getString("loc." + index))));
        }

        arena.initialize();

        arenas.put(arenaName, arena);
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

    public WavesManager getWavesManager() {
        return wavesManager;
    }
}
